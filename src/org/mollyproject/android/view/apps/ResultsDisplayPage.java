package org.mollyproject.android.view.apps;

import android.os.Bundle;

public abstract class ResultsDisplayPage extends ContentPage {

	protected String query;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//this results display page is passed the query from myApp
		if (!myApp.isOnline())
		{
			Page.popupErrorDialog("Network Connection failed", 
					"No connection detected. Please try again later.", this,true);
		}
		query = myApp.getLibraryQuery();
	}
}
