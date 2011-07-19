package org.mollyproject.android.view.pages;

import java.util.Random;

import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.breadcrumbs.BreadCrumbBar;

import android.os.Bundle;
import android.view.ViewGroup;

public class ResultsPage extends ContentPage {
	public static final Page INSTANCE = new ResultsPage();
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		myApp.addBreadCrumb(SelectionManager.getName(INSTANCE));
				
		addContentView(bcBar.getBar(), new ViewGroup.LayoutParams
				(getWindowManager().getDefaultDisplay().getWidth(),
				getWindowManager().getDefaultDisplay().getHeight()/10));
		Random random = new Random();
		int i = random.nextInt();
	}
}
