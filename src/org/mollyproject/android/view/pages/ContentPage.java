package org.mollyproject.android.view.pages;

import org.mollyproject.android.selection.SelectionManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class ContentPage extends Page {
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		contentLayout = new LinearLayout(this);
		contentLayout.setOrientation(LinearLayout.VERTICAL);
				
		contentLayout.addView(bcBar.getBar(), new ViewGroup.LayoutParams
				(getWindowManager().getDefaultDisplay().getWidth(),
				getWindowManager().getDefaultDisplay().getHeight()/10));
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		System.out.println("OnStop reached");
		myApp.removeBreadCrumb();
		myApp.removeListener(bcBar);
	}
	
	@Override
	public void onResume()
	{
		super.onRestart();
		System.out.println("New Restart");
		myApp.addListener(bcBar);
		myApp.addBreadCrumb(SelectionManager.getName(this));
	}
}
