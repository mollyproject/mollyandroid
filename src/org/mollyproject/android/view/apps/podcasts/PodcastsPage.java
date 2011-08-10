package org.mollyproject.android.view.apps.podcasts;

import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class PodcastsPage extends ContentPage {
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		RelativeLayout searchBar = (RelativeLayout) 
		layoutInflater.inflate(R.layout.search_bar,contentLayout, false);
		contentLayout.addView(searchBar);
		
		EditText searchField = (EditText) findViewById(R.id.searchField);
		searchField.setWidth(LayoutParams.FILL_PARENT);
		setEnterKeySearch(searchField, this, "podcasts");
		System.out.println("jsonContent "+jsonContent);
		new PodcastsPageTask(this,true, true).execute(jsonContent);
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
