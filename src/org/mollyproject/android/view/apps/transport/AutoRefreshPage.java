package org.mollyproject.android.view.apps.transport;

import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.Page;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.LinearLayout;

public abstract class AutoRefreshPage extends Page {
	//protected boolean needsRefreshing = true;
	public static boolean firstReq = true;
	@InjectView (R.id.transportLayout) LinearLayout transportLayout;
	protected JSONObject jsonContent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.PUBLIC_TRANSPORT;
		setContentView(R.layout.transport_layout);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}
	
	public LinearLayout getContentLayout()
	{
		return transportLayout;
	}
	
}
