package org.mollyproject.android;


import java.io.IOException;

import org.json.JSONException;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;

public class Splash extends Page {
	protected Router router;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		TextView welcomeText = new TextView(this);
		welcomeText.setText("Molly Android");
		
		setContentView(R.layout.splash);
		
		//try catch actually doesn't do anything useful, but throws
		//cannot be used for onCreate - Android just pull the whole
		//thing down if there is an exception
        try {
			router = new Router(getApplicationContext());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	public Page getInstance() {
		// TODO Auto-generated method stub
		return this;
	}
}
