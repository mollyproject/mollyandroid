package org.mollyproject.android.view.apps.podcasts;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
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
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (!jsonProcessed)
		{
			new PodcastsPageTask(this,true, true).execute();
		}
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
		return MollyModule.PODCAST_PAGE;
	}

	@Override
	public String getQuery() {
		return null;
	}
}
