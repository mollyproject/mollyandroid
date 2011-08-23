package org.mollyproject.android.view.apps.transport;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.Page;

import android.graphics.Color;
import android.os.Looper;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BusTask extends BackgroundTask<JSONObject,Void,JSONObject>{
	public BusTask(BusPage busPage, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(busPage, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			LinearLayout transportLayout = ((BusPage) page).getContentLayout(); //this is the transportLayout in transport_layout.xml

			//Update the page title every time the page is refreshed
			TextView pageTitle = (TextView) transportLayout.findViewById(R.id.transportTitle);
			pageTitle.setText("Nearby bus stops " + TransportPage.hourFormat.format(new Date()));
			
			LinearLayout busLayout = (LinearLayout) transportLayout.findViewById(R.id.transportDetailsLayout);
			busLayout.removeAllViews();
			
			JSONArray stops = jsonContent.getJSONArray("entities");
			
			LayoutInflater layoutInflater = page.getLayoutInflater();
			
			// + hourFormat.format(new Date())
			for (int i = 0; i < stops.length(); i++)
			{
				//process each stop
				LinearLayout stopLayout = (LinearLayout) inflater.inflate
						(R.layout.transport_bus_stop_layout, busLayout, false);
				busLayout.addView(stopLayout);
				
				TextView nearbyStop = (TextView) stopLayout.findViewById(R.id.nearbyStop);
				JSONObject stop = stops.getJSONObject(i);
				String stopTitle = stop.getString("title");
				if (!stop.isNull("distance") & !stop.isNull("bearing"))
				{
					stopTitle = stopTitle + " (about " + stop.getString("distance") 
							+ " " + stop.getString("bearing"); 
				}
				nearbyStop.setText(stopTitle);
				
				//process each bus that passes through this stop
				JSONObject metadata = stop.getJSONObject("metadata");
				JSONObject info = metadata.getJSONObject("real_time_information");
				JSONArray services = info.getJSONArray("services");
				
				LinearLayout stopDetailsLayout = (LinearLayout) stopLayout.findViewById(R.id.stopDetailsLayout);
				
				if (services.length() == 0)
				{
					TextView noServiceText = new TextView(page.getApplicationContext());
					noServiceText.setText("Sorry, there is no real time information available for this service");
					noServiceText.setLayoutParams(Page.paramsWithLine);
					noServiceText.setBackgroundResource(R.drawable.bg_white);
					noServiceText.setPadding(5, 5, 5, 5);
					noServiceText.setTextSize(16);
					noServiceText.setTextColor(Color.BLACK);
					stopDetailsLayout.addView(noServiceText);
				}
				
				for (int j = 0; j < services.length(); j++)
				{
					//this is for one bus
					LinearLayout serviceLayout = (LinearLayout) layoutInflater.inflate
							(R.layout.transport_bus_result, stopLayout, false);
					stopDetailsLayout.addView(serviceLayout);
					
					serviceLayout.setLayoutParams(Page.paramsWithLine);
					
					JSONObject bus = services.getJSONObject(j);
					
					TextView service = (TextView) serviceLayout.findViewById(R.id.serviceNumber);
					service.setText(bus.getString("service"));
					
					TextView busDestination = (TextView) serviceLayout.findViewById(R.id.busDestination);
					busDestination.setText(bus.getString("destination"));
					
					TextView nextBus = (TextView) serviceLayout.findViewById(R.id.busDueTime);
					String next = bus.getString("next");
					//times for the next bus on this route
					if (!next.equals("DUE"))
					{
						JSONArray following = bus.getJSONArray("following");
						for (int k = 0; k < following.length(); k++)
						{
							next = next + ", " + following.getString(k);
						}
						if (following.length() > 0)
						{
							next = next + "...";
						}
					}
					nextBus.setText(next);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} 
	}
	
	@Override
	protected void onPostExecute(JSONObject outputs) {
		super.onPostExecute(outputs);
		((BusPage) page).toBeRefreshed(true);
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
				JSONObject jsonContent = page.getRouter().onRequestSent(page.getName(), page.getAdditionalParams(), 
						Router.OutputFormat.JSON, null);
				myApp.setTransportCache(jsonContent);
				return jsonContent;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			//unknownHostException = true;
		} catch (JSONException e) {
			e.printStackTrace();
			//jsonException = true;
		} catch (IOException e) {
			e.printStackTrace();
			//ioException = true;
		} catch (ParseException e) {
			e.printStackTrace();
			//parseException = true;
		}
		
		return null;
	}
}




















