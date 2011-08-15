package org.mollyproject.android.controller;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

public class LocationTracker {
	protected MyApplication myApp;
	protected LocationManager locMgr;
	protected String provider;
	public static double DEFAULT_LON = -1.255939;
	public static double DEFAULT_LAT = 51.752527;
	protected Location loc;
	protected boolean autoLoc;
	
	public LocationTracker(MyApplication myApp)
	{
		this.myApp = myApp;
		locMgr = (LocationManager) myApp.getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    criteria.setCostAllowed(true);
	    criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		provider = locMgr.getBestProvider(criteria, true);
		
		loc = new Location(provider);
		loc.setLatitude(DEFAULT_LAT);
		loc.setLongitude(DEFAULT_LON);
		
		autoLoc = true;
	}
	
	public void setLocation(double lat, double lon)
	{
		loc.setLatitude(lat);
		loc.setLongitude(lon);
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
    
    public boolean getAutoLoc()
    {
    	return autoLoc;
    }
	    
	public void setAutoLoc(boolean b)
	{
		autoLoc = b;
	}
	
	public Location getCurrentLoc()
	{
		if (autoLoc)
		{
			locMgr.requestLocationUpdates(provider, 0, 0,locationListener);
			if (locMgr.getLastKnownLocation(provider) != null)
			{
				loc = locMgr.getLastKnownLocation(provider);
			}
		}
		return loc;
	}
}
