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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TransportPage extends ContentPage {
	public static TabHost transportTabHost;
	public static boolean firstLoad;
	public static boolean transportPaused;
	public static LocalActivityManager mlam;
	protected SharedPreferences settings;
	public final static DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
	public final static String RAIL = "rail";
	public final static String BUS = "bus";
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        mlam = new LocalActivityManager(this, false);
        LinearLayout tabHostLayout = (LinearLayout) layoutInflater.inflate
        				(R.layout.transport_tabs, contentLayout, false);
        contentLayout.addView(tabHostLayout);
        mlam.dispatchCreate(savedInstanceState);
        settings = getSharedPreferences(MyApplication.PREFS_NAME, 0);
        transportTabHost = (TabHost) tabHostLayout.findViewById(R.id.tabHost);
        transportTabHost.setup(mlam);
        transportTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (!firstLoad)
				{
					mlam.dispatchPause(false);
					if (tabId.equals(BUS))
					{
						parentBreadcrumb.setText("Bus stops");
					}
					else if (tabId.equals(RAIL))
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
		
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString("lastTab", transportTabHost.getCurrentTabTag());
	    editor.commit();
	    
		transportPaused = true;
		mlam.dispatchPause(isFinishing());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		transportPaused = true;
		new TransportPageTask(this, false, true).execute();
	}
	
	@Override
	public String getQuery() throws UnsupportedEncodingException {
		return null;
	}

	@Override
	public String getAdditionalParams() {
		return "&arg=" + settings.getString("lastTab", BUS);
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
