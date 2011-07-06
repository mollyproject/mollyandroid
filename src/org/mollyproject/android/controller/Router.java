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
	public void onRequestSent(URL requestedURL)
	{
		if (waiting) {
			this.requestedURL = requestedURL;
			waiting = false;
		}
		else System.out.println("Router occupied, try again later");
	}
	
	//Open connection to web server, get back the JSON text (if request
	//goes through), and return the JSONObject if request is processed
	//return null otherwise
	public JSONObject processRequest() throws Exception
	{		
		String s = new String();
		//JSONObject o = new JSONObject();
		
		if (!waiting)
		{
			//send the request to web server, process the response and 
			//forward the response and forward the results to Renderer
	        
	        System.out.println("Processing: " + requestedURL);
	        URLConnection conn = requestedURL.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                                conn.getInputStream()));
	        String inputLine;

	        while ((inputLine = in.readLine()) != null) 
            {
        		s = s.concat(inputLine);
            }
	        in.close();
	        waiting = true;
	        //now s is the JSON text representation of the web page received	        
	        return new JSONObject(s);
		}
		else System.out.println("No request received.");
		
		return null;
	}
}
