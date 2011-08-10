package org.mollyproject.android.controller;

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
	protected Router router = null;
	protected String mapQuery;
	protected String[] contactQuery;
	protected List<Map<String,String>> generalOutput;
	protected List<Map<String,String>> podcastsOutput;
	protected String libraryQuery;
	protected String locator;
	protected String podcastsSlug = null;
	protected ArrayListMultimap<String,JSONObject> libraryCache = ArrayListMultimap.create();
	protected boolean destroyed = false;
	protected Injector injector;
	Map<String,Bitmap> podcastIconsCache = new HashMap<String,Bitmap>();
	
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
	
	public void setPodcastsOutput(List<Map<String,String>> podcastsOutput) { this.podcastsOutput = podcastsOutput; }
	
	public List<Map<String,String>> getPodcastsOutput() { return podcastsOutput; }
	
	public void setPodcastsSlug(String slug) { podcastsSlug = slug; }
	
	public String getPodcastsSlug() { return podcastsSlug; }
	
	public void setGeneralOutput(List<Map<String,String>> generalOutput) { this.generalOutput = generalOutput; }
	
	public List<Map<String,String>> getGeneralOutput() { System.out.println(generalOutput); return generalOutput; }
	
	public void updateLibCache(String key, JSONObject object) { libraryCache.put(key, object);	}
	
	public ArrayListMultimap<String, JSONObject> getLibCache() { return libraryCache; }
	
	public void setContactOutput(String... contactQuery) { this.contactQuery = contactQuery; }
	
	public String[] getContactQuery() { return contactQuery; }
	
	public void setMapQuery(String mapQuery) { this.mapQuery = mapQuery; }
	
	public String getMapQuery() { return mapQuery; }
	
	public Router getRouter() {	return router; }
	
	public void setRouter(Router router) { this.router = router; }

	public void setLibraryQuery(String query) { libraryQuery = query; }
	
	public String getLibraryQuery() { return libraryQuery; }
	
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


















