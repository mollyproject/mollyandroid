package org.mollyproject.android.controller;

import java.util.LinkedList;
import android.app.Application;

public class MyApplication extends Application {
	protected LinkedList<String> bcTrail;
	protected int bcCount;
	protected Router router;
	protected String mapQuery;
	protected String contactOutput;
	protected String libraryOutput;
	
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
	
	private long curTime = 0;
	public void timeStart() { curTime = System.nanoTime(); }
	
	public void timeStop()
	{
		long now = System.nanoTime();
		curTime = now - curTime;
		System.out.println("Time elapsed: "+curTime);
	}
	
	public void setContactOutput(String jsonOutput) { contactOutput = jsonOutput; }
	
	public String getContactOutput() { return contactOutput; }
	
	public LinkedList<String> getTrail()	{ return (LinkedList<String>) bcTrail; }
	
	public void setMapQuery(String mapQuery) { this.mapQuery = mapQuery; }
	
	public String getMapQuery() { return mapQuery; }
	
	public Router getRouter() {	return router; }
	
	public void setRouter(Router router) { this.router = router; }

	public void setLibraryOutput(String jsonOutput) { libraryOutput = jsonOutput; }
	
	public String getLibraryOutput() { return libraryOutput; }
}
