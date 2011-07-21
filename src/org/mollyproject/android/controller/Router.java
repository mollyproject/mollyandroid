package org.mollyproject.android.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.mollyproject.android.LocationThread;
import org.mollyproject.android.view.Renderer;

import android.content.Context;

public class Router implements RequestsListener {
	protected CookieManager cookieMgr;
	protected static boolean waiting;	
	protected Renderer ren;
	protected LocationThread locThread;
	protected boolean firstReq;
	protected Context context;
	public final static String mOX =  "http://dev.m.ox.ac.uk/";
	
	public Router (Context context) throws Exception
	{
		waiting = true;	
		cookieMgr = new CookieManager(context);
		firstReq = true;
		this.context = context;
		locThread= new LocationThread(new URL(mOX),context);
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
	
	public String onRequestSent(String locator) throws Exception {
		//Geting the actual URL from the server using the locator (view name)
		//and the reverse API in Molly
		if (waiting) {
			waiting = false;
			
			String urlStr = new String();
			String reverseReq = mOX + "reverse/?name="+locator;
			urlStr = getFrom(reverseReq);
			
			//Have the urlStr, now get the JSON text
			String jsonText = new String();
			jsonText = getFrom(urlStr+"?format=json");
			System.out.println("First Request "+firstReq);
			if (firstReq)
			{ 
				cookieMgr.storeCookies(new URL(urlStr).openConnection());
				locThread.setCSRFToken(cookieMgr.getCSRFToken(new URL(urlStr)));
				locThread.start();
				System.out.println("Router, LocThread starts");
				firstReq = false;
			}
			cookieMgr.setCookies(new URL(urlStr).openConnection());
	        waiting = true;
	        //ren.render(new JSONObject(jsonText));
	        return jsonText;
		}
		return null;
	}
	
	public CookieManager getCookieManager ()
	{
		return cookieMgr;
	}
	
	public LocationThread getLocThread()
	{
		return locThread;
	}
}
