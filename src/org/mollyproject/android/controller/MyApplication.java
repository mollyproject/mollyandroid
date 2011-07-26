package org.mollyproject.android.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import android.app.Application;

public class MyApplication extends Application {
	protected List<String> bcTrail;
	protected int bcCount;
	protected Router router;
	protected String mapQuery;
	protected String contactOutput;
	
	public MyApplication() throws Exception
	{
		super();
		bcCount = 0;
		bcTrail = new ArrayList<String>();
	}
	
	public void addBreadCrumb(String breadcrumb)
	{
		if (!bcTrail.contains(breadcrumb))
		{
			bcTrail.add(breadcrumb);
			bcCount++;
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
	public void setContactOutput(String jsonOutput) { contactOutput = jsonOutput; }
	
	public String getContactOutput() { return contactOutput; }
	
	public ArrayList<String> getTrail()	{ return (ArrayList<String>) bcTrail; }
	
	public void setMapQuery(String mapQuery) { this.mapQuery = mapQuery; }
	
	public String getMapQuery() { return mapQuery; }
	
	public Router getRouter() {	return router; }
	
	public void setRouter(Router router) { this.router = router; }
}
