package org.mollyproject.android.view.apps.places.entity;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;
import org.mollyproject.android.view.apps.places.PlacesNearbyDetailTask;

import android.os.Bundle;

public class PlacesEntityNearbyDetailPage extends PageWithMap{
	protected String slug;
	protected String[] args = new String[2];
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		slug = MyApplication.placesNearbySlug;
		args[0] = MyApplication.placesArgs[0];
		args[1] = MyApplication.placesArgs[1];
		name = MollyModule.PLACES_ENTITY_NEARBY_DETAIL;
		additionalArgs = "&arg=" + args[0] + "&arg=" + args[1] + "&arg=" + slug;
		contentLayout.setBackgroundResource(R.drawable.shape_white);
	}
	
	@Override
	public void refresh() {
		new PlacesNearbyDetailTask(this, false, true).execute();
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

}
