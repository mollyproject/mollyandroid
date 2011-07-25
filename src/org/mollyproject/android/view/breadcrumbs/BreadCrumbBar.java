package org.mollyproject.android.view.breadcrumbs;

import java.util.ArrayList;
import java.util.List;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MyAppListener;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.pages.Page;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BreadCrumbBar extends View implements MyAppListener {
	protected List<String> trail; //trail of breadcrumbs represented as viewnames
	protected Button[] breadCrumbButtons;
	protected int bcCount;
	protected LinearLayout bar;
	protected boolean toBeRemoved;
	protected TextView label;
	protected Page page;
	
	public BreadCrumbBar(Page page) {
		super(page.getApplicationContext());
		this.page = page;
		((MyApplication) page.getApplication()).addListener(this);
		breadCrumbButtons = new Button[4];
		bcCount = 0;
		bar = new LinearLayout(page.getApplicationContext());
		trail = new ArrayList<String>();
		toBeRemoved = false;
		for (int i = 0; i < 4; i++)
		{
			Button button = new Button(page.getApplicationContext());
			//button.setBackgroundResource(R.drawable.android_button);
			breadCrumbButtons[i] = button;
			button.setBackgroundColor(R.color.blue);
			bar.addView(button);
//			bar.setBackgroundColor(R.color.blue);
			button.setVisibility(View.INVISIBLE);
		}
		
		label = new TextView(page.getApplicationContext());
		bar.addView(label);
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
		System.out.println("reconstructed");
		
		ArrayList<String> newTrail = ((MyApplication) page.getApplication()).getTrail();
		
		for (int i = 0; i < newTrail.size(); i++)
		{
			final String breadcrumb = newTrail.get(i);
			breadCrumbButtons[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent myIntent = new Intent(page.getApplicationContext(), 
							SelectionManager.getPageClass(breadcrumb));
					page.startActivity(myIntent);
				}
			});
			breadCrumbButtons[i].setBackgroundColor(R.color.blue);
			breadCrumbButtons[i].setBackgroundResource(
					SelectionManager.getBCImg(newTrail.get(i)));
			addBreadCrumb(newTrail.get(i));
		}
		
		label.setText(trail.get(trail.size()-1));
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
		System.out.println("Label should now be "+trail.get(trail.size()-1));
		label.setText(trail.get(trail.size()-1)); //set label to the corresponding breadcrumb
	}
	
	//remove the last breadcrumb fragment, assuming the trail is never
	//removed if only one element is left
	public void removeBreadCrumb()
	{
		int s = trail.size();
		if (trail.size() > 0) 
		{ 
			trail.remove(s-1);  
		}
		
		bar.getChildAt(s-1).setEnabled(false);
		bar.getChildAt(s-1).setVisibility(View.INVISIBLE);
		if (s > 1)
		{
			System.out.println("Label should now be "+trail.get(trail.size()-1));
			label.setText(trail.get(trail.size()-1)); //set label to the corresponding breadcrumb
			bar.getChildAt(s - 2).setEnabled(false);
			if (s == 2)
			{
				bar.getChildAt(s-2).setVisibility(View.INVISIBLE);
			}
		}
		
		if (s == 3)
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
		System.out.println("This breadcrumb bar belongs to "
								+ SelectionManager.getName(page.getClass()));
		System.out.println("Removed breadcrumb is "+breadcrumb);
		
		if (trail.size() > 0)
		{
			if (breadcrumb == trail.get(trail.size()-1))
			{
				removeBreadCrumb();
			}
		}
	}

	@Override
	public boolean canBeRemoved(String breadcrumb) {
		return ((page == null) || 
				(page.getClass() == SelectionManager.getPageClass(breadcrumb)));
	}
	
	@Override
	public Class<? extends Page> getOwnerClass()
	{
		return page.getClass();
	}
	
	public void setText(String labelText)
	{
		label.setText(labelText);
	}

}
