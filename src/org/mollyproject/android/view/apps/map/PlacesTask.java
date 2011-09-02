package org.mollyproject.android.view.apps.map;

import org.json.JSONObject;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class PlacesTask extends BackgroundTask<JSONObject, Void, JSONObject>{

	public PlacesTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject outputs) {
		try {
			System.out.println(MyApplication.currentLocation.toString(1));
			PlacesPage.currentLocation.setText("Your current location:" + '\n' 
					+ MyApplication.currentLocation.getString("name") + " within approx. " + MyApplication.currentLocation.getString("accuracy"));
			PlacesPage.nearby.setClickable(true);
			PlacesPage.nearby.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("NEARBY");
					Intent myIntent = new Intent(page.getApplicationContext(), 
							MyApplication.getPageClass(MollyModule.PLACES_NEARBY));
					page.startActivityForResult(myIntent, 0);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			PlacesPage.currentLocation.setText("Cannot get a fix on your location");
			PlacesPage.nearby.setClickable(false);
		}
		((ContentPage) page).doneProcessingJSON();
	}

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
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
