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
import org.mollyproject.android.view.apps.Page;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
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
		LinearLayout podcastsLayout = (LinearLayout) inflater.inflate(R.layout.general_search_results_page, 
				((PodcastsCategoryPage) page).getContentLayout(), false);
		((PodcastsCategoryPage) page).getContentLayout().addView(podcastsLayout);
		
		//TextView header = (TextView) page.findViewById(R.id.searchResultsHeader);
		
		((PodcastsCategoryPage) page).updatePage(resultMapsList);
		
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
				
				String description = result.getString("description");
				if (description.length() > 60)
				{
					int j = 40;
					if (description.charAt(40) != ' ')
					{
						
						while (description.charAt(j) != ' ')
						{
							j++;
							System.out.println("STUCK HERE");
						}
					}
					description = description.substring(0, j)+"...";
					resultMap.put("description", description);
				}
				else
				{
					resultMap.put("description", description);
				}
				resultMap.put("logoURL", result.getString("logo"));
				resultMap.put("slug", result.getString("slug"));
				resultMap.put("medium", result.getString("medium"));
				resultMapsList.add(resultMap);
			}
			return resultMapsList;
		} catch (NullPointerException e) {
			e.printStackTrace();
			nullPointerException = true;
		}
		catch (UnknownHostException e) {
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




















