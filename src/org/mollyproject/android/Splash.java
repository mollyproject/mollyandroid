package org.mollyproject.android;


import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.LinearLayout;

public class Splash extends Page {
	protected boolean splashed = false; //this records whether the splash page has been shown in the session
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash); //view the splash image
		new Thread() {
			@Override
			public void run()
			{
				Looper.prepare();
				try 
				{
					//spawn a little timer to jump to home page without blocking UI thread
					Thread.sleep(3000); 
				}
				catch (InterruptedException e)
				{
					//Do nothing
				}
				finally
				{
					//go to home page
					Intent myIntent = new Intent(getApplicationContext(), 
							MyApplication.getPageClass(MollyModule.HOME_PAGE));
					startActivityForResult(myIntent, 0);
				}
			};
		}.start();
	}
	
	@Override 
	public void onResume()
	{
		super.onResume();
		if (splashed)
		{
			//in case the splash page is navigated to from the home page, just skip it
			finish();
		}
		splashed = true;
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getName() {
		return MollyModule.SPLASH;
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
	public LinearLayout getContentLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContentLayout(LinearLayout contentLayout) {
		// TODO Auto-generated method stub
		
	}
}
