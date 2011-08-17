package org.mollyproject.android.view.apps.podcasts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
	public static final int AUDIO = R.id.showAudioItem;
	public static final int VIDEO = R.id.showVideoItem;
	public static final int ALL = R.id.showAllItem;
	protected int currentlyShowing;
	protected String slug;
	protected JSONArray all;
	protected JSONArray audios;
	protected JSONArray videos;
	protected Queue<Map<ImageView,String>> imageDownloadQueue;
	protected int runningImageThreads;
	protected boolean firstLoad;
	protected ImageBatchesTask imageTask = null;
	
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
		runningImageThreads = 0;
	}
	
	public Queue<Map<ImageView,String>> getDownloadQueue()
	{
		return imageDownloadQueue;
	}
	
	public void setDownloadQueue(Queue<Map<ImageView,String>> downloadQueue)
	{
		imageDownloadQueue = downloadQueue;
	}
	
	public synchronized void incRunningImgThreads()
	{
		runningImageThreads++;
	}
	
	public synchronized void decRunningImgThreads()
	{
		runningImageThreads--;
	}
	
	public int getRunningImgThreads()
	{
		return runningImageThreads;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (!jsonProcessed)
		{
			new PodcastsCategoryTask(this,true, true).execute();
		}
		imageTask = new ImageBatchesTask(this, false, false);
		imageTask.execute();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		imageTask.cancel(true);
	}
	
	@Override
	public Page getInstance() {
		return this;
	}
	
	@Override
	public String getAdditionalParams() {
		return ("&arg=" + slug);
	}
	
	public void populateArrays(JSONObject result, String medium)
	{
		if (firstLoad)
		{
			all.put(result);
			//mediumText.setText("Showing all types of media.");
			if (medium.equals("video"))
			{
				videos.put(result);
			}
			else if (medium.equals("audio"))
			{
				audios.put(result);
			}
		}
	}
	
	public void firstLoadDone()
	{
		firstLoad = false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (!firstLoad)
		{
			switch (item.getItemId()) {
	    	case ALL:
	    		if (currentlyShowing != ALL)
	        	{
		    		currentlyShowing = ALL;
		    		new PodcastsCategoryTask(this,true, true).execute(all);
	        	}
	    		break;
	        case AUDIO:
	        	if (currentlyShowing != AUDIO)
	        	{
		        	currentlyShowing = AUDIO;
		        	new PodcastsCategoryTask(this,true, true).execute(audios);
	        	}
	        	break;
	        case VIDEO:
	        	if (currentlyShowing != VIDEO)
	        	{
		        	currentlyShowing = VIDEO;
		        	new PodcastsCategoryTask(this,true, true).execute(videos);
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
	
}

























