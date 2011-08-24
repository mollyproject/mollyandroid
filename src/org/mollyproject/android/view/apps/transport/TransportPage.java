package org.mollyproject.android.view.apps.transport;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TransportPage extends ContentPage {
	public static TabHost tabHost;
	public static boolean firstLoad;
	public static boolean transportPaused;
	public static LocalActivityManager mlam;
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
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (!firstLoad)
				{
					mlam.dispatchPause(false);
					if (tabId.equals("bus"))
					{
						parentBreadcrumb.setText("Bus stops");
					}
					else if (tabId.equals("train"))
					{
						parentBreadcrumb.setText("Train services");
					}
					mlam.dispatchResume();
				}
			}
		});
        transportPaused = false;
        firstLoad = true;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mlam.dispatchStop();
	}
	@Override
	protected void onPause() {
		super.onPause();
		
		/*SharedPreferences settings = getSharedPreferences(MyApplication.PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString("lastTab", tabHost.getCurrentTabTag());
	    editor.commit();*/
		transportPaused = true;
		mlam.dispatchPause(isFinishing());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		transportPaused = true;
		tabHost.setCurrentTab(myApp.getLastTransportTab());
		//The first page loaded is always the transport:public-transport page
		//if (!jsonProcessed || myApp.getTransportCache() == null)
		//{
		new TransportPageTask(this, false, true).execute();
		//}
		//mlam.dispatchResume();
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
