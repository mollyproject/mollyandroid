package org.mollyproject.android.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

public class Router {
	protected CookieManager cookieMgr;
	protected static boolean waiting;	
	protected LocationThread currentLocThread;
	protected String csrfToken;
	protected boolean firstReq;
	protected Context context;
	public final static String mOX =  "http://dev.m.ox.ac.uk/";
	public final static int JSON = 1;
	
	public Router (Context context) throws Exception
	{
		waiting = true;	
		cookieMgr = new CookieManager(context);
		firstReq = true;
		this.context = context;
	}
	
	//Take an URL String, convert to URL, open connection then process 
	//and return the response
	public static String getFrom (String urlStr) throws Exception
	{
		String outputStr = new String();		
		
		System.out.println("Router, Processing: " + urlStr);
		URLConnection revConn = new URL(urlStr).openConnection();
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
	
	public String onRequestSent(String locator,int format, String query) throws Exception {
		//Geting the actual URL from the server using the locator (view name)
		//and the reverse API in Molly
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
	}
	
	public LocationThread getLocThread()
	{
		return currentLocThread;
	}
}
