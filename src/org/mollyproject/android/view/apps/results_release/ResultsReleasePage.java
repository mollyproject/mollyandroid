package org.mollyproject.android.view.apps.results_release;

import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class ResultsReleasePage extends ContentPage {
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		new ResultReleaseTask(this, true, true).execute(jsonContent);
	}
	
	public Page getInstance()
	{
		System.out.println("Called "+this);
		return this;
	}

	@Override
	public String getAdditionalParams() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
