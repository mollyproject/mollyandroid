package org.mollyproject.android.view.apps.weblearn;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class WebLearnPage extends ContentPage {
	protected String oauthToken;
	public static boolean weblearnPageNeedsRefresh;
	public static final int STATE_DEFAULT = 0;
	public static final int STATE_AUTHORISED = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.WEBLEARN;
		oauthToken = MyApplication.oauthToken;
		weblearnPageNeedsRefresh = false;
	}
	
	@Override
	public void onResume() {
		if (weblearnPageNeedsRefresh)
		{
			//Trigger a manual refresh if needed
			manualRefresh = true;
			jsonProcessed = false;
		}
		super.onResume();
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
		new WeblearnPageTask(this, false, true).execute();
	}
	
}
