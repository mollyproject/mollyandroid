package org.mollyproject.android.view.apps.map;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;


public class PlacesResultsPage extends PageWithMap {
	
	public static TransportMapPageRefreshTask transportMapPageRefreshTask;
	public static boolean firstLoad;
	protected String[] args; //identifiers
	public static final String OXPOINTS = "oxpoints";
	public static final String OSM = "osm";
	public static final String TRANSPORT = "atco";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		firstLoad = true;
		args = MyApplication.placesArgs;
	}
	
	@Override
	public void onResume() {
		//always reload this page for the newest location
		if (!firstLoad) // firstLoad is public static and will be changed in TransportMapTask once the task finishes
		{
			jsonProcessed = false; // to activate the refresh method
			manualRefresh = true;  // to force download of new data
		}
		super.onResume();
	}

	@Override
	public void refresh() {
		if (!args[0].equals(TRANSPORT))
		{
			if (args[0].equals(OXPOINTS))
			{
				mapView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
		        		getWindowManager().getDefaultDisplay().getHeight()/3));
			}
			new PlacesResultsTask(this, true, true).execute();
		}
		else if (args[0].equals(TRANSPORT))
		{
			Toast.makeText(this, "Please wait. This page might take a moment or two to refresh...", 
					Toast.LENGTH_SHORT).show();
			
			if (transportMapPageRefreshTask != null) 
			{
				transportMapPageRefreshTask.cancel(true);
			}
			
			if (firstLoad)
			{
				mapLayout.removeView(mapView);
				toggleMapButton.setChecked(false);
			}
			
			mapView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
	        		getWindowManager().getDefaultDisplay().getHeight()/3));
			
			transportMapPageRefreshTask = new TransportMapPageRefreshTask(this, true, false);
			transportMapPageRefreshTask.execute();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (transportMapPageRefreshTask != null) 
		{
			transportMapPageRefreshTask.cancel(true);
		}
	}

	@Override
	public String getAdditionalParams() {
		//to distinguish between different identifiers (osm, oxpoints, atco)
		//placesArgs[0] = entity.getString("identifier_scheme");
		//placesArgs[1] = entity.getString("identifier_value");
		String argsText = new String();
		for (String arg : args)
		{
			argsText = argsText + "&arg=" + arg;
		}
		return argsText;
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getName() {
		return MollyModule.PLACES_ENTITY;
	}

	@Override
	public String getQuery() {
		return null;
	}
}