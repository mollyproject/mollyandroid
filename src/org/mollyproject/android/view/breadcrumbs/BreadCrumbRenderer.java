package org.mollyproject.android.view.breadcrumbs;

import android.view.View;
import android.widget.LinearLayout;

public class BreadCrumbRenderer implements BreadCrumbsListener {
	protected BreadCrumbBar bcBar;
	protected int bcCount;
	protected LinearLayout layout;
	public BreadCrumbRenderer(BreadCrumbBar bcBar, LinearLayout layout)
	{
		this.bcBar = bcBar;
		this.layout = layout;
		bcBar.addBreadCrumbsListener(this);
		bcCount = 0;
		System.out.println("BCRen "+ layout.getChildCount());
	}
	@Override
	public void onBreadCrumbAdded(BreadCrumbFragment bcFrags) {
		bcCount++;
		System.out.println(bcCount);
		if (bcCount < 4)
		{			
			layout.getChildAt(bcCount-1).setEnabled(true);
			layout.getChildAt(bcCount).setVisibility(View.VISIBLE);
			layout.getChildAt(bcCount).setEnabled(false);
		}
		else
		{
			//Do something with the button2, most likely to render
			//a new background
			//layout.getChildAt(2).setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onBreadCrumbRemoved() {		
		if (bcCount == 3)
		{
			layout.getChildAt(bcCount).setEnabled(true);
			layout.getChildAt(2).setVisibility(View.VISIBLE);
			layout.getChildAt(bcCount).setVisibility(View.INVISIBLE);
		}
		else if ((bcCount < 3)&(bcCount > 0))
		{
			layout.getChildAt(bcCount).setEnabled(true);
			layout.getChildAt(bcCount).setVisibility(View.INVISIBLE);
		}
		if (bcCount > 0) { bcCount--; }		
		System.out.println(bcCount);
	}
	
}
