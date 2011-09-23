package org.mollyproject.android.view.apps.transport;

import java.io.UnsupportedEncodingException;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

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
	public static String defaultTransport = BUS; //Assume bus exists, there is no way round
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        name = MollyModule.PUBLIC_TRANSPORT; 
        additionalArgs = "&arg=" + settings.getString("lastTab",defaultTransport);
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
        
        //create sub pages in the tab
        mlam.dispatchCreate(savedInstanceState);
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
	    editor.putString("lastTab", transportTabHost.getCurrentTabTag());
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
		if (settings.getString("lastTab",defaultTransport).equals(RAIL))
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
