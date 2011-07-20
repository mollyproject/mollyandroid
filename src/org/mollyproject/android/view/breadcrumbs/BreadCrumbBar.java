package org.mollyproject.android.view.breadcrumbs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mollyproject.android.MyAppListener;
import org.mollyproject.android.MyApplication;
import org.mollyproject.android.R;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.pages.Page;
import org.mollyproject.android.view.pages.ResultsPage;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class BreadCrumbBar extends View implements MyAppListener {
	//protected LinkedList<BreadCrumbFragment> trail;
	protected List<String> trail; //trail of breadcrumbs represented as viewnames
	protected Button[] breadCrumbButtons;
	protected int bcCount;
	protected LinearLayout bar;
	//protected MyApplication myApp;
	protected Page page;
	
	public BreadCrumbBar(Page page) {
		super(page.getApplicationContext());
		this.page = page;
		((MyApplication) page.getApplication()).addListener(this);
		breadCrumbButtons = new Button[4];
		bcCount = 0;
		bar = new LinearLayout(page.getApplicationContext());
		trail = new ArrayList<String>();
		for (int i = 0; i < 4; i++)
		{
			Button button = new Button(page.getApplicationContext());
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
		
		ArrayList<String> newTrail = ((MyApplication) page.getApplication()).getTrail();
		int i = 0;
		for (final String breadcrumb : newTrail)
		{
			breadCrumbButtons[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent myIntent = new Intent(page.getApplicationContext(), 
							SelectionManager.getPage(breadcrumb).getClass());
					page.startActivity(myIntent);
				}
			});
			System.out.println("BreadCrumb Button added is: "+breadcrumb);
			addBreadCrumb(breadcrumb);
			i++;
		}
	}
	
	//add breadcrumb fragment to end of list
	public void addBreadCrumb(String frag) 
	{
		trail.add(frag);
		int s = trail.size();
		if (s == 1)
		{
			
		}
		else if (s > 1)
		{			
			bar.getChildAt(s-2).setEnabled(true);
			bar.getChildAt(s-2).setVisibility(View.VISIBLE);
			bar.getChildAt(s-1).setEnabled(false);
			bar.getChildAt(s-1).setVisibility(View.VISIBLE);
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
		int s = trail.size();
		if (trail.size() > 0) { trail.remove(trail.size()-1); }
		bar.getChildAt(s-1).setEnabled(false);
		bar.getChildAt(s-1).setVisibility(View.INVISIBLE);
		if (s > 1)
		{
			bar.getChildAt(s - 2).setEnabled(false);
		}
		else if (s == 3)
		{
			bar.getChildAt(s).setVisibility(View.INVISIBLE);
		}
		else if ((s < 3)&(s > 0))
		{

		}
	}
	
	public Button[] getButtons()
	{
		return breadCrumbButtons;
	}
	
	/*	When a breadcrumb is added, all non-destroyed Activities will be notified,
	 * 	but only the one in front which is created from scratch needs to be updated
	 * 	
	 * (non-Javadoc)
	 * @see org.mollyproject.android.MyAppListener#onBreadCrumbAdded(java.lang.String)
	 */
	@Override
	public void onBreadCrumbAdded(String breadcrumb) {
		//if (trail.size() == 0)
		//{
			reconstructBar();
		//}
	}

	@Override
	public void onBreadCrumbRemoved(String breadcrumb) {
		System.out.println("Removed breadcrumb is "+breadcrumb);
		if (trail.size() > 0)
		{
			if (breadcrumb == trail.get(trail.size()-1))
			{
				removeBreadCrumb();
			}
		}
	}

}
