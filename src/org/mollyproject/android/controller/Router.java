package org.mollyproject.android.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;
import org.mollyproject.android.CookieManager;
import org.mollyproject.android.LocationThread;
import org.mollyproject.android.view.Renderer;

public class Router implements RequestsListener {
	protected CookieManager cookieMgr;
	protected static boolean waiting;	
	protected Renderer ren;
	protected LocationThread locThread;
	protected boolean firstReq;
	
	public Router (Renderer ren)
	{
		waiting = true;	
		this.ren = ren;
		cookieMgr = new CookieManager();
		firstReq = true;
		locThread= new LocationThread();
	}
	
	//Take an URL String, convert to URL, open connection then process 
	//and return the response
	private String getFrom (String urlStr) throws Exception
	{
		String outputStr = new String();		
		
		System.out.println("Processing: " + urlStr);
		URLConnection revConn = new URL(urlStr).openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
								revConn.getInputStream()));
		String inputLine;
		
		while ((inputLine = in.readLine()) != null) 
		{
			outputStr = outputStr.concat(inputLine);
		}
		in.close();
		System.out.println("Output: " + outputStr);
		return outputStr;
	}
	
	public void onRequestSent(String locator) throws Exception {
		//Geting the actual URL from the server using the locator (view name)
		//and the reverse API in Molly
		if (waiting) {
			waiting = false;
			
			String urlStr = new String();
			String reverseReq = "http://dev.m.ox.ac.uk/reverse/?name="+locator;
			urlStr = getFrom(reverseReq);
			
			//Have the urlStr, now get the JSON text
			String jsonText = new String();
			jsonText = getFrom(urlStr+"?format=json");
			
			cookieMgr.storeCookies(new URL(urlStr).openConnection());
			locThread.setCSRFToken(cookieMgr.getCSRFToken(new URL(urlStr)));
			if (firstReq)
			{ 
				locThread.start();
				firstReq = false;		
			}

	        waiting = true;
	        ren.render(new JSONObject(jsonText));
	        //return new JSONObject(jsonText);
		}
	}
	
	public CookieManager getCookieManager ()
	{
		return cookieMgr;
	}
}
