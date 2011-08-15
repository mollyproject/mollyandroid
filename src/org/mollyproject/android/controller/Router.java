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

public class Router {
	protected CookieManager cookieMgr;
	//protected LocationThread currentLocThread;
	protected LocationTracker locTracker;
	protected String csrfToken;
	protected boolean firstReq;
	protected MyApplication myApp;
	public final static String mOX =  "http://dev.m.ox.ac.uk/";

	public static enum OutputFormat { JSON, FRAGMENT, JS, YAML, XML, HTML };

	public Router (MyApplication myApp) throws IOException, JSONException 
	{
		this.myApp = myApp;
		cookieMgr = new CookieManager(myApp);
		firstReq = true;
		locTracker = new LocationTracker(myApp);
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
		/*basic method for all requests for json response, it sets up a url to be sent
		  to the server as follow:
		  1. it looks up the url to the required page using the reverse api with either the
		  view_name only or both the view_name and the extra argument (arg)
		  2. then it returns the url, and open a connection to that one itself
		  3. get the response
		  For now it seems quite pointless in including the switch statement and the 
		  format parameters, but I will change this in the future if some other formats are needed,
		  for now JSON is the sole choice and the method only needs to return JSON
		  
		  this method is also to be included in AsyncTask subclasses where no UI is allowed
		  in the doInBackground method
		*/
		
		//Geting the actual URL from the server using the locator (view name)
		//and the reverse API in Molly
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
		
		URL url = new URL(urlStr);
		URLConnection urlConn = url.openConnection();
		if (firstReq)
		{
			//try storing cookies if this is the first request
			cookieMgr.storeCookies(urlConn);
			firstReq = false;
		}
		//set cookie for connection
		cookieMgr.setCookies(urlConn);
		cookieMgr.setLocation(urlConn, locTracker.getCurrentLoc().getLatitude(), 
					locTracker.getCurrentLoc().getLongitude(), locTracker.getCurrentLoc().getAccuracy());
        return new JSONObject(outputStr);
	}
	
	/*public void stopCurrentLocThread()
	{
		if (currentLocThread != null)
		{
			currentLocThread.stopThread();
			/*currentLocThread is set to null as a measure to avoid the NullPointerException
			that is thrown when the app resumes after the interrupted LocationThread is wiped
			out by Android. 
			
			currentLocThread = null;
		}
	}
	
	public LocationThread getLocThread()
	{
		return currentLocThread;
	}*/
}
