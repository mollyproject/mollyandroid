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
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PodcastsCategoryTask extends BackgroundTask<String, Void, List<Map<String,String>>>{
	
	public PodcastsCategoryTask(PodcastsCategoryPage podcastsCategoryPage, boolean toDestroy, boolean dialog)
	{
		super(podcastsCategoryPage, toDestroy, dialog);
	}
	
	@Override
	public void updateView(List<Map<String,String>> resultMapsList) {
		LayoutInflater inflater = page.getLayoutInflater();
		
		LinearLayout podcastsLayout = (LinearLayout) inflater.inflate(R.layout.search_results_page, 
				((PodcastsCategoryPage) page).getContentLayout(), false);
		((PodcastsCategoryPage) page).getContentLayout().addView(podcastsLayout);
		
		TextView header = (TextView) page.findViewById(R.id.searchResultsHeader);
		
		LinearLayout resultsLayout = (LinearLayout) page.findViewById(R.id.generalResultsList);
		for (int i = 0; i < resultMapsList.size(); i++)
		{
			Map<String,String> resultMap = resultMapsList.get(i);
			
			LinearLayout thisResult = (LinearLayout) inflater.inflate(R.layout.podcast_category_result, 
					((PodcastsCategoryPage) page).getContentLayout(),false);
			resultsLayout.addView(thisResult);
			thisResult.setLayoutParams(Page.paramsWithLine);
			
			LinearLayout iconsLayout = (LinearLayout) thisResult.getChildAt(0);
			
			ImageView podcastIcon = (ImageView) iconsLayout.getChildAt(0); 
			String urlStr = resultMap.get("logoURL");
			downloadImageAndShow(urlStr,podcastIcon);
			
			TextView infoText = (TextView) thisResult.getChildAt(1);
			infoText.setText(Html.fromHtml("<font size=18>" + resultMap.get("title") + "</font>" +
					"<br/>" + resultMap.get("description")));
		}
		System.out.println("REACHED END OF PODCAST_CAT_TASK");
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
	
	protected void downloadImageAndShow(String urlStr, ImageView imView)
	{
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn= (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			//conn.setConnectTimeout(5);
			conn.connect();
			InputStream is = conn.getInputStream();
			Bitmap bmImg = BitmapFactory.decodeStream(is);
			imView.setImageBitmap(bmImg);
		} catch (Exception e) {
			e.printStackTrace();
			imView.setImageResource(R.drawable.android_button);
		}
	}
}




















