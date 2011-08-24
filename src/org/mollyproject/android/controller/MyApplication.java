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
import android.net.ConnectivityManager;

public class MyApplication extends RoboApplication {
	//Intermediate stage for caching data in a session
	public static Router router = null; //make router null at first and check for it every time to avoid null pointers
	public static String mapQuery;
	public static String[] contactQuery = new String[2]; //arbitrary array of contact query, should expect the actual query and the required medium
	public static String[] generalQuery;
	public static String[] placesArgs;
	public static String bookControlNumber;
	public static List<Map<String,String>> podcastsOutput;
	public static Map<String,String> libraryQuery = null;
	public static String locator;
	public static String podcastsSlug = new String();
	public static String indPodcastSlug = new String();
	public static ArrayListMultimap<String,JSONObject> libraryCache = ArrayListMultimap.create();
	public static boolean destroyed = true;
	public static Injector injector;
	public static int lastTransportTab;
	public static Map<String,Bitmap> podcastIconsCache = new HashMap<String,Bitmap>();
	public static JSONObject transportCache = null;
	
	public static final String PREFS_NAME = "MyPrefsFile";
	
	public static DateFormat defaultDateFormat = new SimpleDateFormat
			("EEE, d MMM yyyy HH:mm:ss Z");
	public static DateFormat myDateFormat = new SimpleDateFormat
			("d MMM yyyy");
	public static DateFormat podcastDateFormat = new SimpleDateFormat
			("yyyy-MM-d HH:mm:ss");
	
	public static DateFormat trainDateFormat = new SimpleDateFormat
			("yyyy-MM-d HH:mm:ss Z");
	
	public boolean isOnline() {
		 return ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))
				 				.getActiveNetworkInfo().isConnectedOrConnecting();
	}
	
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
	
	/*
	public void setDestroyed(boolean b) { destroyed = b; }
	
	public boolean isDestroyed() { return destroyed; }
	
	public Router getRouter() {	return router; }
	
	public void setRouter(Router router) { this.router = router; }

	public void setLastTransportTab(int i) { lastTransportTab = i; }
	
	public int getLastTransportTab() { return lastTransportTab; }
	
	public void setTransportCache(JSONObject transportCache) { this.transportCache = transportCache; }
	
	public JSONObject getTransportCache() { return transportCache; }
	
	public String getBookNumber() { return bookControlNumber; }
	
	public void setBookNumber(String controlNumber) { bookControlNumber = controlNumber; }
	
	public void setPlacesArgs(String[] args) { placesArgs = args; }
	
	public String[] getPlacesArgs() { return placesArgs; }
	
	
	
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
	
	public void setLibraryArgs(Map<String,String> query) { libraryQuery = query; }
	
	public Map<String,String> getLibraryArgs() { return libraryQuery; }
	
	public String getLocator() { return locator; }
	
	public void setNextLocator(String locator) { this.locator = locator; } */
	
	public static Class <? extends Page> getPageClass(String s)
	{
		return (injector.getInstance(Key.get(Page.class, Names.named(s))).getClass());
	}
	
	public static int getImgResourceId(String viewName)
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


















