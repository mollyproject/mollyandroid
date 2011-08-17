package org.mollyproject.android.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.mollyproject.android.view.apps.Page;

import roboguice.application.RoboApplication;

import com.google.common.collect.ArrayListMultimap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;

import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;

public class MyApplication extends RoboApplication {
	//Intermediate stage for caching data in a session
	protected Router router = null; //make router null at first and check for it every time to avoid null pointers
	protected String mapQuery;
	protected String[] contactQuery; //arbitrary array of contact query, should expect the actual query and the required medium
	protected String[] generalQuery;
	protected String[] placesArgs;
	protected List<Map<String,String>> podcastsOutput;
	protected Map<String,String> libraryQuery = null;
	protected String locator;
	protected String podcastsSlug = null;
	protected String indPodcastSlug = null;
	protected ArrayListMultimap<String,JSONObject> libraryCache = ArrayListMultimap.create();
	protected boolean destroyed = true;
	protected Injector injector;
	//protected LocationTracker locTracker = new LocationTracker(this);
	Map<String,Bitmap> podcastIconsCache = new HashMap<String,Bitmap>();
	
	public static DateFormat defaultDateFormat = new SimpleDateFormat
			("EEE, d MMM yyyy HH:mm:ss Z");
	public static DateFormat myDateFormat = new SimpleDateFormat
			("d MMM yyyy");
	public static DateFormat podcastDateFormat = new SimpleDateFormat
			("yyyy-MM-d HH:mm:ss");
	
	public void setDestroyed(boolean b) { destroyed = b; }
	
	public boolean isDestroyed() { return destroyed; }
	
	public boolean isOnline() {
		 return ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))
				 				.getActiveNetworkInfo().isConnectedOrConnecting();
	}
	
	public void setPlacesArgs(String[] args) { placesArgs = args; }
	
	public String[] getPlacesArgs() { return placesArgs; }
	
	public synchronized void updatePodcastIconsCache(String logoURL, Bitmap bitmap)
	{
		podcastIconsCache.put(logoURL, bitmap);
	}
	
	public synchronized boolean hasPodcastIcon(String logoURL)
	{
		return podcastIconsCache.containsKey(logoURL);
	}
	
	public synchronized Bitmap getIcon(String logoURL)
	{
		System.out.println("podcast image cache");
		return podcastIconsCache.get(logoURL);
	}
	
	public void setPodcastsOutput(List<Map<String,String>> podcastsOutput) { this.podcastsOutput = podcastsOutput; }
	
	public List<Map<String,String>> getPodcastsOutput() { return podcastsOutput; }
	
	public void setIndPodcastSlug(String slug) { indPodcastSlug = slug; }
	
	public String getIndPodcastSlug() { return indPodcastSlug; }
	
	public void setPodcastsSlug(String slug) { podcastsSlug = slug; }
	
	public String getPodcastsSlug() { return podcastsSlug; }
	
	public void setGeneralQuery(String[] query) { this.generalQuery = query; }
	
	public String[] getGeneralQuery() { return generalQuery; }
	
	public void updateLibCache(String key, JSONObject object) { libraryCache.put(key, object);	}
	
	public ArrayListMultimap<String, JSONObject> getLibCache() { return libraryCache; }
	
	public void setContactOutput(String... contactQuery) { this.contactQuery = contactQuery; }
	
	public String[] getContactQuery() { return contactQuery; }
	
	public void setMapQuery(String mapQuery) { this.mapQuery = mapQuery; }
	
	public String getMapQuery() { return mapQuery; }
	
	public Router getRouter() {	return router; }
	
	public void setRouter(Router router) { this.router = router; }

	public void setLibraryArgs(Map<String,String> query) { libraryQuery = query; }
	
	public Map<String,String> getLibraryArgs() { return libraryQuery; }
	
	public String getLocator() { return locator; }
	
	public void setNextLocator(String locator) { this.locator = locator; } 
	
	public Class <? extends Page> getPageClass(String s)
	{
		return (injector.getInstance(Key.get(Page.class, Names.named(s))).getClass());
	}
	
	public int getImgResourceId(String viewName)
	{
		return (injector.getInstance(Key.get(Integer.class, Names.named(viewName))));
	}
	
	@Override
    protected void addApplicationModules(List<Module> modules) {
        MollyModule module = new MollyModule();
		modules.add(module);
        injector = Guice.createInjector(module);
    }
}


















