package org.mollyproject.android.view.apps.podcasts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PodcastsCategoryPage extends ContentPage {
	protected static final int AUDIO = 0;
	protected static final int VIDEO = 1;
	protected static final int ALL = 2;
	protected List<Map<String,String>> resultMapsList = null;
	protected List<Map<String,String>> audios;
	protected List<Map<String,String>> videos;
	protected Button audiosButton;
	protected Button videosButton;
	protected boolean firstLoad;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		new PodcastsCategoryTask(this,true, true).execute(myApp.getPodcastsSlug());
		firstLoad = true;
		audios = new ArrayList<Map<String,String>>();
		videos = new ArrayList<Map<String,String>>();
		audiosButton = new Button(this);
		videosButton = new Button(this);
		
		audiosButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				contentLayout.removeAllViews();
				LinearLayout podcastsLayout = (LinearLayout) layoutInflater.inflate(R.layout.general_search_results_page, 
					contentLayout, false);
				contentLayout.addView(podcastsLayout);
				updatePage(audios);
			}
		});
		
		videosButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				contentLayout.removeAllViews();
				
				updatePage(videos);
			}
		});
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
		LinearLayout resultsLayout = (LinearLayout) findViewById(R.id.generalResultsList);
		if (!firstLoad)
		{
			((ViewGroup) audiosButton.getParent()).removeAllViews();
		}
		resultsLayout.addView(audiosButton);
		if (resultMapsList == null)
		{
			this.resultMapsList = resultMapsList;
		}
		for (int i = 0; i < resultMapsList.size(); i++)
		{
			final Map<String,String> resultMap = resultMapsList.get(i);
			
			LinearLayout thisResult = (LinearLayout) layoutInflater.inflate(R.layout.podcast_category_result, 
					contentLayout,false);
			resultsLayout.addView(thisResult);
			
			thisResult.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					myApp.setIndPodcastSlug(resultMap.get("slug"));
					Intent myIntent = new Intent(PodcastsCategoryPage.this, IndividualPodcastPage.class);
					PodcastsCategoryPage.this.startActivityForResult(myIntent, 0);
				}
			});
			
			thisResult.setLayoutParams(Page.paramsWithLine);
			
			LinearLayout iconsLayout = (LinearLayout) thisResult.getChildAt(0);
			
			String medium = resultMap.get("medium");
			ImageView mediumIcon = (ImageView) iconsLayout.getChildAt(1);
			mediumIcon.setImageResource(myApp.getImgResourceId(medium));
			
			if (medium.equals("video") & firstLoad)
			{
				videos.add(resultMap);
			}
			else if (medium.equals("audio") & firstLoad)
			{
				audios.add(resultMap);
			}
			
			ImageView podcastIcon = (ImageView) iconsLayout.getChildAt(0); 
			String urlStr = resultMap.get("logoURL");
			TextView infoText = (TextView) thisResult.getChildAt(1);
			infoText.setText(Html.fromHtml("<font size=18>" + resultMap.get("title") + "</font>" +
					"<br/>" + resultMap.get("description")));
			new DownloadImageTask(this,podcastIcon, urlStr).execute();
		}
		firstLoad = false;
	}
	
}
