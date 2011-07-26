package org.mollyproject.android.view.apps;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class ContentPage extends Page {
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		contentLayout = new LinearLayout(this);
		contentLayout.setOrientation(LinearLayout.VERTICAL);
				
		contentLayout.addView(bcBar.getBar());
		router = ((MyApplication) getApplication()).getRouter();
		contentLayout.setBackgroundResource(R.drawable.bg_blue);
		setContentView(contentLayout);
	}
	
	//it resumes the next page first, then stops the previous page,
	//unnecessary complications
	@Override
	public void onStop()
	{
		super.onStop();
		System.out.println(SelectionManager.getName(getInstance().getClass())+"onStop");
		myApp.removeBreadCrumb(SelectionManager.getName(getInstance().getClass()));
		//myApp.removeListener(bcBar);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		System.out.println(SelectionManager.getName(getInstance().getClass())+"onResume");
		myApp.addBreadCrumb(SelectionManager.getName(getInstance().getClass()));
		System.out.println("Reconstructing");
		bcBar.reconstruct();
	}
}
