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
	public String getAdditionalParams() {
		String[] args = myApp.getPlacesArgs();
		return ("&arg="+args[0]+"&arg="+args[1]);
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
