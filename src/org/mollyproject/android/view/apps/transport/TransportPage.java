package org.mollyproject.android.view.apps.transport;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
	protected TabHost tabHost;
	protected LocalActivityManager mlam;
	public final static DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
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
        tabHost = (TabHost) tabHostLayout.findViewById(R.id.tabHost);
        tabHost.setup(mlam);
        TabHost.TabSpec spec;
        Intent myIntent;
        Resources res = getResources();
        
        myIntent = new Intent().setClass(this, BusPage.class);
	    spec = tabHost.newTabSpec("Bus")
	    	.setIndicator("Bus", res.getDrawable(R.drawable.android_button)).setContent(myIntent);
	    tabHost.addTab(spec);

	    myIntent = new Intent().setClass(this, TrainPage.class);
	    spec = tabHost.newTabSpec("Train")
	    	.setIndicator("Train", res.getDrawable(R.drawable.android_button)).setContent(myIntent);
	    tabHost.addTab(spec);
	}
	
	public LocalActivityManager getLAM()
	{
		return mlam;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		myApp.setLastTransportTab(tabHost.getCurrentTab());
		mlam.dispatchPause(isFinishing());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		tabHost.setCurrentTab(myApp.getLastTransportTab());
		//The first page loaded is always the transport:public-transport page
		if (!jsonProcessed & myApp.getTransportCache() == null)
		{
			new TransportPageTask(this, false, true).execute();
		}
		else if (myApp.getTransportCache() != null)
		{
			mlam.dispatchResume();
		}
	}
	
	@Override
	public String getQuery() throws UnsupportedEncodingException {
		return null;
	}

	@Override
	public String getAdditionalParams() {
		return "&arg=bus";
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getName() {
		return MollyModule.PUBLIC_TRANSPORT;
	}

}
