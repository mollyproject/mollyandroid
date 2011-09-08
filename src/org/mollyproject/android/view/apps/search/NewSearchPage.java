package org.mollyproject.android.view.apps.search;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class NewSearchPage extends ContentPage {
	protected String[] generalQuery;
	public static TabHost searchTabHost;
	protected int currentTabId;
	//public static LocalActivityManager mlam;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.generalQuery = MyApplication.generalQuery;
		
		LinearLayout tabHostLayout = (LinearLayout) getLayoutInflater().inflate
				(R.layout.app_browser_tab, contentLayout, false);
		contentLayout.addView(tabHostLayout);
		
		System.out.println(tabHostLayout.getChildAt(0));
		searchTabHost = (TabHost) tabHostLayout.getChildAt(0);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		currentTabId = searchTabHost.getCurrentTab();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (jsonProcessed)
		{
			searchTabHost.setCurrentTab(currentTabId);
		}
	}
	
	@Override
	public void refresh() {
		new NewSearchTask(this, false, true).execute();
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getQuery() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAdditionalParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return MollyModule.SEARCH_PAGE;
	}

}
