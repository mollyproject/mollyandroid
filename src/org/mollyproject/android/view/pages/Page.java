package org.mollyproject.android.view.pages;

import java.util.ArrayList;

import org.json.JSONObject;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.breadcrumbs.BreadCrumbBar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public abstract class Page extends Activity {
	protected BreadCrumbBar bcBar;
	protected MyApplication myApp;
	protected ArrayList<String> trail;
	protected JSONObject jsonContent;
	protected String jsonText;
	protected LinearLayout contentLayout;
	protected Router router;
	//public abstract void refresh();
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		myApp = (MyApplication) getApplication();
		bcBar = new BreadCrumbBar(getInstance());
		router = myApp.getRouter();
	}
	
	public abstract Page getInstance();
}
