package org.mollyproject.android.controller;

import java.util.LinkedList;
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
	protected LinkedList<String> bcTrail;
	protected int bcCount;
	protected Router router;
	protected String mapQuery;
	protected String contactQuery;
	protected String libraryQuery;
	protected String unimplementedLocator;
	protected ArrayListMultimap<String,JSONObject> libraryCache;
	protected boolean destroyed = false;
	protected Injector injector;
	
	public MyApplication() throws Exception
	{
		super();
		bcCount = 0;
		router = null;
		libraryCache = ArrayListMultimap.create();
		bcTrail = new LinkedList<String>();
	}
	
	public void updateBreadCrumb(String breadcrumb)
	{
		if (!bcTrail.contains(breadcrumb))
		{
			//new breadcrumb, just add to the tail of the list
			bcTrail.add(breadcrumb);
			bcCount++;
		}
		else 
		{
			//breadcrumb already exists, this indicates that the user either pressed
			//the back key or an item on the breadcrumb bar, solution: remove anything
			//after it
			int target = bcTrail.indexOf(breadcrumb);
			System.out.println(target);
			//pre: target is not -1
			for (int i = 0; i < bcTrail.size(); i++)
			{
				if (i > target)
				{
					bcTrail.remove(i);
				}
			}
		}
		
		System.out.println("Trail extended");
		for (String b : bcTrail)
		{
			System.out.println(b);
		}
	}
	
	public void removeBreadCrumb(String breadcrumb)
	{	
		if ((bcCount > 0)&bcTrail.get(bcTrail.size()-1).equals(breadcrumb))
		{
			bcCount--;
			bcTrail.remove(bcCount);
		}
		System.out.println("Trail contracted");
		for (String b : bcTrail)
		{
			System.out.println(b);
		}
	}
	
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
	
	public void updateLibCache(String key, JSONObject object) { libraryCache.put(key, object);	}
	
	public ArrayListMultimap<String, JSONObject> getLibCache() { return libraryCache; }
	
	public void setContactQuery(String query) { contactQuery = query; }
	
	public String getContactQuery() { return contactQuery; }
	
	public LinkedList<String> getTrail()	{ return (LinkedList<String>) bcTrail; }
	
	public void setMapQuery(String mapQuery) { this.mapQuery = mapQuery; }
	
	public String getMapQuery() { return mapQuery; }
	
	public Router getRouter() {	return router; }
	
	public void setRouter(Router router) { this.router = router; }

	public void setLibraryQuery(String query) { libraryQuery = query; }
	
	public String getLibraryQuery() { return libraryQuery; }
	
	public String getUnimplementedLocator() { return unimplementedLocator; }
	
	public void setUnimplementedLocator(String unimplementedLocator) { this.unimplementedLocator = unimplementedLocator; } 
	
	public Page test(String s)
	{
		Page page = injector.getInstance(Key.get(Page.class, Names.named(s)));
		System.out.println(s);
		return page;
	}
	
	@Override
    protected void addApplicationModules(List<Module> modules) {
        MollyModule module = new MollyModule();
		modules.add(module);
        injector = Guice.createInjector(module);
    }
}


















