package org.mollyproject.android.view.pages;

import android.os.Bundle;

public abstract class ContentPage extends Page {
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		myApp.removeBreadCrumb();
		bcBar.removeBreadCrumb();
	}

}
