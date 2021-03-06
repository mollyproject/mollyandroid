package org.mollyproject.android.view.apps.weblearn.signup;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class WebLearnSignupEventPage extends ContentPage {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.WEBLEARN_SIGNUP_EVENT;
		//url: signup/site/id
		additionalArgs = "&arg=" + MyApplication.weblearnSignupSlug + "&arg=" + MyApplication.weblearnEventId; 
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
		new WebLearneventEventTask(this, false, true).execute();
	}

}
