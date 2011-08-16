package org.mollyproject.android.view.apps.map;

import java.util.List;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class PlacesResultsPage extends ContentPage {
	protected String oxpoints;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		new PlacesResultsTask(this, true, true).execute(jsonContent);
	}
	
	@Override
	public String getAdditionalParams() {
		String[] args = myApp.getPlacesArgs();
		String argsText = new String();
		for (String arg : args)
		{
			argsText = argsText + "&arg" + arg;
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
