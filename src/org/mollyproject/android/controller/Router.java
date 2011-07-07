package org.mollyproject.android.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;
import org.mollyproject.android.view.Renderer;

public class Router implements RequestsListener {

	protected static boolean waiting;
	protected URL requestedURL;
	protected Renderer ren;
	
	public Router (Renderer ren)
	{
		//requestedURL is null at this point, but it is protected by waiting		
		waiting = true;	
		this.ren = ren;
		ren.addRequestsListener(this);
	}
	
	public JSONObject onRequestSent(String locator) throws Exception {
		//Geting the actual URL from the server using the locator (view name)
		//and the reverse API in Molly
		if (waiting) {
			waiting = false;
			
			String urlStr = new String();
			String reverseReq = "http://dev.m.ox.ac.uk/reverse/?name="+locator;
			System.out.println("Processing: " + locator);
			URLConnection revConn = new URL(reverseReq).openConnection();
			BufferedReader inRev = new BufferedReader(new InputStreamReader(
									revConn.getInputStream()));
			String inputRevLine;
			
			while ((inputRevLine = inRev.readLine()) != null) 
			{
				urlStr = urlStr.concat(inputRevLine);
			}
			urlStr += "?format=json";
			inRev.close();
			
			//Have the urlStr, now get the JSON text
			String jsonText = new String();			
			System.out.println("Processing: " + urlStr);
	        URLConnection conn = new URL(urlStr).openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                                conn.getInputStream()));
	        String inputLine;
	
	        while ((inputLine = in.readLine()) != null) 
	        {
	    		jsonText = jsonText.concat(inputLine);
	        }
	        in.close();
	        System.out.println(jsonText); //print out json text for testing
	        waiting = true;
	        return new JSONObject(jsonText);
		}
		else 
		{
			return null;
		}        		
	}
}
