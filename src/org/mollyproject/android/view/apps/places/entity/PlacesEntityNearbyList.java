package org.mollyproject.android.view.apps.places.entity;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;
import org.mollyproject.android.view.apps.places.PlacesNearbyTask;

import android.os.Bundle;
import android.widget.LinearLayout;

public class PlacesEntityNearbyList extends PageWithMap {
	protected String[] args = new String[2]; //places args
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.PLACES_ENTITY_NEARBY_LIST;
		args[0] = MyApplication.placesArgs[0];
		args[1] = MyApplication.placesArgs[1];
		additionalArgs = "&arg=" + args[0] + "&arg=" + args[1];
		mapView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
        		getWindowManager().getDefaultDisplay().getHeight()/3));
		hideMap();
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getQuery() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		new PlacesNearbyTask(this, false, true).execute();
	}

}
