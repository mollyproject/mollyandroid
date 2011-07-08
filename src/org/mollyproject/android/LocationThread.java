package org.mollyproject.android;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.location.Location;

public class LocationThread extends Thread {	
	protected CookieManager cookieMgr;
	protected String csrftoken;
	protected URL url;
	protected Location loc;
	
	public LocationThread()
	{
		super();
		//this.cookieMgr = cookieMgr;
	}
	
	public void setCSRFToken(String csrftoken)
	{
		this.csrftoken = csrftoken;
	}
	
	public void run() {
		try {
	        // Construct data
	        String data = URLEncoder.encode("csrfmiddlewaretoken", "UTF-8") 
	        					+ "=" + URLEncoder.encode(csrftoken, "UTF-8")
	        				+ "&" + URLEncoder.encode("longtitude", "UTF-8") 
	        					+ "=" + URLEncoder.encode(Double.toString(loc.getLongitude())
	        																		, "UTF-8")
	        				+ "&" + URLEncoder.encode("latitude", "UTF-8") 
        						+ "=" + URLEncoder.encode(Double.toString(loc.getLatitude())
        																			, "UTF-8")
	        				+ "&" + URLEncoder.encode("accuracy", "UTF-8") 
	        					+ "=" + URLEncoder.encode(Float.toString(loc.getAccuracy())
	        																	, "UTF-8")
	        				+ "&" + URLEncoder.encode("method", "UTF-8") 
	        					+ "=" + URLEncoder.encode("html5", "UTF-8") 
	        				+ "&" + URLEncoder.encode("format", "UTF-8") 
	        					+ "=" + URLEncoder.encode("json", "UTF-8")
	        				+ "&" + URLEncoder.encode("force", "UTF-8") 
	        					+ "=" + URLEncoder.encode("True", "UTF-8");
	        System.out.println("Here");
	        // Send data	        
	        URLConnection conn = url.openConnection();
	        conn.setDoOutput(true);
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	        wr.write(data);
	        wr.flush();
	 
	        // Get the response
	        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String line;
	        while ((line = rd.readLine()) != null) {
	            System.out.println(line);
	        }
	        wr.close();
	        rd.close();
	    } catch (Exception e) {
	    	System.out.println("Exception");
	    }
	}
}
