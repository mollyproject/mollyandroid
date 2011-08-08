package org.mollyproject.android.view.apps.search;

import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
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
    	final EditText searchField = (EditText) findViewById(R.id.searchField);
    	setEnterKeySearch(searchField, this);
		contentLayout.setBackgroundColor(R.color.white);
		new SearchTask(this,true).execute("query="+myApp.getGeneralQuery());
	}
	@Override
	public Page getInstance() {
		return this;
	}

}
