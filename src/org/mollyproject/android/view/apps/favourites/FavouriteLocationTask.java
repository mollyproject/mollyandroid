package org.mollyproject.android.view.apps.favourites;

import org.json.JSONObject;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;

import android.widget.Toast;

public class FavouriteLocationTask extends JSONProcessingTask {

	public FavouriteLocationTask(ContentPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject outputs) {
		Toast.makeText(page.getApplicationContext(), "Your current location has been successfully updated " 
				, Toast.LENGTH_SHORT).show();	
	}
	
	@Override
	protected JSONObject doInBackground(JSONObject... entities) {
		JSONObject entity = entities[0];
		//Set the favourite as current location
		try {
			MyApplication.router.updateLocationManually(entity.getString("title"), entity.getJSONArray("location").getDouble(0), 
					entity.getJSONArray("location").getDouble(1), 0.0);
			return MyApplication.currentLocation; //this means the operation has been done successfully
		} catch (Exception e) {
			e.printStackTrace();
			operationException = true;
		}
		return null;
	}

}
