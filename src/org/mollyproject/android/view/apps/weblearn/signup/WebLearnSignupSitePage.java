package org.mollyproject.android.view.apps.weblearn.signup;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class WebLearnSignupSitePage extends ContentPage {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.WEBLEARN_SIGNUP_SITE;
	}
	@Override
	public Page getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQuery() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
