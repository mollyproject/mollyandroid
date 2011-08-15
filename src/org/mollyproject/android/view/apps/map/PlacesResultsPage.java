package org.mollyproject.android.view.apps.map;

import java.util.List;

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
		return ("&arg=oxpoints"+"&arg="+oxpoints);
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getQuery() {
		return null;
	}
	
}
