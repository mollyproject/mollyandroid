package org.mollyproject.android.view.apps.transport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.view.apps.Page;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class PublicTransportTask extends BackgroundTask<JSONObject,Void,JSONObject> {

	public PublicTransportTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}
	
	public LinearLayout parseBusEntity(JSONObject entity, Page page, LinearLayout busLayout, LayoutInflater layoutInflater) throws JSONException
	{
		//parse a bus stop entity, return the linear layout, it has to be here and cannot be simply
		//accessed as a static method to avoid wrong thread exception
		
		//Structure of the layout:
		//stopLayout
		//- child 0: nearbyStop (TextView)
		//- child 1: stopDetailsLayout (LinearLayout)
		//-- child 0 - rest: transport_bus_result (LinearLayout)
		//--- child 0: Service (TextView)
		//--- child 1: Destination (TextView)
		//--- child 2: Time (TextView)
		
		//Any other tasks that needs the entity parsed using this method can extract all the info it needs from
		//the layout
		LinearLayout stopLayout = (LinearLayout) layoutInflater.inflate
				(R.layout.transport_bus_stop_layout, busLayout, false);
		
		TextView nearbyStop = (TextView) stopLayout.findViewById(R.id.nearbyStop);
		//JSONObject stop = stops.getJSONObject(i);
		String stopTitle = entity.getString("title");
		if (entity.has("distance"))
		{
			if (!entity.isNull("distance") & !entity.isNull("bearing"))
			{
				stopTitle = stopTitle + " (about " + entity.getString("distance") 
						+ " " + entity.getString("bearing") + ")"; 
			}
		}
		nearbyStop.setText(stopTitle);
		
		//process each bus that passes through this stop
		JSONObject metadata = entity.getJSONObject("metadata");
		JSONObject info = metadata.getJSONObject("real_time_information");
		JSONArray services = info.getJSONArray("services");
		
		LinearLayout stopDetailsLayout = (LinearLayout) stopLayout.findViewById(R.id.stopDetailsLayout);
		
		if (services.length() == 0)
		{
			TextView noServiceText = new TextView(page);
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
		
		return stopLayout;
	}
}
