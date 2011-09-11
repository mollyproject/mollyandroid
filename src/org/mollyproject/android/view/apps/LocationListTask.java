package org.mollyproject.android.view.apps;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
		//views[0] should be the historyLayout and view[1] should be the suggestionsLayout and view[2] is currentLocation TextView
		final LinearLayout historyLayout = (LinearLayout) views[0];
		final LinearLayout suggestionsLayout = (LinearLayout) views[1];
		final RelativeLayout showHistoryLayout = (RelativeLayout) views[2];
		final RelativeLayout showSuggestionsLayout = (RelativeLayout) views[3];
		final TextView currentLocation = (TextView) views[4];
		try {
			//for use in the location dialog only
			if (MyApplication.currentLocation != null)
			{
				//update the currentLocation text
				Page.updateLocText(currentLocation);
				
				/* Each JSONObject in the alternatives (suggestions) and history JSONArrays should have:
				 * String name
				 * JSONArray location, with 0: Double lat and 1: Double lon
				 * Double accuracy
				 */
				
				//populate the history
				JSONArray locationHistory = MyApplication.currentLocation.getJSONArray("history");
				if (locationHistory.length() == 0)
				{
					//Disable the history function
					showHistoryLayout.setBackgroundResource(R.drawable.shape_gray);
					showHistoryLayout.setClickable(false);
				}
				else
				{
					//Enable it, update the historyLayout
					showHistoryLayout.setBackgroundResource(R.drawable.bg_blue);
					showHistoryLayout.setClickable(true);
					showHistoryLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							page.showDialog(Page.DIALOG_LOCATION_HISTORY);
						}
					});
					
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
											location.getJSONArray("location").getDouble(1), location.getDouble("accuracy"), page, false, true)
												.execute(historyLayout, suggestionsLayout, showHistoryLayout, showSuggestionsLayout, currentLocation);
								} catch (Exception e) {
									Toast.makeText(page, "Your new location cannot be updated. Please try " +
											"again later", Toast.LENGTH_SHORT).show();
								}
								page.dismissDialog(Page.DIALOG_LOCATION_HISTORY);
							}
						});
						historyLayout.addView(historyResult);
					}
				}
				
				JSONArray alternatives = MyApplication.currentLocation.getJSONArray("alternatives");
				if (alternatives.length() == 0)
				{
					//Disable the suggestions function
					showSuggestionsLayout.setBackgroundResource(R.drawable.shape_gray);
					showSuggestionsLayout.setClickable(false);
				}
				else
				{
					//Enable it, update the suggestionsLayout
					showSuggestionsLayout.setBackgroundResource(R.drawable.bg_blue);
					showSuggestionsLayout.setClickable(true);
					showSuggestionsLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							page.showDialog(Page.DIALOG_LOCATION_SUGGESTIONS);
						}
					});
					suggestionsLayout.removeAllViews();
					for (int i = 0; i < alternatives.length(); i++)
					{
						final JSONObject alternative = alternatives.getJSONObject(i);
						LinearLayout alternativeResult = (LinearLayout) page.getLayoutInflater().inflate
								(R.layout.plain_text_search_result, null);
						alternativeResult.setLayoutParams(Page.paramsWithLine);
						//get the name of the location and display it
						TextView alternativeResultName = (TextView) alternativeResult.findViewById(R.id.plainTextResultText);
						alternativeResultName.setText(alternative.getString("name"));
						//make the result clickable
						alternativeResult.setClickable(true);
						alternativeResult.setBackgroundResource(R.drawable.bg_blue);
						alternativeResult.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								try {
									//get new location and update the current location text and the alternatives list
									new LocationListTask(alternative.getString("name"), alternative.getJSONArray("location").getDouble(0), 
											alternative.getJSONArray("location").getDouble(1), alternative.getDouble("accuracy"), page, false, true)
												.execute(historyLayout, suggestionsLayout, showHistoryLayout, showSuggestionsLayout, currentLocation);
								} catch (Exception e) {
									Toast.makeText(page, "Your new location cannot be updated. Please try " +
											"again later", Toast.LENGTH_SHORT).show();
								}
								page.dismissDialog(Page.DIALOG_LOCATION_SUGGESTIONS);
							}
						});
						suggestionsLayout.addView(alternativeResult);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//otherException = true;
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
