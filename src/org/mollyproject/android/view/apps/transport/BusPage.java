package org.mollyproject.android.view.apps.transport;

import java.io.UnsupportedEncodingException;
import org.mollyproject.android.view.apps.Page;

import android.widget.LinearLayout;
import android.widget.Toast;

public class BusPage extends AutoRefreshPage{
	
	public static BusPageRefreshTask busPageRefreshTask;
	
	@Override
	public void onResume() {
		super.onResume();
		if (TransportPage.transportTabHost.getCurrentTabTag().equals(TransportPage.BUS))
		{
			if (busPageRefreshTask != null) 
			{
				busPageRefreshTask.cancel(true);
			}

			if (manualRefresh)
			{
				manualRefresh = false;
				Toast toast = Toast.makeText(this, "Please wait. This page might take a moment or two to refresh...", 
						Toast.LENGTH_SHORT);
				toast.show();
			}
			
			busPageRefreshTask = new BusPageRefreshTask(this, false, false);
			busPageRefreshTask.execute();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (!TransportPage.transportTabHost.getCurrentTabTag().equals(TransportPage.BUS) 
				|| TransportPage.transportPaused)
		{
			System.out.println("Bus Page paused");
			if (busPageRefreshTask != null) 
			{
				busPageRefreshTask.cancel(true);
			}
		}
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
		return "&arg=bus";
	}

	@Override
	public void setContentLayout(LinearLayout contentLayout) {
		// TODO Auto-generated method stub
		
	}
}











