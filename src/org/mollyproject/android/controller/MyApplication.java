package org.mollyproject.android.controller;

import java.util.List;

import org.json.JSONObject;
import org.mollyproject.android.view.apps.Page;

import roboguice.application.RoboApplication;

import com.google.common.collect.ArrayListMultimap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;

import android.net.ConnectivityManager;

public class MyApplication extends RoboApplication {
	protected Router router = null;
	protected String mapQuery;
	protected JSONObject contactOutput;
	protected String generalQuery;
	protected String libraryQuery;
	protected String locator;
	protected ArrayListMultimap<String,JSONObject> libraryCache = ArrayListMultimap.create();
	protected boolean destroyed = false;
	protected Injector injector;
	
	public boolean isOnline() {
		 return ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))
				 				.getActiveNetworkInfo().isConnectedOrConnecting();
	}
	
	private long curTime = 0;
	public void timeStart() { curTime = System.currentTimeMillis(); }
	
	public void timeStop()
	{
		long now = System.currentTimeMillis();
		curTime = now - curTime;
		System.out.println(curTime+" milliseconds");
	}
	
	public void setGeneralQuery(String generalQuery) { this.generalQuery = generalQuery; }
	
	public String getGeneralQuery() { return generalQuery; }
	
	public void updateLibCache(String key, JSONObject object) { libraryCache.put(key, object);	}
	
	public ArrayListMultimap<String, JSONObject> getLibCache() { return libraryCache; }
	
	public void setContactOutput(JSONObject contactOutput) { this.contactOutput = contactOutput; }
	
	public JSONObject getContactOutput() { return contactOutput; }
	
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


















