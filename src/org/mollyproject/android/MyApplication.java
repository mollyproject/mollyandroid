package org.mollyproject.android;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mollyproject.android.controller.Router;

import android.app.Application;

public class MyApplication extends Application {
	protected List<String> bcTrail;
	protected List<MyAppListener> myAppListeners;
	protected int bcCount;
	protected Router router;
	protected Set<MyAppListener> toBeRemoved;
	
	public MyApplication() throws Exception
	{
		super();
		bcCount = 0;
		bcTrail = new ArrayList<String>();
		myAppListeners = new ArrayList<MyAppListener>();
		toBeRemoved = new HashSet<MyAppListener>();
	}
	
	public void addListener(MyAppListener l)
	{
		for (MyAppListener oldListener : myAppListeners)
		{
			if (oldListener.getOwnerClass() == l.getOwnerClass())
			{
				toBeRemoved.add(oldListener);
			}
		}
		myAppListeners.add(l);
	}
	
	public void removeListener(MyAppListener l)
	{
		System.out.println(myAppListeners.contains(l));
		System.out.println(myAppListeners.size());
		myAppListeners.remove(l);
		System.out.println(myAppListeners.size());
	}
	
	public void addBreadCrumb(String breadcrumb)
	{
		if (!bcTrail.contains(breadcrumb))
		{
			bcTrail.add(breadcrumb);
			bcCount++;
			
			for (MyAppListener l: myAppListeners)
			{
				l.onBreadCrumbAdded(breadcrumb);			
			}
		}
	}
	
	public void removeBreadCrumb()
	{	
		String temp = new String();
		for (MyAppListener l : toBeRemoved)
		{
			removeListener(l);
		}
		if (bcCount > 0)
		{
			bcCount--;
			temp = bcTrail.get(bcCount);
			bcTrail.remove(bcCount);
		}
		
		for (MyAppListener l: myAppListeners)
		{
			if (l.canBeRemoved(temp))
			{
				toBeRemoved.add(l);
			}
			else
			{
				l.onBreadCrumbRemoved(temp);
			}
		}
	}
	
	public ArrayList<String> getTrail()
	{
		return (ArrayList<String>) bcTrail;
	}
	
	public Router getRouter()
	{
		return router;
	}
	
	public void setRouter(Router router)
	{
		this.router = router;
	}
}
