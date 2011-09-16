package org.mollyproject.android.view.apps.search;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class NewSearchPage extends ContentPage {
	protected String[] generalQuery;
	public static TabHost searchTabHost;
	protected int currentTabId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.SEARCH_PAGE;
		this.generalQuery = MyApplication.generalQuery;
		
		LinearLayout tabHostLayout = (LinearLayout) getLayoutInflater().inflate
				(R.layout.app_browser_tab, contentLayout, false);
		contentLayout.addView(tabHostLayout);
		
		System.out.println(tabHostLayout.getChildAt(0));
		searchTabHost = (TabHost) tabHostLayout.getChildAt(0);
		
		extraTextView.setText("Search");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		currentTabId = searchTabHost.getCurrentTab();
	}
	
	@Override
	public void onResume() {
		//No need to download any json, but still process the json given by MyApplication.availableApps
		loaded = true;
		jsonProcessed = false;
		super.onResume();
		searchTabHost.setCurrentTab(currentTabId); // jump to the last tab
	}
	
	@Override
	public void refresh() {
		new NewSearchTask(this, true, true).execute(); //destroy the page if this task fails (i.e. the list of available apps is null)
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
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

}
