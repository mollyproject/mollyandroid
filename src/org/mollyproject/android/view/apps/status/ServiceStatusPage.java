package org.mollyproject.android.view.apps.status;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

public class ServiceStatusPage extends ContentPage {

	@Override
	public void onResume() {
		super.onResume();
		if (!jsonProcessed)
		{
			new ServiceStatusTask(this,true,true).execute();
		}
		else 
		{
			new ServiceStatusTask(this,false,true).execute();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return MollyModule.STATUS_PAGE;
	}
	
}
