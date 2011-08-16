package org.mollyproject.android.view.apps.podcasts;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
		try
			{
			LinearLayout podcastsLayout = (LinearLayout) layoutInflater.inflate(R.layout.general_search_results_page, 
					contentLayout, false);
			contentLayout.addView(podcastsLayout);
			
			TextView header = (TextView) podcastsLayout.findViewById(R.id.searchResultsHeader);
			header.setText("By division");
			
			LinearLayout resultsLayout = (LinearLayout) podcastsLayout.findViewById(R.id.generalResultsList);
			resultsLayout.setOrientation(LinearLayout.VERTICAL);
			
			JSONArray jsonCategories = jsonContent.getJSONArray("categories");
			for (int i = 0; i < jsonCategories.length(); i++)
			{
				//final Map<String,String> resultMap = resultMapsList.get(i);
				final JSONObject category = jsonCategories.getJSONObject(i);
				
				LinearLayout thisResult = (LinearLayout) layoutInflater.inflate
						(R.layout.plain_text_search_result, contentLayout,false);
				resultsLayout.addView(thisResult);
				thisResult.setLayoutParams(Page.paramsWithLine);
				((TextView) thisResult.getChildAt(0)).setText(category.getString("name"));
				final String slug = category.getString("slug");
				thisResult.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						myApp.setPodcastsSlug(slug);
						Intent myIntent = new Intent(PodcastsPage.this, myApp.getPageClass
								(MollyModule.PODCAST_CATEGORY_PAGE));
						startActivityForResult(myIntent, 0);
					}
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		//new PodcastsPageTask(this,true, true).execute(jsonContent);
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
