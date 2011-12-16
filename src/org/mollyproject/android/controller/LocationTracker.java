package org.mollyproject.android.controller;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * class responsible for keeping track and updating of the current location of the device
 * 
 * @author famanson
 *
 */
public class LocationTracker {
	/**
	 * a reference to the application state
	 */
	protected MyApplication myApp;
	/**
	 * a LocationManager object, used to communicate with the location service
	 */
	protected LocationManager locMgr;
	/**
	 * holds the location provider (e.g. GPS, network...)
	 */
	protected String provider;
	/**
	 * the default longitude, guess where this is
	 */
	public static double DEFAULT_LON = -1.255939;
	/**
	 * the default latitude, guess where this is
	 */
	public static double DEFAULT_LAT = 51.752527;
	/**
	 * the (last updated) location
	 */
	protected Location loc;
	/**
	 * specifies whether the location should be automatically updated or it is updated manually (in which case automatic update should be disabled)
	 */
	public static boolean autoLoc;
	/**
	 * the class constructor sets the criteria when the location service should be run and initiates the required parameters
	 * @param myApp the application state
	 */
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
	
	/**
	 * set the current location to a given pair of (lat, lon)
	 * @param lat the given latitude
	 * @param lon the given longitude
	 */
	public void setLocation(double lat, double lon)
	{
		loc.setLatitude(lat);
		loc.setLongitude(lon);
	}
	
	/**
	 * a location listener
	 */
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
    
    /**
     * checks if automatic location update is in use
     * @return if autoLoc is true
     */
    public boolean isAutoLoc()
    {
    	return autoLoc;
    }
	/**
	 * enable/disable the automatic location update
	 * @param b a boolean value (true/false)
	 */
	public void setAutoLoc(boolean b)
	{
		autoLoc = b;
	}
	
	/**
	 * request a location update from the LocationManager
	 */
	public void startLocUpdate()
	{
		locMgr.requestLocationUpdates(provider, 0, 0,locationListener);
	}
	
	/**
	 * query the current (actually last updated) location
	 * @return the last known location
	 */
	public Location getCurrentLoc()
	{
		if (autoLoc)
		{
			//locMgr.requestLocationUpdates(provider, 0, 0,locationListener);
			if (locMgr.getLastKnownLocation(provider) != null)
			{
				loc = locMgr.getLastKnownLocation(provider);
			}
		}
		return loc;
	}
}
