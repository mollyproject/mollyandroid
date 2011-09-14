package org.mollyproject.android.view.apps.transport;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.map.PlacesResultsPage;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BusTask extends BackgroundTask<JSONObject,Void,JSONObject>{
	public BusTask(Page busPage, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(busPage, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			LinearLayout transportLayout = page.getContentLayout(); //this is the transportLayout in transport_layout.xml

			//Update the page title every time the page is refreshed
			TextView pageTitle = (TextView) transportLayout.findViewById(R.id.transportTitle);
			String pageTitleText = new String();
			pageTitleText = MyApplication.hourFormat.format(new Date()) 
						+ " - Nearby bus stops from your current location";
			
			if (MyApplication.currentLocation != null & MyApplication.currentLocation.has("name"))
			{
				pageTitleText = pageTitleText + " (" + MyApplication.currentLocation.getString("name")
						+ " within approx. " + MyApplication.currentLocation.getString("accuracy") + ")";
			}
			pageTitleText = pageTitleText + ":";
			
			pageTitle.setText(pageTitleText);
			LinearLayout busLayout = (LinearLayout) transportLayout.findViewById(R.id.transportDetailsLayout);
			busLayout.setBackgroundResource(R.drawable.shape_yellow);
			busLayout.removeAllViews();
			
			JSONArray stops = jsonContent.getJSONArray("entities");
			
			LayoutInflater layoutInflater = page.getLayoutInflater();
			
			for (int i = 0; i < stops.length(); i++)
			{
				//process each stop
				LinearLayout stopLayout = parseBusEntity(stops.getJSONObject(i), page, busLayout, layoutInflater);
				busLayout.addView(stopLayout);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} finally {
			BusPageRefreshTask.busNeedsRefresh = true;
		}
	}
	
	public static LinearLayout parseBusEntity(final JSONObject entity, final Page page, LinearLayout busLayout, 
			LayoutInflater layoutInflater) throws JSONException
	{
		//parse a bus stop entity, return the linear layout
		LinearLayout stopLayout = (LinearLayout) layoutInflater.inflate
				(R.layout.transport_bus_stop_layout, busLayout, false);
		
		TextView nearbyStop = (TextView) stopLayout.findViewById(R.id.nearbyStop);
		nearbyStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Intent myIntent = new Intent(page, MyApplication.getPageClass(MollyModule.PLACES_ENTITY));
					MyApplication.placesArgs[0] = PlacesResultsPage.TRANSPORT;//identifier_scheme, should be atco
					MyApplication.placesArgs[1] = entity.getJSONObject("identifiers").getString(PlacesResultsPage.TRANSPORT); //identifier_value
					page.startActivityForResult(myIntent, 0);
				} catch (JSONException e) {
					Toast.makeText(page, "There has been an error, please try again later", Toast.LENGTH_SHORT);
					e.printStackTrace();
				}
			}
		});
		
		String stopTitle = entity.getString("title");
		if (entity.has("distance"))
		{
			if (!entity.isNull("distance") & !entity.isNull("bearing"))
			{
				stopTitle = stopTitle + " (about " + entity.getString("distance") 
						+ " " + entity.getString("bearing") + ")"; 
			}
		}
		nearbyStop.setText(stopTitle + ":");
		
		//process each bus that passes through this stop
		JSONObject metadata = entity.getJSONObject("metadata");
		JSONObject info = metadata.getJSONObject("real_time_information");
		JSONArray services = info.getJSONArray("services");
		
		LinearLayout stopDetailsLayout = (LinearLayout) stopLayout.findViewById(R.id.stopDetailsLayout);
		
		if (services.length() == 0)
		{
			TextView noServiceText = new TextView(page);
			noServiceText.setText("Sorry, there is no real time information available for this stop");
			//noServiceText.setLayoutParams(Page.paramsWithLine);
			noServiceText.setBackgroundResource(R.drawable.shape_black);
			noServiceText.setPadding(5, 5, 5, 5);
			noServiceText.setTextSize(16);
			noServiceText.setTextColor(page.getResources().getColor(R.color.yellow));
			stopDetailsLayout.addView(noServiceText);
		}
		
		for (int j = 0; j < services.length(); j++)
		{
			//this is for one bus
			LinearLayout serviceLayout = (LinearLayout) layoutInflater.inflate
					(R.layout.transport_bus_result, stopLayout, false);
			stopDetailsLayout.addView(serviceLayout);
			
			JSONObject bus = services.getJSONObject(j);
			
			TextView service = (TextView) serviceLayout.findViewById(R.id.newServiceNumber);
			if (bus.getString("service").length() > 3)
			{
				service.setTextSize(36);
			}
			service.setText(bus.getString("service"));
			
			String next = bus.getString("next");
			TextView busDestination = (TextView) serviceLayout.findViewById(R.id.newBusDestination);
			busDestination.setText(bus.getString("destination")  + '\n' + next); //destination and closest bus time
			
			TextView newBusDueTime = (TextView) serviceLayout.findViewById(R.id.newBusDueTime);
			
			//times for the next bus on this route
			JSONArray following = bus.getJSONArray("following");
			if (!next.equals("DUE") & following.length() > 0)
			{
				String followingText = new String();
				for (int k = 0; k < following.length() & k < 2; k++)
				{
					if (k == 0)
					{
						followingText = following.getString(k);
					}
					else
					{
						followingText =  followingText + ", " +  following.getString(k);
					}
				}
				followingText = followingText + "...";
				newBusDueTime.setText("Next: "+followingText);
			}
			else
			{
				newBusDueTime.setText("Next: N/A");
			}
		}
		return stopLayout;
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
				JSONObject jsonContent = MyApplication.router.onRequestSent(page.getName(), page.getAdditionalParams(), 
						Router.OutputFormat.JSON, null);
				MyApplication.transportCache = jsonContent;
				return jsonContent;
			}
		} catch (SocketException e){
			e.printStackTrace();
			cancel(true);
			new BusTask(page,toDestroyPageAfterFailure,dialogEnabled).execute();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			unknownHostException = true;
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (IOException e) {
			e.printStackTrace();
			ioException = true;
		} catch (ParseException e) {
			e.printStackTrace();
			parseException = true;
		}
		
		return null;
	}
}




















