package org.mollyproject.android.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.mollyproject.android.selection.SelectionManager;
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
		currentLocThread = null;
	}
    
	public void setApp(MyApplication myApp)
    {
    	this.myApp = myApp;
    }
    
	//Take an URL String, convert to URL, open connection then process 
	//and return the response
	public static String getFrom (String urlStr) throws MalformedURLException,
											IOException, UnknownHostException
	{
		String outputStr = new String();		
		
		System.out.println("Router, Processing: " + urlStr);
		URLConnection revConn = new URL(urlStr).openConnection();
		revConn.setConnectTimeout(5000);
		revConn.setReadTimeout(5000);
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
	
	public String exceptionHandledOnRequestSent(String locator,Page page, OutputFormat format, String query)
	{
		try{
			return onRequestSent(locator, format, query);
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
		} catch (IOException e)
		{
			e.printStackTrace();
			Page.popupErrorDialog("I/O Exception (Router)", 
					"There might be a problem with cookie files. " +
					"Please try later.", page);
		} finally
		{
			waitForRequests();
		}
		return null;
	}
	
	public String onRequestSent(String locator,OutputFormat format, String query) 
							throws JSONException, UnknownHostException, IOException {
		//to be included in AsyncTask subclasses where no UI is allowed
		//in the doInBackground method
		
		//Geting the actual URL from the server using the locator (view name)
		//and the reverse API in Molly
		if (waiting) {
			waiting = false;
			//if an exception is thrown, waiting might be false forever
			String urlStr = new String();
			String reverseReq = mOX + "reverse/?name="+locator;
			urlStr = getFrom(reverseReq);
			String output = new String();
			
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
			
			output = getFrom(urlStr);
			
			System.out.println("First Request "+firstReq);
			if (firstReq)
			{ 
				URL url = new URL(urlStr);
				cookieMgr.storeCookies(url.openConnection());
				System.out.println("CSRF: "+cookieMgr.getCSRFToken());
				spawnNewLocThread();
				System.out.println("Router, LocThread starts");
				firstReq = false;
			}
			cookieMgr.setCookies(new URL(urlStr).openConnection());
	        waiting = true;
	        //ren.render(new JSONObject(jsonText));
	        return output;
		} 
		
		return null;
		
	}
	
	public CookieManager getCookieManager ()
	{
		return cookieMgr;
	}
	
	public void spawnNewLocThread() throws JSONException, UnknownHostException, IOException
	{
		//Router connects to server, then cookieMgr gets the cookies
		//cookieMgr extracts csrftoken, then pass to LocThread
		onRequestSent(SelectionManager.HOME_PAGE, OutputFormat.JSON, null); //csrftoken can only be received after at least 1 request
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
