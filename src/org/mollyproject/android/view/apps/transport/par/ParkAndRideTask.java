package org.mollyproject.android.view.apps.transport.par;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ParkAndRideTask extends BackgroundTask<JSONObject,Void,JSONObject> {

	public ParkAndRideTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			JSONArray pars = jsonContent.getJSONArray("park_and_rides");
			page.getContentLayout().removeAllViews();
			for (int i = 0; i < pars.length(); i++)
			{
				final JSONObject par = pars.getJSONObject(i);
				
				RelativeLayout parLayout = (RelativeLayout) page.getLayoutInflater().inflate(R.layout.park_and_ride, null);
				parLayout.setLayoutParams(Page.paramsWithLine);
				
				((TextView) parLayout.findViewById(R.id.parName)).setText(par.getString("title").replace(" Park and Ride", ""));
				
				JSONObject metadata = par.getJSONObject("metadata");
				
				JSONObject spacesDetails = metadata.getJSONObject("park_and_ride");
				TextView parSpaces = (TextView) parLayout.findViewById(R.id.parSpaces);
				ImageView unavailBar = (ImageView) parLayout.findViewById(R.id.unavailBar);
				if (spacesDetails.getBoolean("unavailable"))
				{
					parSpaces.setText("Unavailable");
					unavailBar.setMaxWidth(0);
				}
				else
				{
					parSpaces.setText("Spaces: " + spacesDetails.getString("spaces") + "/" + spacesDetails.getString("capacity"));
					unavailBar.setMaxWidth(page.getWindowManager().getDefaultDisplay().getWidth()*spacesDetails.getInt("percentage")/100);
					unavailBar.setMinimumWidth(page.getWindowManager().getDefaultDisplay().getWidth()*spacesDetails.getInt("percentage")/100);
				}
				
				parLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							//Go to the place entity page of the park and ride
							MyApplication.placesArgs[0] = par.getString("identifier_scheme");
							MyApplication.placesArgs[1] = par.getString("identifier_value");
							Intent myIntent = new Intent(page.getApplicationContext(), MyApplication.getPageClass(MollyModule.PLACES_ENTITY));
							page.startActivityForResult(myIntent, 0);
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(page.getApplicationContext(), "This page is unavailable. Please try again later.", Toast.LENGTH_SHORT);
						}
					}
				});
				page.getContentLayout().addView(parLayout);
			}
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		} finally {
			ParkAndRideRefreshTask.parNeedsRefresh = true;
		}
	}
	
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		try {
			if (params.length > 0)
			{
				return params[0];
			}
			else 
			{
				JSONObject jsonContent = MyApplication.router.requestJSON(page.getName(), page.getAdditionalParams(), null);
				MyApplication.transportCache = jsonContent;
				return jsonContent;
			}
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
		
		return null;
	}
}
