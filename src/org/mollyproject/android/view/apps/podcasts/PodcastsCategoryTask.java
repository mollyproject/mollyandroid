package org.mollyproject.android.view.apps.podcasts;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.RejectedExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PodcastsCategoryTask extends BackgroundTask<JSONArray, Void, JSONArray>
{
	protected volatile boolean rejectedDownloadImages;
	
	protected static Map<Integer,String> mediumHeaders = new HashMap<Integer,String>();
	static {
		mediumHeaders.put(PodcastsCategoryPage.ALL, "Showing all types of media.");
		mediumHeaders.put(PodcastsCategoryPage.AUDIO, "Showing only audios.");
		mediumHeaders.put(PodcastsCategoryPage.VIDEO, "Showing only videos.");
	}
	
	//Map<ImageView,String> imagesCache;
	Queue<Map<ImageView,String>> downloadQueue;
	protected int spawned;
	public PodcastsCategoryTask(PodcastsCategoryPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		rejectedDownloadImages = false;
		spawned = 0;
		downloadQueue = new LinkedList<Map<ImageView,String>>();
	}

	@Override
	public void updateView(JSONArray podcasts) {
		updatePage(podcasts);
	}

	@Override
	protected JSONArray doInBackground(JSONArray... params) {
		
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JSONArray podcasts = null;
		if(params.length > 0)
		{
			podcasts = params[0];
		}
		else 
		{
			try {
				podcasts = ((ContentPage) page).getJSONContent().getJSONArray("podcasts");
			} catch (JSONException e) {
				e.printStackTrace();
				jsonException = true;
			};
		}
		return podcasts;
	}
	
	public void updatePage(JSONArray podcasts)
	{
		LinearLayout contentLayout = ((ContentPage) page).getContentLayout();
		contentLayout.removeAllViews();
		
		LayoutInflater layoutInflater = page.getLayoutInflater();
		
		LinearLayout podcastsLayout = (LinearLayout) layoutInflater.inflate(R.layout.general_search_results_page,null);
		contentLayout.addView(podcastsLayout);
		
		LinearLayout resultsLayout = (LinearLayout) podcastsLayout.findViewById(R.id.generalResultsList);
		try
		{
			for (int i = 0; i < podcasts.length(); i++)
			{
				final JSONObject result = podcasts.getJSONObject(i);
				
				LinearLayout thisResult = (LinearLayout) layoutInflater.inflate(R.layout.podcast_category_result,null);
				resultsLayout.addView(thisResult);
				
				TextView mediumText = (TextView) podcastsLayout.findViewById(R.id.searchResultsHeader);
				mediumText.setText(mediumHeaders.get(PodcastsCategoryPage.currentlyShowing));
				
				final String slug = result.getString("slug");
				thisResult.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						MyApplication.indPodcastSlug = slug;
						Intent myIntent = new Intent(page, IndividualPodcastPage.class);
						page.startActivityForResult(myIntent, 0);
					}
				});
				
				thisResult.setLayoutParams(Page.paramsWithLine);
				
				String medium = result.getString("medium");
				ImageView mediumIcon = (ImageView) thisResult.findViewById(R.id.mediaIcon);
				mediumIcon.setImageResource(MyApplication.getImgResourceId(medium));
				
				//podcast logo
				ImageView podcastIcon = (ImageView) thisResult.findViewById(R.id.podcastIcon); 
				String urlStr = result.getString("logo");
				
				TextView podcastText = (TextView) thisResult.findViewById(R.id.podcastText);
				
				String description = result.getString("description");
				if (description.length() > 60)
				{
					int j = 40;
					if (description.charAt(40) != ' ')
					{
						while (description.charAt(j) != ' ') { j++;	}
					}
					description = description.substring(0, j)+"...";
				}
				podcastText.setText(Html.fromHtml("<font size=18>" + result.getString("title") + "</font>" +
						"<br/>" + description));
				
				((PodcastsCategoryPage) page).populateArrays(result,medium);
				Map<ImageView,String> imagesCache = new HashMap<ImageView,String>();
				imagesCache.put(podcastIcon,urlStr);
				downloadQueue.add(imagesCache);
			}
			//new ImageBatchesTask(page, false, false).execute(downloadQueue);
			((PodcastsCategoryPage) page).setDownloadQueue(downloadQueue);
			((ContentPage) page).doneProcessingJSON();
			
			
			PodcastsCategoryPage.imageTask = new ImageBatchesTask(page, false, false);
			PodcastsCategoryPage.imageTask.execute();
			
		} catch (RejectedExecutionException e) {
			e.printStackTrace();
			if (!rejectedDownloadImages)
			{
				Toast.makeText(page.getApplicationContext(), "Image download request rejected. " +
					"Some images will not show up properly.", Toast.LENGTH_LONG).show();
				/*Page.popupErrorDialog("Image request rejected","Some images will not show up properly."
						, page, false);*/
				rejectedDownloadImages = true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
		
		PodcastsCategoryPage.firstLoad = false;
	}
	
}























