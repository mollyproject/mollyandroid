package org.mollyproject.android.view.apps.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchPage extends ContentPage {
	protected String[] generalQuery;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		generalQuery = myApp.getGeneralQuery();
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
		if(!jsonProcessed)
		{
			new SearchTask(this, true, true).execute();
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
		return MollyModule.SEARCH_PAGE;
	}

	@Override
	public String getQuery() throws UnsupportedEncodingException {
		if (generalQuery.length == 1)
		{
			return ("&query=" + URLEncoder.encode(generalQuery[0],"UTF-8"));
		}
		else if (generalQuery.length == 2)
		{
			return ("&query=" + URLEncoder.encode(generalQuery[0],"UTF-8")
				+"&application=" + URLEncoder.encode(generalQuery[1], "UTF-8"));
		}
		else 
		{
			return null;
		}
	}
}
