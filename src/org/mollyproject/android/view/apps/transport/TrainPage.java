package org.mollyproject.android.view.apps.transport;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.Page;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.LinearLayout;

public class TrainPage extends Page {

	@InjectView (R.id.transportLayout) LinearLayout transportLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transport_layout);
	}
	
	public LinearLayout getContentLayout()
	{
		return transportLayout;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		new TrainTask(this, false, true).execute();
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

	@Override
	public String getName() {
		return MollyModule.PUBLIC_TRANSPORT;
	}

}
