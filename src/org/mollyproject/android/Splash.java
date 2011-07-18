package org.mollyproject.android;


import org.mollyproject.android.R;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.pages.HomePage;
import org.mollyproject.android.view.pages.Page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;

public class Splash extends Page {
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		TextView welcomeText = new TextView(this);
		welcomeText.setText("Molly Android");
		
		setContentView(R.layout.splash);
		
		Thread splashThread = new Thread() {
			@Override
			public void run()
			{
				Looper.prepare();
				try 
				{
					Thread.sleep(3000);
				}
				catch (InterruptedException e)
				{
					//Do nothing
				}
				finally
				{
					Intent myIntent = new Intent(getApplicationContext(), 
							SelectionManager.getPage(SelectionManager.HOME_PAGE).getClass());
					startActivityForResult(myIntent, 0);
				}
			};
		};
		
		splashThread.start();
	}
}
