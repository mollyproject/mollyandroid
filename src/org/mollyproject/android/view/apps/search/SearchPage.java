package org.mollyproject.android.view.apps.search;

import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class SearchPage extends ContentPage {
	
	@SuppressWarnings("unchecked")
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
		new SearchResultsTask(this,true, true).execute(myApp.getGeneralOutput());
	}
	@Override
	public Page getInstance() {
		return this;
	}
	@Override
	public String getAdditionalParams() {
		// TODO Auto-generated method stub
		return null;
	}

}
