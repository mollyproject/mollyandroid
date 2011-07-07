package org.mollyproject.android.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;
import org.mollyproject.android.view.Renderer;

public class Router implements RequestsListener {

	protected static boolean waiting;	
	protected Renderer ren;
	
	public Router (Renderer ren)
	{
		waiting = true;	
		this.ren = ren;
		ren.addRequestsListener(this);
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
	
	public JSONObject onRequestSent(String locator) throws Exception {
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
	        waiting = true;
	        return new JSONObject(jsonText);
		}
		else 
		{
			return null;
		}        		
	}
}
