package org.mollyproject.android.view.apps.results_release;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class ResultsReleasePage extends ContentPage {
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		name = MollyModule.RESULTS_PAGE;
	}
	
	@Override
	public void refresh() {
		new ResultReleaseTask(this, false, true).execute();
	}
	
	public Page getInstance()
	{
		return this;
	}

	@Override
	public String getQuery() {
		return null;
	}
	

}
