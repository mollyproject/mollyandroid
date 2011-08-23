package org.mollyproject.android.view.apps.transport;

import java.io.UnsupportedEncodingException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.Page;

import roboguice.inject.InjectView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

public class BusPage extends AutoRefreshPage{
	
	public static BusPageRefreshTask busPageRefreshTask;
	
	@Override
	public void onResume() {
		super.onResume();
		if (TransportPage.tabHost.getCurrentTabTag().equals("bus"))
		{
			System.out.println("Bus Page resumed");
			busPageRefreshTask = new BusPageRefreshTask(this, false, false);
			busPageRefreshTask.execute();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (!TransportPage.tabHost.getCurrentTabTag().equals("bus"))
		{
			System.out.println("Bus Page paused");
			busPageRefreshTask.cancel(true);
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











