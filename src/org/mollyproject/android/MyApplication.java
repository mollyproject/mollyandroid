package org.mollyproject.android;

import java.util.ArrayList;
import java.util.List;

import org.mollyproject.android.controller.Router;

import android.app.Application;

public class MyApplication extends Application {
	protected List<String> bcTrail;
	protected List<MyAppListener> myAppListeners;
	protected int bcCount;
	protected Router router;
	
	public MyApplication() throws Exception
	{
		super();
		bcCount = 0;
		bcTrail = new ArrayList<String>();
		myAppListeners = new ArrayList<MyAppListener>();
	}
	
	public void addListener(MyAppListener l)
	{
		myAppListeners.add(l);
	}
	
	public void removeListener(MyAppListener l)
	{
		myAppListeners.remove(l);
	}
	
	public void addBreadCrumb(String breadcrumb)
	{
		bcTrail.add(breadcrumb);
		bcCount++;
		System.out.println("Breadcrumb added: "+breadcrumb);
		
		for (MyAppListener l: myAppListeners)
		{
			l.onBreadCrumbAdded(breadcrumb);			
		}
	}
	
	public void removeBreadCrumb()
	{		
		String temp = new String();
		if (bcCount > 0)
		{
			bcCount--;
			temp = bcTrail.get(bcCount);
			bcTrail.remove(bcCount);
		}
		
		for (MyAppListener l: myAppListeners)
		{
			l.onBreadCrumbRemoved(temp);			
		}
	}
	
	public ArrayList<String> getTrail()
	{
		return (ArrayList<String>) bcTrail;
	}
	
	public void setRouter(Router router)
	{
		this.router = router;
	}
}
