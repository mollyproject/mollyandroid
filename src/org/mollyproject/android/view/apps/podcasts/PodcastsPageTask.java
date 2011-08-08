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

import android.widget.LinearLayout;
import android.widget.TextView;

public class PodcastsPageTask extends BackgroundTask<Void, Void, List<Map<String,String>>>{
	
	public PodcastsPageTask(PodcastsPage podcastsPage, boolean b)
	{
		super(podcastsPage, b);
		
	}
	
	@Override
	public void updateView(List<Map<String,String>> resultMapsList) {
		for (int i = 0; i < resultMapsList.size(); i++)
		{
			Map<String,String> resultMap = resultMapsList.get(i);
			LinearLayout resultLayout = (LinearLayout) page.getLayoutInflater().inflate
					(R.layout.podcasts_search_result, ((PodcastsPage) page).getContentLayout());
			//((ContentPage) page).getContentLayout().addView(resultLayout);
			System.out.println(resultLayout.getChildAt(0).getId());
		}
	}

	@Override
	protected List<Map<String,String>> doInBackground(Void... params) {
		try {
			JSONArray jsonCategories = page.getRouter().onRequestSent("podcasts:index", 
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
