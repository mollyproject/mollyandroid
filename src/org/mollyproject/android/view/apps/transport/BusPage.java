package org.mollyproject.android.view.apps.transport;

import java.io.UnsupportedEncodingException;
import org.mollyproject.android.view.apps.Page;

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
}











