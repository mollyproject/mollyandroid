package org.mollyproject.android.view.breadcrumbs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import android.content.Context;
import android.view.View;

public class BreadCrumbBar extends View {
	protected LinkedList<BreadCrumbFragment> trail;
	protected List<BreadCrumbsListener> bcListeners;
	
	public BreadCrumbBar(Context context) {
		super(context);
		trail = new LinkedList<BreadCrumbFragment>();
		bcListeners = new ArrayList<BreadCrumbsListener>();
	}
	
	public void addBreadCrumbsListener(BreadCrumbsListener l)
	{
		bcListeners.add(l);
	}
	
	//add breadcrumb fragment to end of list
	public void addBreadCrumb(BreadCrumbFragment frag) 
	{
		trail.addLast(frag);
		for (BreadCrumbsListener l : bcListeners)
		{
			l.onBreadCrumbAdded(frag);
		}
	}
	
	//remove the last breadcrumb fragment
	public void removeBreadCrumb()
	{
		if (trail.size() > 0) { trail.removeLast(); }
		for (BreadCrumbsListener l : bcListeners)
		{
			l.onBreadCrumbRemoved();
		}
	}
}
