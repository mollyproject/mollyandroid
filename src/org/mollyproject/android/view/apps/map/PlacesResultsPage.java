package org.mollyproject.android.view.apps.map;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;


public class PlacesResultsPage extends PageWithMap {
	
	public static TransportMapPageRefreshTask transportMapPageRefreshTask;
	public static boolean firstLoad;
	protected String[] args = new String[2]; //identifiers
	public static final String OXPOINTS = "oxpoints";
	public static final String OSM = "osm";
	public static final String TRANSPORT = "atco";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		firstLoad = true;
		args[0] = MyApplication.placesArgs[0];
		args[1] = MyApplication.placesArgs[1];
	}
	
	@Override
	public void onResume() {
		//always reload this page for the newest location
		//firstLoad is public static and will be changed in TransportMapTask once the task finishes
		//this means the reset only actually runs every time the page is reloaded by user input when using
		//PlacesResultTask, no need to do this in TransportMapTask because it reloads the page automatically
		if (!firstLoad) 
		{
			//force reset of the whole page (not simply manual refresh)
			loaded = false;
			jsonProcessed = false;
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
			System.out.println("HERE");
			new PlacesResultsTask(this, false, true).execute();
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
			
			transportMapPageRefreshTask = new TransportMapPageRefreshTask(this, false, false);
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
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.nearbyPlaces).setVisible(true);
		menu.findItem(R.id.directions).setVisible(true);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
			case R.id.nearbyPlaces:
				MyApplication.placesArgs[0] = args[0];
				MyApplication.placesArgs[1] = args[1];
				Intent myIntent =  new Intent(getApplicationContext(), MyApplication.getPageClass(MollyModule.PLACES_ENTITY_NEARBY_LIST));
				startActivityForResult(myIntent, 0);
				return true;
			default:
				return super.onOptionsItemSelected(item);
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