package org.mollyproject.android.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONException;
import org.mollyproject.android.view.apps.Page;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Router {
	protected CookieManager cookieMgr;
	protected static boolean waiting;	
	protected LocationThread currentLocThread;
	protected String csrfToken;
	protected boolean firstReq;
	protected Context context;
	public final static String mOX =  "http://dev.m.ox.ac.uk/";
	public final static int JSON = 1;
	
	//The exception thrown by this class is solely because of problems with the
	//network part
	public Router (Context context) throws IOException, JSONException 
	{
		waiting = true;	
		cookieMgr = new CookieManager(context);
		firstReq = true;
		this.context = context;
	}
	
	//Take an URL String, convert to URL, open connection then process 
	//and return the response

	public static String getFrom (String urlStr) throws MalformedURLException, IOException
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
	
	public String onRequestSent(String locator,int format, String query) {
		//Geting the actual URL from the server using the locator (view name)
		//and the reverse API in Molly
		try
		{
		if (waiting) {
			waiting = false;
			
			String urlStr = new String();
			String reverseReq = mOX + "reverse/?name="+locator;
			urlStr = getFrom(reverseReq);
			String output = new String();
			
			//Have the urlStr, now get the JSON text or query
			
			switch(format){
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
				spawnNewLocThread(cookieMgr.getCSRFToken(url));
				System.out.println("Router, LocThread starts");
				firstReq = false;
			}
			cookieMgr.setCookies(new URL(urlStr).openConnection());
	        waiting = true;
	        //ren.render(new JSONObject(jsonText));
	        return output;
		}
		
		} 
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			AlertDialog dialog = Page.popupErrorDialog("Malformed URL (Router)",
					"Please try restarting the app", context);
			dialog.setButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			
		} catch (IOException e)
		{
			e.printStackTrace();
			AlertDialog dialog = Page.popupErrorDialog("I/O Exception (Router)", 
					"There might be a problem with cookie files. " +
					"Please try restarting the app", context);
			dialog.setButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		}catch (JSONException e)
		{
			e.printStackTrace();
			AlertDialog dialog = Page.popupErrorDialog("JSON Exception (Router)", 
					"There might be a problem with JSON output " +
					"from server. Please try restarting the app", context);
			dialog.setButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		}
		return null;
		
	}
	
	public CookieManager getCookieManager ()
	{
		return cookieMgr;
	}
	
	public void spawnNewLocThread(String token) throws MalformedURLException
	{
		System.out.println("New LocThread spawned");
		currentLocThread = new LocationThread(new URL(mOX),context);
		currentLocThread.setCSRFToken(token);
		currentLocThread.start();
	}
	
	public void stopCurrentLocThread()
	{
		currentLocThread.stopThread();
		/*currentLocThread is set to null as a measure to avoid the NullPointerException
		that is thrown when the app resumes after the interrupted LocationThread is wiped
		out by Android. 
		*/
		currentLocThread = null; 
	}
	
	public LocationThread getLocThread()
	{
		return currentLocThread;
	}
}
