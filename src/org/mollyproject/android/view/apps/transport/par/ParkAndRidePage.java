package org.mollyproject.android.view.apps.transport.par;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.transport.AutoRefreshPage;
import org.mollyproject.android.view.apps.transport.TransportPage;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ParkAndRidePage extends AutoRefreshPage{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.PARK_AND_RIDE;
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
	public void refresh() {
		if (TransportPage.transportTabHost.getCurrentTabTag().equals(TransportPage.PAR))
		{
			Toast.makeText(this, "Please wait. This page might take a moment or two to refresh...", 
					Toast.LENGTH_SHORT).show();
			new ParkAndRideRefreshTask(this, false, false).execute();
		}		
	}
	@Override
	public void setContentLayout(LinearLayout contentLayout) {
		// TODO Auto-generated method stub
		
	}

}
