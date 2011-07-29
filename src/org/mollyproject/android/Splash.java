package org.mollyproject.android;


import org.mollyproject.android.R;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.apps.Page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;

public class Splash extends Page {
	protected boolean splashed = false;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		TextView welcomeText = new TextView(this);
		welcomeText.setText("Molly Android");
		
		setContentView(R.layout.splash);
		splashed = true;
		try {
			router = new Router(getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
			Page.popupErrorDialog("Network Connection cannot be set up. ", 
					"Please try again later", this, true);
		}
        
        myApp.setRouter(router);
		Thread splashThread = new Thread() {
			@Override
			public void run()
			{
				Looper.prepare();
				try 
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					//Do nothing
				}
				finally
				{
					Intent myIntent = new Intent(getApplicationContext(), 
							SelectionManager.getPageClass(SelectionManager.HOME_PAGE));
					startActivityForResult(myIntent, 0);
				}
			};
		};
		splashThread.start();
	}
	
	@Override 
	public void onResume()
	{
		super.onResume();
		if (splashed)
		{
			finish();
		}
	}
	@Override
	public Page getInstance() {
		// TODO Auto-generated method stub
		return this;
	}
}
