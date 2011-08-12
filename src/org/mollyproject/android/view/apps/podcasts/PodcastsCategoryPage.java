package org.mollyproject.android.view.apps.podcasts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PodcastsCategoryPage extends ContentPage {
	protected static final int AUDIO = R.id.showAudioItem;
	protected static final int VIDEO = R.id.showVideoItem;
	protected static final int ALL = R.id.showAllItem;
	protected int currentlyShowing;
	protected List<Map<String,String>> all;
	protected List<Map<String,String>> audios;
	protected List<Map<String,String>> videos;
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
		new PodcastsCategoryTask(this,true, true).execute(myApp.getPodcastsSlug());
		firstLoad = true;
		currentlyShowing = ALL;
		all = new ArrayList<Map<String,String>>();
		audios = new ArrayList<Map<String,String>>();
		videos = new ArrayList<Map<String,String>>();
	}
	
	@Override
	public Page getInstance() {
		return this;
	}
	
	@Override
	public String getAdditionalParams() {
		// TODO Auto-generated method stub
		return myApp.getPodcastsSlug();
	}
	
	public void updatePage(List<Map<String,String>> resultMapsList)
	{
		//this is a hack: the view hierachy must be done in one UI thread only so I cannot inflate
		//the resultsLayout in the BackgroundTask then download the logos to the imageviews in another
		//So I have to do this here but still want to keep the progress spinner thus keeping the task
		LinearLayout resultsLayout = (LinearLayout) findViewById(R.id.generalResultsList);
		for (int i = 0; i < resultMapsList.size(); i++)
		{
			final Map<String,String> resultMap = resultMapsList.get(i);
			
			LinearLayout thisResult = (LinearLayout) layoutInflater.inflate(R.layout.podcast_category_result, 
					contentLayout,false);
			resultsLayout.addView(thisResult);
			
			showMedium(currentlyShowing);
			
			thisResult.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					myApp.setIndPodcastSlug(resultMap.get("slug"));
					Intent myIntent = new Intent(PodcastsCategoryPage.this, IndividualPodcastPage.class);
					PodcastsCategoryPage.this.startActivityForResult(myIntent, 0);
				}
			});
			
			thisResult.setLayoutParams(Page.paramsWithLine);
			
			String medium = resultMap.get("medium");
			ImageView mediumIcon = (ImageView) thisResult.findViewById(R.id.mediaIcon);
			mediumIcon.setImageResource(myApp.getImgResourceId(medium));
			
			if (firstLoad)
			{
				all.add(resultMap);
				TextView mediumText = (TextView) findViewById(R.id.searchResultsHeader);
				mediumText.setText("Showing all types of media.");
			}
			if (medium.equals("video") & firstLoad)
			{
				videos.add(resultMap);
			}
			else if (medium.equals("audio") & firstLoad)
			{
				audios.add(resultMap);
			}
			
			//medium text
			ImageView podcastIcon = (ImageView) thisResult.findViewById(R.id.podcastIcon); 
			String urlStr = resultMap.get("logoURL");
			TextView podcastText = (TextView) thisResult.findViewById(R.id.podcastText);
			podcastText.setText(Html.fromHtml("<font size=18>" + resultMap.get("title") + "</font>" +
					"<br/>" + resultMap.get("description")));
			try
			{
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
			}
			
		}
		firstLoad = false;
	}
	
	protected void showMedium(int medium)
	{
		//only for use after R.layout.podcast_category_result is inflated in its scope
		//otherwise the textview will be null
		TextView mediumText = (TextView) findViewById(R.id.searchResultsHeader);
		mediumText.setText(mediumTexts.get(medium));
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
}

























