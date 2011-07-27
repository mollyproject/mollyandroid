package org.mollyproject.android.view.apps.library;

import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class LibraryResultsPage extends ContentPage {

	public void oncreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		String query = myApp.getLibraryQuery();
		//now at page1
		System.out.println(query);
	}
	
	private void generatePage(int pageNumber)
	{
		
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

}
