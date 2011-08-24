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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TransportPage extends ContentPage {
	public static TabHost transportTabHost;
	public static boolean firstLoad;
	public static boolean transportPaused;
	public static LocalActivityManager mlam;
	
	public static Button transportPageTitle;
	
	public final static DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
	public final static String RAIL = "rail";
	public final static String BUS = "bus";
	public static String defaultTransport = BUS; //Assume bus exists, there is no way round
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        mlam = new LocalActivityManager(this, false);
        LinearLayout tabHostLayout = (LinearLayout) layoutInflater.inflate
        				(R.layout.transport_tabs, contentLayout, false);
        contentLayout.addView(tabHostLayout);
        mlam.dispatchCreate(savedInstanceState);
        transportTabHost = (TabHost) tabHostLayout.findViewById(R.id.tabHost);
        transportTabHost.setup(mlam);
        transportPageTitle = parentBreadcrumb;
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
						
						if (TrainPage.board.equals(TrainPage.DEPARTURES))
						{
							parentBreadcrumb.setText("Rail Departures");
						}
						else if (TrainPage.board.equals(TrainPage.ARRIVALS))
						{
							parentBreadcrumb.setText("Rail Arrivals");
						}
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
		
	    editor.putString("lastTab", transportTabHost.getCurrentTabTag());
	    editor.commit();
	    
		transportPaused = true;
		mlam.dispatchPause(isFinishing());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		System.out.println(settings.getString("lastTab", "bus"));
		transportPaused = true;
		new TransportPageTask(this, false, true).execute();
	}
	
	@Override
	public String getQuery() throws UnsupportedEncodingException {
		//special case for trains:
		if (settings.getString("lastTab",defaultTransport).equals(RAIL))
		{
			return "&board=" + settings.getString("lastBoard", TrainPage.DEPARTURES);
		}
		return null;
	}

	@Override
	public String getAdditionalParams() {
		return "&arg=" + settings.getString("lastTab",defaultTransport);
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
