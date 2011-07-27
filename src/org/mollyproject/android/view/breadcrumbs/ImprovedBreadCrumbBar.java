package org.mollyproject.android.view.breadcrumbs;

import java.util.LinkedList;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImprovedBreadCrumbBar extends View {
	protected Button[] breadCrumbButtons;
	protected int bcCount;
	protected LinearLayout bar;
	protected TextView label;
	protected Page page;
	
	public ImprovedBreadCrumbBar(Page page)
	{
		super(page.getApplicationContext());
		this.page = page;
		
		breadCrumbButtons = new Button[4];
		bcCount = 0;
		bar = new LinearLayout(page.getApplicationContext());
		for (int i = 0; i < 4; i++)
		{
			Button button = new Button(page.getApplicationContext());
			//button.setBackgroundResource(R.drawable.android_button);
			breadCrumbButtons[i] = button;
			button.setBackgroundColor(R.color.blue);
			bar.addView(button);
			//bar.setBackgroundColor(R.color.blue);
			button.setVisibility(View.INVISIBLE);
		}
	}
	
	public LinearLayout getBar()
	{
		return bar;
	}
	
	public void reconstruct()
	{
		//reset everything first
		for (Button button : breadCrumbButtons)
		{
			button.setVisibility(View.INVISIBLE);
			button.setEnabled(false);
		}
		
		LinkedList<String> newTrail = ((MyApplication) page.getApplication()).getTrail();
		
		if (newTrail.size() < 4)
		{
			//Nice and easy case, display each breadcrumb as they appear
			for (int i = 0; i < newTrail.size(); i++)
			{
				final String breadcrumb = newTrail.get(i);
				breadCrumbButtons[i].setVisibility(View.VISIBLE);
				breadCrumbButtons[i].setEnabled(true);
				breadCrumbButtons[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent myIntent = new Intent(page.getApplicationContext(), 
								SelectionManager.getPageClass(breadcrumb));
						page.startActivity(myIntent);
					}
				});
				breadCrumbButtons[i].setBackgroundResource(
						SelectionManager.getBCImg(newTrail.get(i)));
			}
			breadCrumbButtons[newTrail.size()-1].setEnabled(false);
		}
		else
		{
			//the more complicated case
			for (int i = 0; i < newTrail.size(); i++)
			{
				breadCrumbButtons[i].setVisibility(View.VISIBLE);
				breadCrumbButtons[i].setEnabled(true);
				if ((i == 0) || (i == 1))
				{
					//same as the <4 case
					final String breadcrumb = newTrail.get(i);
					breadCrumbButtons[i].setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent myIntent = new Intent(page.getApplicationContext(), 
									SelectionManager.getPageClass(breadcrumb));
							page.startActivity(myIntent);
						}
					});
					breadCrumbButtons[i].setBackgroundResource(
							SelectionManager.getBCImg(newTrail.get(i)));
				}
				else if (i == 3)
				{
					//points to trail(lastIndex - 1)
					final String breadcrumb = newTrail.get(newTrail.size()-2);
					breadCrumbButtons[i].setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent myIntent = new Intent(page.getApplicationContext(), 
									SelectionManager.getPageClass(breadcrumb));
							page.startActivity(myIntent);
						}
					});
					breadCrumbButtons[i].setBackgroundResource(R.drawable.android_button);
				}
				else
				{
					//points to last in trail
					final String breadcrumb = newTrail.get(newTrail.size()-1);
					breadCrumbButtons[i].setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent myIntent = new Intent(page.getApplicationContext(), 
									SelectionManager.getPageClass(breadcrumb));
							page.startActivity(myIntent);
						}
					});
					breadCrumbButtons[i].setBackgroundResource(
							SelectionManager.getBCImg(newTrail.get(newTrail.size()-1)));
					breadCrumbButtons[i].setEnabled(false);
				}
			}
		}
	}
}
