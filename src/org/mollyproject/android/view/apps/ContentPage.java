package org.mollyproject.android.view.apps;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class ContentPage extends Page {
	//aka ImplementedPage
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
	@Override
	public void onResume()
	{
		super.onResume();
		System.out.println(SelectionManager.getName(getInstance().getClass())+" onResume");
		myApp.updateBreadCrumb(SelectionManager.getName(getInstance().getClass()));
		System.out.println("Reconstructing");
		bcBar.reconstruct();
	}
}
