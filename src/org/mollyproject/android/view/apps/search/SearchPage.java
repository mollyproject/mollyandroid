package org.mollyproject.android.view.apps.search;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class SearchPage extends ContentPage {
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		RelativeLayout searchBar = (RelativeLayout) 
				layoutInflater.inflate(R.layout.search_bar,contentLayout, false);
    	contentLayout.addView(searchBar);
    	
    	EditText searchField = (EditText) findViewById(R.id.searchField);
    	searchField.setWidth(LayoutParams.FILL_PARENT);
    	setEnterKeySearch(searchField, this, null);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		new SearchTask(this, true, true).execute(myApp.getGeneralQuery());
	}
	@Override
	public Page getInstance() {
		return this;
	}
	@Override
	public String getAdditionalParams() {
		return null;
	}
	@Override
	public String getName() {
		return MollyModule.SEARCH_PAGE;
	}

}
