package org.mollyproject.android.view.apps.transport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.Page;

import roboguice.inject.InjectView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BusPage extends Page{
	
	//@InjectView (R.id.transportTitle) TextView pageTitle; 
	@InjectView (R.id.transportLayout) LinearLayout transportLayout;
	protected JSONObject jsonContent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transport_layout);
		//TextView pageTitle = (TextView) transportLayout.findViewById(R.id.transportTitle);
		//pageTitle.setText("Nearby bus stops " + hourFormat.format(new Date()));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//pageTitle.setText("Nearby bus stops " + hourFormat.format(new Date()));
		jsonContent = myApp.getTransportCache();
		System.out.println("Bus Page onresume");
		new BusTask(this, false, false).execute(jsonContent);
		
	}
	
	public LinearLayout getContentLayout()
	{
		return transportLayout;
	}
	
	@Override
	public Page getInstance() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
