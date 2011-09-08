package org.mollyproject.android.view.apps.podcasts;

import java.util.Map;
import java.util.Queue;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

public class PodcastsCategoryPage extends ContentPage {
	public static final int AUDIO = R.id.showAudioItem;
	public static final int VIDEO = R.id.showVideoItem;
	public static final int ALL = R.id.showAllItem;
	public static int currentlyShowing;
	protected String slug;
	protected JSONArray all;
	protected JSONArray audios;
	protected JSONArray videos;
	protected Queue<Map<ImageView,String>> imageDownloadQueue;
	protected int runningImageThreads;
	public static boolean firstLoad;
	public static ImageBatchesTask imageTask; //this reference should be kept here because it can be easily paused from this page
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		slug = MyApplication.podcastsSlug;
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
	public void refresh() {
		if (!jsonProcessed|| manualRefresh)
		{
			manualRefresh = false;
			new PodcastsCategoryTask(this,true, true).execute();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (imageTask != null)
		{
			imageTask.cancel(true);
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (!firstLoad)
		{
			imageTask.cancel(true);
			contentScroll.scrollTo(0, 0);
			switch (item.getItemId()) {
	    	case ALL:
	    		if (currentlyShowing != ALL)
	        	{
		    		currentlyShowing = ALL;
		    		new PodcastsCategoryTask(this,false, false).execute(all);
	        	}
	    		break;
	        case AUDIO:
	        	if (currentlyShowing != AUDIO)
	        	{
		        	currentlyShowing = AUDIO;
		        	new PodcastsCategoryTask(this,false, false).execute(audios);
	        	}
	        	break;
	        case VIDEO:
	        	if (currentlyShowing != VIDEO)
	        	{
		        	currentlyShowing = VIDEO;
		        	new PodcastsCategoryTask(this,false, false).execute(videos);
	        	}
	            break;
	        default:
	        	return super.onOptionsItemSelected(item);
		    }
		}
	    return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.audio_video_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.setGroupEnabled(R.id.podcastItemsGroup, true); // Enable everything
	    menu.findItem(currentlyShowing).setEnabled(false); // but cannot select the option currently showing
		return super.onPrepareOptionsMenu(menu);
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

























