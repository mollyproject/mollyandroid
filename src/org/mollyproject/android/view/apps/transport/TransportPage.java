package org.mollyproject.android.view.apps.transport;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class TransportPage extends ContentPage {

	protected LocalActivityManager mlam;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        mlam = new LocalActivityManager(this, false);
        //TabHost tabHost = (TabHost) layoutInflater.inflate(R.layout.transport_tabs, contentLayout, false);
        LinearLayout tabHostLayout = (LinearLayout) layoutInflater.inflate
        				(R.layout.transport_tabs, contentLayout, false);
        contentLayout.addView(tabHostLayout);
        //setContentView(tabHostLayout);
        mlam.dispatchCreate(savedInstanceState);
        TabHost tabHost = (TabHost) tabHostLayout.findViewById(R.id.tabHost);
        tabHost.setup(mlam);
        TabHost.TabSpec spec;
        Intent myIntent;
        Resources res = getResources();
        
        myIntent = new Intent().setClass(this, BusPage.class);
	    spec = tabHost.newTabSpec("Items")
	    	.setIndicator("Items", res.getDrawable(R.drawable.android_button)).setContent(myIntent);
	    tabHost.addTab(spec);
	    
	    /*intent = new Intent().setClass(this, Show2.class);
	    spec = tabHost.newTabSpec("Users")
	    	.setIndicator("Users",res.getDrawable(R.drawable.user32_ldpi)).setContent(intent);
	    tabHost.addTab(spec);*/
	}
	
	public LocalActivityManager getLAM()
	{
		return mlam;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mlam.dispatchPause(isFinishing());
	}
	
	@Override
	public void onResume() {
		loaded = false;
		jsonProcessed = false;
		jsonDownloaded = false;
		super.onResume();
		new TransportPageTask(this, false, true).execute();
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
	public Page getInstance() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return MollyModule.TRANSPORT_PAGE;
	}

}
