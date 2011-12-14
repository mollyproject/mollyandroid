package org.mollyproject.android.view.apps.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class SearchPage extends ContentPage {
	protected String[] generalQuery;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		name = MollyModule.SEARCH_PAGE;
		this.generalQuery = MyApplication.generalQuery;
		RelativeLayout searchBar = (RelativeLayout) 
				getLayoutInflater().inflate(R.layout.search_bar,null);
    	contentLayout.addView(searchBar);
    	
    	EditText searchField = (EditText) findViewById(R.id.searchField);
    	searchField.setWidth(LayoutParams.FILL_PARENT);
    	setEnterKeySearch(searchField, this, null);
    	
    	Button searchButton = (Button) findViewById(R.id.searchButton);
    	setClickSearch(searchButton, searchField, this, null);
	}
	
	@Override
	public void refresh() {
		new SearchTask(this, false, true).execute();
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getQuery() throws UnsupportedEncodingException {
		if (generalQuery != null)
		{
			if (generalQuery.length == 1)
			{
				return ("&query=" + URLEncoder.encode(generalQuery[0],"UTF-8"));
			}
			else if (generalQuery.length == 2)
			{
				return ("&query=" + URLEncoder.encode(generalQuery[0],"UTF-8")
					+"&application=" + URLEncoder.encode(generalQuery[1], "UTF-8"));
			}
		}
		return null;
	}
}
