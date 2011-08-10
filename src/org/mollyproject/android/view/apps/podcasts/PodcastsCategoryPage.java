package org.mollyproject.android.view.apps.podcasts;

import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class PodcastsCategoryPage extends ContentPage {
	protected static final int AUDIO = 0;
	protected static final int VIDEO = 1;
	protected static final int ALL = 2;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		new PodcastsCategoryTask(this,true, true).execute(myApp.getPodcastsSlug());
		//new PodcastsCategoryTask(this, true, true).execute(myApp.getPodcastsOutput());
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
}
