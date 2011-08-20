package org.mollyproject.android.view.apps.transport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.view.apps.ContentPage;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BusTask extends BackgroundTask<Void,Void,JSONObject>{
	protected TransportPage transportPage;
	public BusTask(TransportPage transportPage, BusPage busPage, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(busPage, toDestroyPageAfterFailure, dialogEnabled);
		this.transportPage = transportPage;
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			LinearLayout busLayout = ((BusPage) page).getContentLayout();
			
			JSONObject nearbyStops = jsonContent.getJSONObject("nearby");
			JSONArray stops = nearbyStops.getJSONArray("entities");
			LayoutInflater layoutInflater = page.getLayoutInflater();
			
			// + hourFormat.format(new Date())
			for (int i = 0; i < stops.length(); i++)
			{
				//process each stop
				LinearLayout stopLayout = (LinearLayout) inflater.inflate
						(R.layout.transport_bus_stop_layout, busLayout, false);
				
				busLayout.addView(stopLayout);
				TextView nearbyStop = (TextView) busLayout.findViewById(R.id.nearbyStop);
				JSONObject stop = stops.getJSONObject(i);
				String stopTitle = stop.getString("title");
				if (!stop.isNull("distance") & !stop.isNull("bearing"))
				{
					stopTitle = stopTitle + " (about " + stop.getString("distance") 
							+ " " + stop.getString("bearing"); 
				}
				nearbyStop.setText(stopTitle);
				
				//process each bus that passes through this stop
				JSONObject metadata = jsonContent.getJSONObject("metadata");
				JSONObject info = metadata.getJSONObject("real_time_information");
				JSONArray services = info.getJSONArray("services");
				
				for (int j = 0; j < services.length(); j++)
				{
					//this is for one bus
					LinearLayout serviceLayout = (LinearLayout) layoutInflater.inflate
							(R.layout.transport_bus_result, stopLayout, false);
					stopLayout.addView(serviceLayout);
					
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
					}
					nextBus.setText(next);
					
					stopLayout.addView(serviceLayout);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} 
	}

	@Override
	protected JSONObject doInBackground(Void... arg0) {
		while (!((ContentPage) transportPage).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ((ContentPage) transportPage).getJSONContent();
	}
}
