package org.mollyproject.android.view.apps.podcasts;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PodcastsPageTask extends JSONProcessingTask
{

	public PodcastsPageTask(ContentPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject outputs) {
		// TODO Auto-generated method stub
		try
			{
			LinearLayout contentLayout = ((ContentPage) page).getContentLayout();
			contentLayout.removeAllViews();
			
			LayoutInflater layoutInflater = page.getLayoutInflater();
			
			LinearLayout podcastsLayout = (LinearLayout) layoutInflater.inflate(R.layout.general_search_results_page, null);
			
			contentLayout.addView(podcastsLayout);
			
			TextView header = (TextView) podcastsLayout.findViewById(R.id.searchResultsHeader);
			header.setText("By division");
			
			LinearLayout resultsLayout = (LinearLayout) podcastsLayout.findViewById(R.id.generalResultsList);
			resultsLayout.setOrientation(LinearLayout.VERTICAL);
			
			JSONArray jsonCategories = outputs.getJSONArray("categories");
			for (int i = 0; i < jsonCategories.length(); i++)
			{
				final JSONObject category = jsonCategories.getJSONObject(i);
				
				LinearLayout thisResult = (LinearLayout) layoutInflater.inflate
						(R.layout.clickable_search_result, null);
				resultsLayout.addView(thisResult);
				thisResult.setLayoutParams(Page.paramsWithLine);
				((TextView) thisResult.findViewById(R.id.clickableResultText)).setText(category.getString("name"));
				final String slug = category.getString("slug");
				System.out.println(slug);
				thisResult.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MyApplication.podcastsSlug = slug;
						Intent myIntent = new Intent(page, MyApplication.getPageClass
								(MollyModule.PODCAST_CATEGORY_PAGE));
						page.startActivityForResult(myIntent, 0);
					}
				});
			}
			((ContentPage) page).doneProcessingJSON();
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
	}

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		if (Page.manualRefresh)
		{
			return super.doInBackground();
		}
		
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ((ContentPage) page).getJSONContent();
	}
}