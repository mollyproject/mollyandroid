package org.mollyproject.android.view.apps.webcam;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class WebcamsPage extends ContentPage {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.WEBCAMS;
		extraTextView.setText("Webcams");
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
		new WebcamPageTask(this, false, true).execute();
	}

}
