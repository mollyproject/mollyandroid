package org.mollyproject.android.view.pages;

import org.mollyproject.android.view.breadcrumbs.BreadCrumbBar;

import android.app.Activity;
import android.os.Bundle;

public abstract class Page extends Activity {
	protected BreadCrumbBar bcBar;
	//public abstract void refresh();
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
	}
}
