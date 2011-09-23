package org.mollyproject.android.view.apps.transport.bus;

import java.io.UnsupportedEncodingException;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.transport.AutoRefreshPage;
import org.mollyproject.android.view.apps.transport.TransportPage;

import android.widget.LinearLayout;
import android.widget.Toast;

public class BusPage extends AutoRefreshPage{
	
	public static BusPageRefreshTask busPageRefreshTask;
	
	@Override
	public void refresh() {
		if (TransportPage.transportTabHost.getCurrentTabTag().equals(TransportPage.BUS))
		{
			if (busPageRefreshTask != null) 
			{
				busPageRefreshTask.cancel(true);
			}

			Toast.makeText(this, "Please wait. This page might take a moment or two to refresh...", 
					Toast.LENGTH_SHORT).show();
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











