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
	
	//TimerBusTask timerBusTask = new TimerBusTask(this);
	protected BusPageRefreshTask busRefreshTask;
	
	private Handler mHandler = new Handler();
	
	@InjectView (R.id.transportLayout) LinearLayout transportLayout;
	protected JSONObject jsonContent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transport_layout);
	}
	
	public Handler getHandler()
	{
		System.out.println("Got handler");
		return mHandler;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		busRefreshTask = new BusPageRefreshTask(this, false, false);
		busRefreshTask.execute();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		busRefreshTask.cancel(true);
	}
	
	public LinearLayout getContentLayout()
	{
		return transportLayout;
	}
	
	@Override
	public Page getInstance() {
		return this;
	}
	
	@Override
	public String getName() {
		return MollyModule.PUBLIC_TRANSPORT;
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











