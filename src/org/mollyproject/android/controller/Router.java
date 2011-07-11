package org.mollyproject.android.controller;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;
import org.mollyproject.android.CookieManager;
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
	private final static String COOKIESFILE = "cookiesFile.txt";
	
	public Router (Renderer ren, Context context) throws MalformedURLException
	{
		waiting = true;	
		this.ren = ren;
		cookieMgr = new CookieManager();
		firstReq = true;
		this.context = context;
		locThread= new LocationThread(new URL(mOX),context);
	}
	
	//Take an URL String, convert to URL, open connection then process 
	//and return the response
	private String getFrom (String urlStr) throws Exception
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
	
	public void onRequestSent(String locator) throws Exception {
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
			
			//store cookies, write to file
			cookieMgr.storeCookies(new URL(urlStr).openConnection());
			FileOutputStream fos = context.openFileOutput(COOKIESFILE, Context.MODE_WORLD_READABLE);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			osw.write(cookieMgr.getJSONCookieStore().toString());
			osw.flush();
            osw.close();
            
			locThread.setCSRFToken(cookieMgr.getCSRFToken(new URL(urlStr)));
			
			if (firstReq)
			{ 
				locThread.start();
				firstReq = false;
			}
			
	        waiting = true;
	        ren.render(new JSONObject(jsonText));
	        
	        System.out.println("JSON Cookie: "+ cookieMgr.getJSONCookieStore().toString());
		}
	}
	
	public CookieManager getCookieManager ()
	{
		return cookieMgr;
	}
}
