package org.mollyproject.android.view.breadcrumbs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mollyproject.android.MyAppListener;
import org.mollyproject.android.MyApplication;
import org.mollyproject.android.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class BreadCrumbBar extends View implements MyAppListener {
	//protected LinkedList<BreadCrumbFragment> trail;
	protected List<String> trail; //trail of breadcrumbs represented as viewnames
	protected Button[] breadCrumbButtons;
	protected int bcCount;
	protected LinearLayout bar;
	protected MyApplication myApp;
	
	public BreadCrumbBar(MyApplication myApp) {
		super(myApp.getApplicationContext());
		this.myApp = myApp;
		myApp.addListener(this);
		breadCrumbButtons = new Button[4];
		bcCount = 0;
		bar = new LinearLayout(myApp.getApplicationContext());
		trail = new ArrayList<String>();
		for (int i = 0; i < 4; i++)
		{
			Button button = new Button(myApp.getApplicationContext());
			button.setBackgroundResource(R.drawable.android_button);
			breadCrumbButtons[i] = button;
			bar.addView(button);
			button.setVisibility(View.INVISIBLE);
		}
	}
	
	public LinearLayout getBar()
	{
		return bar;
	}
	
	public void reconstructBar()
	{
		trail.clear();
		
		for (Button button : breadCrumbButtons)
		{
			button.setVisibility(View.INVISIBLE);
			button.setEnabled(false);
		}
		
		ArrayList<String> newTrail = myApp.getTrail();		
		for (String breadcrumb : newTrail)
		{
			addBreadCrumb(breadcrumb);
		}
	}
	
	//add breadcrumb fragment to end of list
	public void addBreadCrumb(String frag) 
	{
		trail.add(frag);
		bcCount++;
		System.out.println(bcCount);
		if (bcCount < 4)
		{			
			bar.getChildAt(bcCount-1).setEnabled(true);
			bar.getChildAt(bcCount-1).setVisibility(View.VISIBLE);
			bar.getChildAt(bcCount).setEnabled(false);
		}
		else
		{
			//Do something with the button2, most likely to render
			//a new background
			//layout.getChildAt(2).setVisibility(View.INVISIBLE);
		}
	}
	
	//remove the last breadcrumb fragment
	public void removeBreadCrumb()
	{
		if (trail.size() > 0) trail.remove(trail.size()-1);
		if (bcCount == 3)
		{
			bar.getChildAt(bcCount).setEnabled(true);
			bar.getChildAt(2).setVisibility(View.VISIBLE);
			bar.getChildAt(bcCount).setVisibility(View.INVISIBLE);
		}
		else if ((bcCount < 3)&(bcCount > 0))
		{
			bar.getChildAt(bcCount).setEnabled(true);
			bar.getChildAt(bcCount).setVisibility(View.INVISIBLE);
		}
		if (bcCount > 0) { bcCount--; }		
		System.out.println(bcCount);

	}
	
	/*	When a breadcrumb is added, all non-destroyed Activities will be notified,
	 * 	but only the one in front which is created from scratch needs to be updated
	 * 	
	 * (non-Javadoc)
	 * @see org.mollyproject.android.MyAppListener#onBreadCrumbAdded(java.lang.String)
	 */
	@Override
	public void onBreadCrumbAdded(String breadcrumb) {
		System.out.println("Home trail size "+trail.size());
		if (trail.size() == 0)
		{
			reconstructBar();
		}
	}

	@Override
	public void onBreadCrumbRemoved(String breadcrumb) {
		// TODO Auto-generated method stub
		if (breadcrumb == trail.get(trail.size()-1))
		{
			removeBreadCrumb();
		}
	}

}
