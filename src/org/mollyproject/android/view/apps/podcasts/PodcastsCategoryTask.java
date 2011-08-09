package org.mollyproject.android.view.apps.podcasts;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.controller.MyApplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class PodcastsCategoryTask extends BackgroundTask<String, Void, List<Map<String,String>>>{
	
	public PodcastsCategoryTask(PodcastsPage podcastsCategoryPage, boolean toDestroy, boolean dialog)
	{
		super(podcastsCategoryPage, toDestroy, dialog);
	}
	
	@Override
	public void updateView(List<Map<String,String>> resultMapsList) {
		((MyApplication) page.getApplication()).setPodcastsOutput(resultMapsList);
		System.out.println("REACHED END OF PODCAST_CAT_TASK");
		Intent myIntent = new Intent(page, PodcastsCategoryPage.class);
		page.startActivityForResult(myIntent, 0);

	}

	@Override
	protected List<Map<String, String>> doInBackground(String... args) {
		try {
			//args[0] should be the slug
			JSONObject jsonOutput = page.getRouter().onRequestSent("podcasts:category", args[0],
					Router.OutputFormat.JSON, null);
			JSONArray podcasts = jsonOutput.getJSONArray("podcasts");
			List<Map<String,String>> resultMapsList = new ArrayList<Map<String,String>>();
			
			for (int i = 0; i < podcasts.length(); i++)
			{
				JSONObject result = podcasts.getJSONObject(i);
				Map<String,String> resultMap = new HashMap<String,String>();
				resultMap.put("title", result.getString("title"));
				resultMap.put("description", result.getString("description"));
				resultMap.put("logoURL", result.getString("logo"));
				resultMap.put("slug", result.getString("slug"));
				resultMap.put("medium", result.getString("medium"));
				resultMapsList.add(resultMap);
			}
			return resultMapsList;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			unknownHostException = true;
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (IOException e) {
			e.printStackTrace();
			ioException = true;
		}
		return null;
	}
	
}




















