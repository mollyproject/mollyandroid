package org.mollyproject.android.view.apps.map;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;
import android.os.Bundle;


public class PlacesResultsPage extends PageWithMap {
	
	protected String[] args;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		args = MyApplication.placesArgs;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!jsonProcessed)
		{
			new PlacesResultsTask(this, true, true).execute();
		}
	}

	@Override
	public String getAdditionalParams() {
		//to distinguish between osm and oxpoints
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