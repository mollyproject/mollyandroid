package org.mollyproject.android.view.apps.search;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.app.LocalActivityManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class NewSearchPage extends ContentPage {
	protected String[] generalQuery;
	public static TabHost searchTabHost;
	//public static LocalActivityManager mlam;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.generalQuery = MyApplication.generalQuery;
		
		//mlam = new LocalActivityManager(this, false);
		
		LinearLayout tabHostLayout = (LinearLayout) getLayoutInflater().inflate
				(R.layout.app_browser_tab, contentLayout, false);
		contentLayout.addView(tabHostLayout);
		
		System.out.println(tabHostLayout.getChildAt(0));
		searchTabHost = (TabHost) tabHostLayout.getChildAt(0);
		//searchTabHost.setup(mlam);
		
		//create sub pages in the tab
        //mlam.dispatchCreate(savedInstanceState);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		//mlam.dispatchStop(); //stop the sub pages in tab
	}
	@Override
	protected void onPause() {
		super.onPause();
		//mlam.dispatchPause(isFinishing());
	}
	
	@Override
	public void onResume() {
		super.onResume();
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
