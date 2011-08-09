package org.mollyproject.android.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.view.apps.Page;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Router {
	protected CookieManager cookieMgr;
	protected boolean waiting;	
	protected LocationThread currentLocThread;
	protected String csrfToken;
	protected boolean firstReq;
	protected String destinationPage; //view_name of the page to go to
	//protected Context context;
	protected MyApplication myApp;
	public final static String mOX =  "http://dev.m.ox.ac.uk/";

	public static enum OutputFormat { JSON, FRAGMENT, JS, YAML, XML, HTML };

	public Router (MyApplication myApp) throws IOException, JSONException 
	{
		waiting = true;	
		this.myApp = myApp;
		cookieMgr = new CookieManager(myApp);
		firstReq = true;
		destinationPage = new String();
		currentLocThread = null;
	}
    
	public void setApp(MyApplication myApp)
    {
    	this.myApp = myApp;
    }
    
	//Take an URL String, convert to URL, open connection then process 
	//and return the response
	public static String getFrom (String urlStr) throws MalformedURLException,
											IOException, UnknownHostException, SocketTimeoutException
	{
		String outputStr = new String();		
		
		System.out.println("Router, Processing: " + urlStr);
		URLConnection revConn = new URL(urlStr).openConnection();
		revConn.setConnectTimeout(20000);
		revConn.setReadTimeout(20000);
		BufferedReader in = new BufferedReader(new InputStreamReader(
								revConn.getInputStream()));
		String inputLine;
		
		while ((inputLine = in.readLine()) != null) 
		{
			outputStr = outputStr.concat(inputLine);
		}
		in.close();
		System.out.println("Router, Output: " + outputStr);
		return outputStr;
	}
	
	public JSONObject exceptionHandledOnRequestSent(String locator, String arg, Page page, OutputFormat format, String query)
	{
		try{
			return onRequestSent(locator, arg, format, query);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			Page.popupErrorDialog("Malformed URL (Router)",
					"Please try restarting the app", page);
			
		}catch (JSONException e)
		{
			e.printStackTrace();
			Page.popupErrorDialog("JSON Exception (Router)", 
					"There might be a problem with JSON output " +
					"from server. Please try again later", page);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Page.popupErrorDialog("Unknown Host Exception (Router)", 
					"There might be a problem with network connection. " +
					"Please try later.", page);
		}  catch (IOException e)
		{
			e.printStackTrace();
			Page.popupErrorDialog("I/O Exception (Router)", 
					"There might be a problem with network connection. " +
					"Please try later.", page);
		} 
		return null;
	}
	
	public JSONObject onRequestSent(String locator, String arg, OutputFormat format, String query) 
							throws JSONException, UnknownHostException, IOException {
		//to be included in AsyncTask subclasses where no UI is allowed
		//in the doInBackground method
		
		//Geting the actual URL from the server using the locator (view name)
		//and the reverse API in Molly
		System.out.println("Is waiting "+waiting);
		//if (waiting) {
			waiting = false;
			//if an exception is thrown, waiting might be false forever
			String urlStr = new String();
			String reverseReq = new String();
			if (arg != null)
			{
				reverseReq = mOX + "reverse/?name="+locator + "&arg=" + arg;
			}
			else
			{
				reverseReq = mOX + "reverse/?name="+locator;
			}
			urlStr = getFrom(reverseReq);
			String outputStr = new String();
			
			switch(format){
			//Depending on the format wanted, get the output
			case JSON:
				urlStr = urlStr+"?format=json";
				break;
			}
			
			if (query != null)
			{
				urlStr = urlStr+"&"+query;
			}
			
			outputStr = getFrom(urlStr);
			
			if (firstReq)
			{ 
				URL url = new URL(urlStr);
				cookieMgr.storeCookies(url.openConnection());
				//spawnNewLocThread();
				System.out.println("Router, LocThread starts");
				firstReq = false;
			}
			cookieMgr.setCookies(new URL(urlStr).openConnection());
	        waiting = true;
	        JSONObject output = new JSONObject(outputStr);
	        System.out.println(output);
	        if (query!=null)
	        {
	        	//only when there is some actual query, set the new destination
	        	destinationPage = output.getString("view_name");
	        	System.out.println("Destination "+destinationPage);
	        }
	        return output;
		//} 
		//return null;
	}
	
	public Class <? extends Page> getDestination()
	{
		return MollyModule.getPageClass(destinationPage);
	}
	
	public void spawnNewLocThread() throws JSONException, UnknownHostException, IOException
	{
		//Router connects to server, then cookieMgr gets the cookies
		//cookieMgr extracts csrftoken, then pass to LocThread
		onRequestSent(MollyModule.HOME_PAGE, null, OutputFormat.JSON, null); //csrftoken can only be received after at least 1 request
		String token = cookieMgr.getCSRFToken();//if connection goes through, csrftoken should be available
		currentLocThread = new LocationThread(new URL(mOX),myApp,token);
		currentLocThread.start();
		System.out.println("New LocThread spawned");
	}
	
	public void stopCurrentLocThread()
	{
		if (currentLocThread != null)
		{
			currentLocThread.stopThread();
			/*currentLocThread is set to null as a measure to avoid the NullPointerException
			that is thrown when the app resumes after the interrupted LocationThread is wiped
			out by Android. 
			*/
			currentLocThread = null;
		}
	}
	
	public void waitForRequests()
	{
		waiting = true;
	}
	
	public LocationThread getLocThread()
	{
		return currentLocThread;
	}
}
