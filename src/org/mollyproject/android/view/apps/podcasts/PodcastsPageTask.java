package org.mollyproject.android.view.apps.podcasts;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.search.SearchPage;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PodcastsPageTask extends BackgroundTask<Void, Void, List<Map<String,String>>>{
	
	public PodcastsPageTask(PodcastsPage podcastsPage, boolean b)
	{
		super(podcastsPage, b);
		
	}
	
	@Override
	public void updateView(List<Map<String,String>> resultMapsList) {
		//Exactly the same arrangements and layouts as Search Results 
		//only with podcasts_search_result
		LayoutInflater inflater = page.getLayoutInflater();
		LinearLayout podcastsLayout = (LinearLayout) inflater.inflate(R.layout.search_results_page, 
				((PodcastsPage) page).getContentLayout(), false);
		((PodcastsPage) page).getContentLayout().addView(podcastsLayout);
		
		TextView header = (TextView) page.findViewById(R.id.searchResultsHeader);
		header.setText("By division");
		
		LinearLayout resultsLayout = (LinearLayout) page.findViewById(R.id.generalResultsList);
		
		resultsLayout.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < resultMapsList.size(); i++)
		{
			Map<String,String> resultMap = resultMapsList.get(i);
			LinearLayout thisResult = (LinearLayout) inflater.inflate
					(R.layout.podcasts_search_result, ((PodcastsPage) page).getContentLayout(),false);
			resultsLayout.addView(thisResult);
			thisResult.setLayoutParams(Page.paramsWithLine);
			((TextView) thisResult.getChildAt(0)).setText(resultMap.get("name"));
		}
	}

	@Override
	protected List<Map<String,String>> doInBackground(Void... params) {
		try {
			JSONArray jsonCategories = page.getRouter().onRequestSent("podcasts:index", null,
					Router.OutputFormat.JSON, null).getJSONArray("categories");
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
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

}
