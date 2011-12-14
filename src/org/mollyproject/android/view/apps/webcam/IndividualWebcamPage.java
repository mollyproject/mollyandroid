package org.mollyproject.android.view.apps.webcam;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
import android.widget.LinearLayout;

public class IndividualWebcamPage extends ContentPage {
	protected String slug;
	public static boolean firstLoad;
	public static boolean webcamNeedsRefresh;
	protected IndividualWebcamTask indWebcamTask; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		firstLoad = true;
		name = MollyModule.WEBCAM;
		slug = MyApplication.webcamSlug;
		additionalArgs = "&arg=" + slug;
		extraTextView.setText("Webcam");
		dialogOnSetup = true;
		
		LinearLayout webcamLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.webcam, null);
		contentLayout.addView(webcamLayout);
		
		contentLayout = webcamLayout;
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
		if (indWebcamTask != null)
		{
			indWebcamTask.cancel(true);
		}
		indWebcamTask = new IndividualWebcamTask(this, false, false);
		indWebcamTask.execute();
	}
	
	@Override
	public void onResume() {
		jsonProcessed = false; //always reload
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (indWebcamTask != null)
		{
			indWebcamTask.cancel(true);
		}
	}
}
