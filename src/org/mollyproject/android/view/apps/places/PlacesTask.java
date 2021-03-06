package org.mollyproject.android.view.apps.places;

import org.json.JSONObject;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class PlacesTask extends JSONProcessingTask {

	public PlacesTask(ContentPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject outputs) {
		try {
			PlacesPage.currentLocation.setText("Your last updated location:" + '\n' 
					+ MyApplication.currentLocation.getString("name") + " within approx. " + MyApplication.currentLocation.getString("accuracy"));
			PlacesPage.nearby.setClickable(true);
			PlacesPage.nearby.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent myIntent = new Intent(page.getApplicationContext(), 
							MyApplication.getPageClass(MollyModule.PLACES_NEARBY));
					page.startActivityForResult(myIntent, 0);
				}
			});
			((ContentPage) page).doneProcessingJSON();
		} catch (Exception e) {
			e.printStackTrace();
			PlacesPage.currentLocation.setText("Cannot get a fix on your location");
			PlacesPage.nearby.setClickable(false);
		} finally {
			PlacesPage.firstLoad = false; //finish the first run
		}
		
	}

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		if(Page.manualRefresh)
		{
			return super.doInBackground();
		}
		
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ((ContentPage) page).getJSONContent();
	}

}
