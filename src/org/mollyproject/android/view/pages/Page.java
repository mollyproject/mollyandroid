package org.mollyproject.android.view.pages;

import java.util.ArrayList;

import org.json.JSONObject;
import org.mollyproject.android.MyApplication;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.breadcrumbs.BreadCrumbBar;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public abstract class Page extends Activity {
	protected BreadCrumbBar bcBar;
	protected MyApplication myApp;
	protected ArrayList<String> trail;
	protected JSONObject jsonContent;
	//public abstract void refresh();
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		myApp = (MyApplication) getApplication();
		bcBar = new BreadCrumbBar(myApp);
	}
	
	public void updateContent()
	{
		String locator = SelectionManager.getName(this);
		
	}
}
