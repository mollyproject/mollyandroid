package org.mollyproject.android.view.apps.places;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
import android.widget.TextView;

public class PlacesNearbyPage extends ContentPage {
	protected TextView currentLocation;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.PLACES_NEARBY;
		currentLocation = new TextView(getApplicationContext());
		contentLayout.addView(currentLocation);
	}
	
	@Override
	public void refresh() {
		try {
			currentLocation.setText("Your last updated location: " + MyApplication.currentLocation.getString("name"));
			new PlacesNearbyTask(this, false, true).execute(jsonContent);
		} catch (Exception e) {
			//this includes null pointer and json exceptions
			e.printStackTrace();
			currentLocation.setText("Cannot get a fix on your location. " +
					"Please check your GPS/network settings or set the location manually");
		}
	}
	
	@Override
	public String getQuery() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page getInstance() {
		return this;
	}
}
