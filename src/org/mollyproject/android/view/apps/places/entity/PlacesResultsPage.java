package org.mollyproject.android.view.apps.places.entity;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;
import org.mollyproject.android.view.apps.places.TransportMapPageRefreshTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;


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
		name = MollyModule.PLACES_ENTITY;
		args[0] = MyApplication.placesArgs[0];
		args[1] = MyApplication.placesArgs[1];
		additionalArgs = "&arg=" + args[0] + "&arg=" + args[1];
		mapView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
        		getWindowManager().getDefaultDisplay().getHeight()/3));
	}

	@Override
	public void refresh() {
		if (!args[0].equals(TRANSPORT))
		{
			toggleMapButton.setEnabled(false);
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
		menu.setGroupVisible(R.id.placesGroup, true);
		if (args[0].equals(OXPOINTS))
		{
			menu.findItem(R.id.directions).setEnabled(true);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MyApplication.placesArgs[0] = args[0];
		MyApplication.placesArgs[1] = args[1];
		Intent myIntent;
		switch (item.getItemId())
		{
			case R.id.nearbyPlaces:
				myIntent = new Intent(getApplicationContext(), MyApplication.getPageClass(MollyModule.PLACES_ENTITY_NEARBY_LIST));
				startActivityForResult(myIntent, 0);
				return true;
			case R.id.directions:
				myIntent = new Intent(getApplicationContext(), MyApplication.getPageClass(MollyModule.PLACES_ENTITY_DIRECTIONS));
				startActivityForResult(myIntent, 0);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getQuery() {
		return null;
	}
}