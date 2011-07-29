package org.mollyproject.android.controller;

import java.util.LinkedList;
import android.app.Application;
import android.net.ConnectivityManager;

public class MyApplication extends Application {
	protected LinkedList<String> bcTrail;
	protected int bcCount;
	protected Router router;
	protected String mapQuery;
	protected String contactQuery;
	protected String libraryQuery;
	
	public MyApplication() throws Exception
	{
		super();
		bcCount = 0;
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
	
	public void setContactQuery(String query) { contactQuery = query; }
	
	public String getContactQuery() { return contactQuery; }
	
	public LinkedList<String> getTrail()	{ return (LinkedList<String>) bcTrail; }
	
	public void setMapQuery(String mapQuery) { this.mapQuery = mapQuery; }
	
	public String getMapQuery() { return mapQuery; }
	
	public Router getRouter() {	return router; }
	
	public void setRouter(Router router) { this.router = router; }

	public void setLibraryQuery(String query) { libraryQuery = query; }
	
	public String getLibraryQuery() { return libraryQuery; }
}
