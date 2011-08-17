package org.mollyproject.android.view.apps.podcasts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PodcastsCategoryPage extends ContentPage {
	protected static final int AUDIO = R.id.showAudioItem;
	protected static final int VIDEO = R.id.showVideoItem;
	protected static final int ALL = R.id.showAllItem;
	protected int currentlyShowing;
	protected String slug;
	protected JSONArray all;
	protected JSONArray audios;
	protected JSONArray videos;
	protected volatile boolean rejectedDownloadImages = false;
	protected boolean firstLoad;
	
	protected static Map<Integer,String> mediumTexts = new HashMap<Integer,String>();
	static {
		mediumTexts.put(ALL, "Showing all types of media.");
		mediumTexts.put(AUDIO, "Showing only audios.");
		mediumTexts.put(VIDEO, "Showing only videos.");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		slug = myApp.getPodcastsSlug();
		firstLoad = true;
		currentlyShowing = ALL;
		all = new JSONArray();
		audios = new JSONArray();
		videos = new JSONArray();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (!jsonProcessed)
		{
			new PodcastsCategoryTask(this,true, true).execute();
		}
	}
	
	@Override
	public Page getInstance() {
		return this;
	}
	
	@Override
	public String getAdditionalParams() {
		return ("&arg=" + slug);
	}
	
	public void updatePage(JSONArray podcasts)
	{
		contentLayout.removeAllViews();
		LinearLayout podcastsLayout = (LinearLayout) layoutInflater.inflate(R.layout.general_search_results_page, 
				contentLayout, false);
		contentLayout.addView(podcastsLayout);
		
		LinearLayout resultsLayout = (LinearLayout) findViewById(R.id.generalResultsList);
		
		for (int i = 0; i < podcasts.length(); i++)
		{
			try
			{
				final JSONObject result = podcasts.getJSONObject(i);
				
				LinearLayout thisResult = (LinearLayout) layoutInflater.inflate(R.layout.podcast_category_result, 
						contentLayout,false);
				resultsLayout.addView(thisResult);
				
				TextView mediumText = (TextView) findViewById(R.id.searchResultsHeader);
				mediumText.setText(mediumTexts.get(currentlyShowing));
				
				final String slug = result.getString("slug");
				thisResult.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						myApp.setIndPodcastSlug(slug);
						Intent myIntent = new Intent(PodcastsCategoryPage.this, IndividualPodcastPage.class);
						PodcastsCategoryPage.this.startActivityForResult(myIntent, 0);
					}
				});
				
				thisResult.setLayoutParams(Page.paramsWithLine);
				
				String medium = result.getString("medium");
				ImageView mediumIcon = (ImageView) thisResult.findViewById(R.id.mediaIcon);
				mediumIcon.setImageResource(myApp.getImgResourceId(medium));
				
				if (firstLoad)
				{
					all.put(result);
					mediumText.setText("Showing all types of media.");
				}
				if (medium.equals("video") & firstLoad)
				{
					videos.put(result);
				}
				else if (medium.equals("audio") & firstLoad)
				{
					audios.put(result);
				}
				
				//medium text
				ImageView podcastIcon = (ImageView) thisResult.findViewById(R.id.podcastIcon); 
				String urlStr = result.getString("logo");
				
				TextView podcastText = (TextView) thisResult.findViewById(R.id.podcastText);
				
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
				}
				podcastText.setText(Html.fromHtml("<font size=18>" + result.getString("title") + "</font>" +
						"<br/>" + description));
				
				new DownloadImageTask(this,podcastIcon, urlStr).execute();
				
			} catch (RejectedExecutionException e) {
				e.printStackTrace();
				if (!rejectedDownloadImages)
				{
					/*Toast.makeText(this, "Image download request rejected. " +
						"Some images will not show up properly.", Toast.LENGTH_LONG).show();*/
					popupErrorDialog("Image request rejected","Some images will not show up properly."
							, this, false);
					rejectedDownloadImages = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		firstLoad = false;
	}
	
	protected void showMedium(int medium)
	{
		//only for use after R.layout.podcast_category_result is inflated in its scope
		//otherwise the textview will be null
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		LinearLayout podcastsLayout = (LinearLayout) layoutInflater.inflate(R.layout.general_search_results_page, 
				contentLayout, false);
		if (!firstLoad)
		{
			switch (item.getItemId()) {
	    	case ALL:
	    		if (currentlyShowing != ALL)
	        	{
		    		currentlyShowing = ALL;
		    		contentLayout.removeAllViews();
					contentLayout.addView(podcastsLayout);
					updatePage(all);
	        	}
	    		break;
	        case AUDIO:
	        	if (currentlyShowing != AUDIO)
	        	{
		        	currentlyShowing = AUDIO;
		        	contentLayout.removeAllViews();
					contentLayout.addView(podcastsLayout);
					updatePage(audios);
	        	}
	        	break;
	        case VIDEO:
	        	if (currentlyShowing != VIDEO)
	        	{
		        	currentlyShowing = VIDEO;
		        	contentLayout.removeAllViews();
					contentLayout.addView(podcastsLayout);
					updatePage(videos);
	        	}
	            break;
		    }
		}
	    return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.audio_video, menu);
	    return true;
	}

	@Override
	public String getName() {
		return MollyModule.PODCAST_CATEGORY_PAGE;
	}

	@Override
	public String getQuery() {
		return null;
	}
	
	private class PodcastsCategoryTask extends BackgroundTask<Void, Void, JSONObject>
	{

		public PodcastsCategoryTask(Page page,
				boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
			super(page, toDestroyPageAfterFailure, dialogEnabled);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void updateView(JSONObject outputs) {
			try {
				JSONArray podcasts = outputs.getJSONArray("podcasts");
				updatePage(podcasts);
				jsonProcessed = true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			while (!jsonDownloaded)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return jsonContent;
		}
		
	}
}

























