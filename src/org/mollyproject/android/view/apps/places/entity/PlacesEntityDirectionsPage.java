package org.mollyproject.android.view.apps.places.entity;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;

import android.os.Bundle;

public class PlacesEntityDirectionsPage extends PageWithMap{
	protected String[] args = new String[2];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.PLACES_ENTITY_DIRECTIONS;
		args[0] = MyApplication.placesArgs[0];
		args[1] = MyApplication.placesArgs[1];
		additionalArgs = "&arg=" + args[0] + "&arg=" + args[1];
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
		new PlacesEntityDirectionsTask(this, false, true).execute();
	}

}
