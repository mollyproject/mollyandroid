package org.mollyproject.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.mollyproject.android.controller.CookieManager;

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
	
	
	public LocationThread(final URL url, Context context)
	{
		super();
		this.url = url;
		locMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
	    Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    criteria.setCostAllowed(true);
	    criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		provider = locMgr.getBestProvider(criteria, true);
		System.out.println("LocationThread, Provider: "+ provider);
		//this.cookieMgr = cookieMgr;
		stop = false;
		handler = new Handler();
		r = new Runnable() {
			public void run()
			{
				try {
					locMgr.requestLocationUpdates(provider, 0, 0,locationListener);
					if (locMgr.getLastKnownLocation(provider) != null)
					{
						
						loc = locMgr.getLastKnownLocation(provider);
				        // Construct data			
				        String data;
						
							data = URLEncoder.encode("csrfmiddlewaretoken", "UTF-8") 
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
				        System.out.println("Location Thread, Here");
				        System.out.println("Long:" + loc.getLongitude() +" Lat:"+loc.getLatitude());
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
				        //Thread.sleep(2000);
				        wr.close();
				        rd.close();	
					}
					} catch (UnsupportedEncodingException e) {
						System.out.println("Unsupported Encoding Exception");
					} catch (IOException e) {
						System.out.println("I/O Exception");
					}
			}
		};				
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
			System.out.println("Location Thread, Provider: " + provider);
			try {
				while (!stop)
				{	
					sleep(5000);
					handler.post(r);
				}
			} catch (InterruptedException e) {
	            System.out.println("Thread interrupted");
	            Thread.currentThread().interrupt();
	        } 
			
			Looper.loop();
			Looper.myLooper().quit();
	}
	
	public synchronized void stopThread()
	{
		stop = true;
	}
}
