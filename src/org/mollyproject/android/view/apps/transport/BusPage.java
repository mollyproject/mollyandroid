package org.mollyproject.android.view.apps.transport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.Page;

import roboguice.inject.InjectView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BusPage extends Page{
	
	@InjectView (R.id.transportTitle) TextView pageTitle; 
	@InjectView (R.id.transportDetailsLayout) LinearLayout transportDetailsLayout;
	protected JSONObject jsonContent;
	DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transport_layout);
		//TextView pageTitle = (TextView) transportLayout.findViewById(R.id.transportTitle);
		pageTitle.setText("Nearby bus stops " + hourFormat.format(new Date()));
	}
	
	public LinearLayout getContentLayout()
	{
		return transportDetailsLayout;
	}
	
	public JSONObject getJSONContent()
	{
		return jsonContent;
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
