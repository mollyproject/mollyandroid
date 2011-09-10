package org.mollyproject.android.view.apps;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LocationListTask extends BackgroundTask<View, Void, View[]>{
	protected String requestedLocation;
	protected Double lat;
	protected Double lon;
	protected Double accuracy;
	public LocationListTask(String requestedLocation, Double lat, Double lon, Double accuracy, Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		this.requestedLocation = requestedLocation;
		this.lat = lat;
		this.lon = lon;
		this.accuracy = accuracy;
	}

	@Override
	public void updateView(View[] views) {
		//views[0] should be the historyLayout and view[1] should be the currentLocation TextView
		final LinearLayout historyLayout = (LinearLayout) views[0];
		final TextView currentLocation = (TextView) views[1];
		try {
			updateLocationLayout(historyLayout, currentLocation);
		} catch (Exception e) {
			e.printStackTrace();
			//otherException = true;
		}
	}
	
	private void updateLocationLayout(final LinearLayout historyLayout, final TextView currentLocation) throws Exception
	{
		//for use in the location dialog only
		if (MyApplication.currentLocation != null)
		{
			Page.updateLocText(currentLocation);
			JSONArray locationHistory = MyApplication.currentLocation.getJSONArray("history");
			historyLayout.removeAllViews();
			for (int i = 0; i < locationHistory.length(); i++)
			{
				final JSONObject location = locationHistory.getJSONObject(i);
				LinearLayout historyResult = (LinearLayout) page.getLayoutInflater().inflate
							(R.layout.plain_text_search_result, null);
				historyResult.setLayoutParams(Page.paramsWithLine);
				//get the name of the location and display it
				TextView historyResultName = (TextView) historyResult.findViewById(R.id.plainTextResultText);
				historyResultName.setText(location.getString("name"));
				//make the result clickable
				historyResult.setClickable(true);
				historyResult.setBackgroundResource(R.drawable.bg_blue);
				historyResult.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							//get new location and update the current location text and the history list
							new LocationListTask(location.getString("name"), location.getJSONArray("location").getDouble(0), 
									location.getJSONArray("location").getDouble(0), location.getDouble("accuracy"), page, false, true)
										.execute(historyLayout,currentLocation);
						} catch (Exception e) {
							Toast.makeText(page, "Your new location cannot be updated. Please try " +
									"again later", Toast.LENGTH_SHORT).show();
						}
					}
				});
				historyLayout.addView(historyResult);
			}
		}
	}

	@Override
	protected View[] doInBackground(View... params) {
		// TODO Auto-generated method stub
		try {
			if (requestedLocation == null)
			{
				MyApplication.router.updateCurrentLocation();
			}
			else if (lon == null || lat == null)
			{
				MyApplication.router.updateLocationGeocoded(requestedLocation);
			}
			else
			{
				MyApplication.router.updateLocationManually(requestedLocation, lat, lon, accuracy);
			}
			return params;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
