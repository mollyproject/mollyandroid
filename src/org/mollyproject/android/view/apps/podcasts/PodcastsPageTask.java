package org.mollyproject.android.view.apps.podcasts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PodcastsPageTask extends BackgroundTask<JSONObject, Void, List<Map<String,String>>>{
	
	public PodcastsPageTask(PodcastsPage podcastsPage, boolean toDestroy, boolean dialog)
	{
		super(podcastsPage, toDestroy, dialog);
		
	}
	
	@Override
	public void updateView(List<Map<String,String>> resultMapsList) {
		//Exactly the same arrangements and layouts as Search Results 
		//only with podcasts_search_result
		LayoutInflater inflater = page.getLayoutInflater();
		LinearLayout podcastsLayout = (LinearLayout) inflater.inflate(R.layout.general_search_results_page, 
				((PodcastsPage) page).getContentLayout(), false);
		((PodcastsPage) page).getContentLayout().addView(podcastsLayout);
		
		TextView header = (TextView) page.findViewById(R.id.searchResultsHeader);
		header.setText("By division");
		
		LinearLayout resultsLayout = (LinearLayout) page.findViewById(R.id.generalResultsList);
		
		resultsLayout.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < resultMapsList.size(); i++)
		{
			final Map<String,String> resultMap = resultMapsList.get(i);
			LinearLayout thisResult = (LinearLayout) inflater.inflate
					(R.layout.podcasts_search_result, ((PodcastsPage) page).getContentLayout(),false);
			resultsLayout.addView(thisResult);
			thisResult.setLayoutParams(Page.paramsWithLine);
			((TextView) thisResult.getChildAt(0)).setText(resultMap.get("name"));
			thisResult.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((MyApplication) page.getApplication()).setPodcastsSlug(resultMap.get("slug"));
					Intent myIntent = new Intent(page, ((MyApplication) page.getApplication())
							.getPageClass(MollyModule.PODCAST_CATEGORY_PAGE));
					page.startActivityForResult(myIntent, 0);
				}
			});
		}
	}

	@Override
	protected List<Map<String,String>> doInBackground(JSONObject... args) {
		try {
			JSONArray jsonCategories = args[0].getJSONArray("categories");
			List<Map<String,String>> resultMapsList = new ArrayList<Map<String, String>>();
			
			for (int i = 0; i < jsonCategories.length(); i++)
			{
				JSONObject category = jsonCategories.getJSONObject(i);
				Map<String,String> resultMap = new HashMap<String,String>();
				resultMap.put("name", category.getString("name"));
				resultMap.put("slug", category.getString("slug"));
				resultMapsList.add(resultMap);
			}
			return resultMapsList;
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (NullPointerException e) {
			e.printStackTrace();
			nullPointerException = true;
		}
		return null;
	}
	

}
