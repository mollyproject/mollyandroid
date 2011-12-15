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

/**
 * the Splash screen that first appears when launching the app
 */
public class Splash extends Page {
	/**
	 * records whether the splash page has been shown in the session
	 */
	protected boolean splashed = false;
	
	/**
	 * on creation, it spawns a thread in the background that
	 * sleeps for 3 seconds before starting the next Activity (HomePage)
	 *  
	 * @param savedInstanceState carries the saved instance state of the activity
	 * 
	 * @see Activity
	 */
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
	/**
	 * on resume, in case the splash page is navigated to from the home page, just skip it
	 */
	@Override 
	public void onResume()
	{
		super.onResume();
		if (splashed)
		{
			finish();
		}
		splashed = true;
	}
	
	/**
	 * @return an instance of itself
	 * 
	 * @see Page
	 */
	@Override
	public Page getInstance() {
		return this;
	}
	
	/**
	 * @return its assigned name
	 * 
	 * @see Page
	 * 
	 */
	@Override
	public String getName() {
		return MollyModule.SPLASH;
	}
	
	/**
	 * no query to pass on
	 * 
	 * @return just the null object
	 */
	@Override
	public String getQuery() throws UnsupportedEncodingException {
		return null;
	}
	
	/**
	 * no additional params to pass on
	 * 
	 * @return just the null object
	 */
	@Override
	public String getAdditionalParams() {
		return null;
	}
	/**
	 * no content layout available
	 * 
	 * @return just the null object
	 */
	@Override
	public LinearLayout getContentLayout() {
		return null;
	}
	
	/**
	 * no content layout available so does nothing
	 * 
	 */
	@Override
	public void setContentLayout(LinearLayout contentLayout) {
		
	}

	@Override
	public void refresh() {
	}
}
