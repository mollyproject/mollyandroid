package org.mollyproject.android.view.apps.library;

import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class LibraryResultsPage extends ContentPage {

	public void oncreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		String jsonOutput = myApp.getLibraryOutput();
		System.out.println(jsonOutput);
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

}
