package org.mollyproject.android.view.apps.transport;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.Page;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.LinearLayout;

public class TrainPage extends AutoRefreshPage {
	
	public static TrainPageRefreshTask trainPageRefreshTask;
	
	@Override
	public void onResume() {
		super.onResume();
		if (TransportPage.tabHost.getCurrentTabTag().equals("train"))
		{
			System.out.println("Train Page resumed");
			if (trainPageRefreshTask != null) 
			{
				trainPageRefreshTask.cancel(true);
			}

			trainPageRefreshTask = new TrainPageRefreshTask(this, false, false);
			trainPageRefreshTask.execute();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (!TransportPage.tabHost.getCurrentTabTag().equals("train") || TransportPage.transportPaused)
		{
			System.out.println("Train Page paused");
			trainPageRefreshTask.cancel(true);
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
		return "&arg=rail";
	}
}
