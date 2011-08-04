package org.mollyproject.android.view.apps;

import android.os.Bundle;

public abstract class ResultsDisplayPage extends ContentPage {

	protected String query;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	public String getQuery()
	{
		return query;
	}
}
