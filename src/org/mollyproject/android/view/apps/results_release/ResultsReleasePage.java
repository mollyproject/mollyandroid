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
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (!jsonProcessed)
		{
			new ResultReleaseTask(this, true, true).execute(jsonContent);
		}
	}
	
	public Page getInstance()
	{
		System.out.println("Called "+this);
		return this;
	}

	@Override
	public String getAdditionalParams() {
		return null;
	}

	@Override
	public String getName() {
		return MollyModule.RESULTS_PAGE;
	}

	@Override
	public String getQuery() {
		return null;
	}
	

}
