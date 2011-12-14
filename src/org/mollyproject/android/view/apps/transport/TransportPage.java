package org.mollyproject.android.view.apps.transport;

import java.io.UnsupportedEncodingException;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.transport.train.TrainPage;

import android.app.LocalActivityManager;
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
	
	public final static String RAIL = "rail";
	public final static String BUS = "bus";
	public final static String PAR = "park-and-ride";
	public static String defaultTransport = BUS; //Assume bus exists, there is no way round
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        transportPageTitle = parentBreadcrumb;
        
        mlam = new LocalActivityManager(this, false);
        LinearLayout tabHostLayout = (LinearLayout) getLayoutInflater().inflate
        				(R.layout.transport_tabs, contentLayout, false);
        contentLayout.addView(tabHostLayout);
        //set up the tab host, it has no content yet
        transportTabHost = (TabHost) tabHostLayout.findViewById(R.id.tabHost);
        transportTabHost.setup(mlam);
        transportTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				//stop the current thread (
				if (!firstLoad)
				{
					//change the page title and view_name so that the right page can be loaded afterwards
					mlam.dispatchPause(false);
					if (tabId.equals(BUS))
					{
						name = MollyModule.PUBLIC_TRANSPORT;
						parentBreadcrumb.setText("Bus stops");
					}
					else if (tabId.equals(RAIL))
					{
						name = MollyModule.PUBLIC_TRANSPORT;
						if (TrainPage.board.equals(TrainPage.DEPARTURES))
						{
							parentBreadcrumb.setText("Rail Departures");
						}
						else if (TrainPage.board.equals(TrainPage.ARRIVALS))
						{
							parentBreadcrumb.setText("Rail Arrivals");
						}
					}
					else if (tabId.equals(PAR))
					{
						name = MollyModule.PARK_AND_RIDE;
						parentBreadcrumb.setText("Park and Rides");
					}
					mlam.dispatchResume();
				}
			}
		});
        transportPaused = false;
        firstLoad = true;
        //create sub pages in the tab
        mlam.dispatchCreate(savedInstanceState);
	}
	@Override
	public void onResume() {
		name = settings.getString("name", MollyModule.PUBLIC_TRANSPORT);
		System.out.println("Name " + name);
		//only public transport requires the additional arguments
		if (name.equals(MollyModule.PUBLIC_TRANSPORT))
		{
			additionalArgs = "&arg=" + settings.getString("lastTab",defaultTransport);
		}
		else
		{
			additionalArgs = null;
		}
		super.onResume();
		//get the last name stored, if it is not there, the default is public transport 
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mlam.dispatchStop(); //stop the sub pages in tab
	}
	@Override
	protected void onPause() {
		super.onPause();
		//once the page is paused, remember the last tab left open
		MyApplication.lastTransportTag = transportTabHost.getCurrentTabTag();
	    editor.putString("lastTab", transportTabHost.getCurrentTabTag());
	    editor.putString("name", name);
	    editor.commit();
	    
		transportPaused = true;
		mlam.dispatchPause(isFinishing());
	}
	
	@Override
	public void refresh() {
		transportPaused = true;
		new TransportPageTask(this, false, true).execute();
	}
	
	@Override
	public String getQuery() throws UnsupportedEncodingException {
		//special case for trains:
		if (settings.getString("lastTab",defaultTransport).equals(RAIL) & name.equals(MollyModule.PUBLIC_TRANSPORT))
		{
			return "&board=" + settings.getString("lastBoard", TrainPage.DEPARTURES);
		}
		return null;
	}

	@Override
	public Page getInstance() {
		return this;
	}

}
