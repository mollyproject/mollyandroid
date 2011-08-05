package org.mollyproject.android.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class LocationThread extends Thread {	
	protected CookieManager cookieMgr;
	protected String csrftoken;
	protected URL url;
	protected LocationManager locMgr;
	protected String provider;
	protected Location loc;
	protected Runnable r;
	protected Handler handler;
	protected volatile boolean stop;
	protected volatile boolean autoLoc;
	private static double DEFAULT_LON = -1.255939;
	private static double DEFAULT_LAT = 51.752527;
	private static int UPDATE_DELAY_IN_MS = 600000; 
	
	public LocationThread(final URL url, final MyApplication myApp, String csrftoken)
	{
		super();
		autoLoc = true;
		this.url = url;
		this.csrftoken = csrftoken;
		locMgr = (LocationManager) myApp.getSystemService(Context.LOCATION_SERVICE);
		
	    Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    criteria.setCostAllowed(true);
	    criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		provider = locMgr.getBestProvider(criteria, true);
		System.out.println("LocationThread, Provider: "+ provider);
		
		//first run of the thread, update the position to the default
		loc = new Location(provider);
		loc.setLatitude(DEFAULT_LAT);
		loc.setLongitude(DEFAULT_LON);
		
		stop = false;
		
	}
	
	private final LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	       
	    }
	    public void onProviderDisabled(String provider){
	        
	    }
	    public void onProviderEnabled(String provider){ 
	    	
	    }
	    public void onStatusChanged(String provider, int status,
	        Bundle extras){ 
	    	
	    }
	    };
	
	public void setCSRFToken(String csrftoken)
	{
		this.csrftoken = csrftoken;
	}
	
	//Send an HTTP POST request to server to update the device's location
	@Override
	public void run() {
			Looper.prepare();
			try {
				System.out.println("locthread is about to run");
				while (!stop)
				{	
					try 
					{
						if (autoLoc)
						{
							locMgr.requestLocationUpdates(provider, 0, 0,locationListener);
							if (locMgr.getLastKnownLocation(provider) != null)
							{
								loc = locMgr.getLastKnownLocation(provider);
							}
						}
						// Construct data			
				        String data;
						
						data = URLEncoder.encode("csrfmiddlewaretoken", "UTF-8") 
								+ "=" + URLEncoder.encode(csrftoken, "UTF-8")
							+ "&" + URLEncoder.encode("longtitude", "UTF-8") 
								+ "=" + URLEncoder.encode(
										Double.toString(loc.getLongitude()), "UTF-8")
							+ "&" + URLEncoder.encode("latitude", "UTF-8") 
								+ "=" + URLEncoder.encode(
										Double.toString(loc.getLongitude()), "UTF-8")
							+ "&" + URLEncoder.encode("accuracy", "UTF-8") 
								+ "=" + URLEncoder.encode(
										Float.toString(loc.getAccuracy()), "UTF-8")
							+ "&" + URLEncoder.encode("method", "UTF-8") 
								+ "=" + URLEncoder.encode("html5", "UTF-8") 
							+ "&" + URLEncoder.encode("format", "UTF-8") 
								+ "=" + URLEncoder.encode("json", "UTF-8")
							+ "&" + URLEncoder.encode("force", "UTF-8") 
								+ "=" + URLEncoder.encode("True", "UTF-8");							        
			        
						System.out.println("Long:" + loc.getLongitude() +
													" Lat:"+loc.getLatitude());
				        
						// Send data
				        URLConnection conn = url.openConnection();
				        conn.setDoOutput(true);
				        OutputStreamWriter wr = new OutputStreamWriter
				        								(conn.getOutputStream());
				        wr.write(data);
				        wr.flush();
				 
				        // Get the response
				        BufferedReader rd = new BufferedReader
				        			(new InputStreamReader(conn.getInputStream()));
				        String line;
				        while ((line = rd.readLine()) != null) {
				            System.out.println(line);
				        }
				        //Thread.sleep(2000);
				        wr.close();
				        rd.close();	
					} catch (UnsupportedEncodingException e) {
						/*Page.popupErrorDialog("Unsupported Encoding", 
								"There might be a problem with Location Update" +
								"Latest location is not uploaded to server. Application will retry " +
								"in 10 minutes.", context);*/
					} catch (IOException e) {
						/*Page.popupErrorDialog("I/O Exception (Location)", 
								"There might be a problem with POST Requests from Location Update" +
								"Latest location is not uploaded to server. Application will retry " +
								"in 10 minutes", context);*/
								
					}
					
					sleep(UPDATE_DELAY_IN_MS);
				}
			} catch (InterruptedException e) {
	            System.out.println("Thread interrupted");
	            Thread.currentThread().interrupt();
	        } 
			
			Looper.loop();
			Looper.myLooper().quit();
	}
	
	public void setAutoLocationUpdate(boolean b)
	{
		autoLoc = b;
	}
	
	public void setLocationManually(double lat, double lon)
	{
		loc.setLatitude(lat);
		loc.setLongitude(lon);
	}
	
	public synchronized void stopThread()
	{	
		stop = true;
		interrupt();
	}
}
