package org.mollyproject.android.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import android.app.Application;

public class MyApplication extends Application {
	protected List<String> bcTrail;
	protected List<MyAppListener> myAppListeners;
	protected List<MyAppListener> toBeRemoved;
	protected int bcCount;
	protected Router router;
	protected String mapQuery;
	protected String contactOutput;
	
	public MyApplication() throws Exception
	{
		super();
		bcCount = 0;
		bcTrail = new ArrayList<String>();
		myAppListeners = new ArrayList<MyAppListener>();
		toBeRemoved = new ArrayList<MyAppListener>();
	}
	
	public void addListener(MyAppListener l)
	{
		//Changed from enhanced for loop to counted for loop (faster)
		for (int i = 0; i < myAppListeners.size(); i++)
		{
			if (myAppListeners.get(i).getOwnerClass() == l.getOwnerClass())
			{
				toBeRemoved.add(myAppListeners.get(i));
			}
		}
		myAppListeners.add(l);
	}
	
	public void removeListener(MyAppListener l)
	{
		myAppListeners.remove(l);
	}
	
	public void addBreadCrumb(String breadcrumb)
	{
		if (!bcTrail.contains(breadcrumb))
		{
			bcTrail.add(breadcrumb);
			bcCount++;
			
			//Changed from enhanced for loop to counted for loop (faster)
			for (int i = 0; i < myAppListeners.size(); i++)
			{
				myAppListeners.get(i).onBreadCrumbAdded(breadcrumb);			
			}
		}
	}
	
	public void removeBreadCrumb()
	{	
		String temp = new String();
		//Changed from enhanced for loop to counted for loop (faster)
		for (int i = 0; i < toBeRemoved.size(); i++)
		{
			removeListener(toBeRemoved.get(i));
		}
		
		if (bcCount > 0)
		{
			bcCount--;
			temp = bcTrail.get(bcCount);
			bcTrail.remove(bcCount);
		}
		
		for (int i = 0; i < myAppListeners.size(); i++)
		{
			if (myAppListeners.get(i).canBeRemoved(temp))
			{
				toBeRemoved.add(myAppListeners.get(i));
			}
			else
			{
				myAppListeners.get(i).onBreadCrumbRemoved(temp);
			}
		}
	}
	public void setContactOutput(String jsonOutput) { contactOutput = jsonOutput; }
	
	public String getContactOutput() { return contactOutput; }
	
	public ArrayList<String> getTrail()	{ return (ArrayList<String>) bcTrail; }
	
	public void setMapQuery(String mapQuery) { this.mapQuery = mapQuery; }
	
	public String getMapQuery() { return mapQuery; }
	
	public Router getRouter() {	return router; }
	
	public void setRouter(Router router) { this.router = router; }
}
