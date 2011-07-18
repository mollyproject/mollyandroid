package org.mollyproject.android.view.breadcrumbs;

import java.util.ArrayList;

import org.mollyproject.android.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ImprovedBreadCrumbBar extends View {

		//To-do: design a breadcrumb bar that builds itself upon being generated
	
		protected ArrayList<String> trail; //trail of breadcrumbs represented as viewnames
		protected Button[] breadCrumbButtons;
		protected int bcCount;
		protected LinearLayout bar;
		
		public ImprovedBreadCrumbBar(Context context) {
			super(context);
			breadCrumbButtons = new Button[4];
			bcCount = 0;
			bar = new LinearLayout(context);
			for (int i = 0; i < 4; i++)
			{
				Button button = new Button(context);
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
		
		//add breadcrumb fragment to end of list
		public void addBreadCrumb(String frag) 
		{
			trail.add(frag);
			bcCount++;
			System.out.println(bcCount);
			if (bcCount < 4)
			{			
				bar.getChildAt(bcCount-1).setEnabled(true);
				bar.getChildAt(bcCount).setVisibility(View.VISIBLE);
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
			if (trail.size() > 0) trail.remove(trail.size());
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
}
