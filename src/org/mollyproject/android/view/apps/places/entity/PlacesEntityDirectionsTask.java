package org.mollyproject.android.view.apps.places.entity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ComplexMapResultTask;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;

import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class PlacesEntityDirectionsTask extends ComplexMapResultTask {

	public PlacesEntityDirectionsTask(ContentPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}
	
	private void setTransportChanged(ToggleButton transportButton, final String transportation)
	{
		transportButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
				{
					buttonView.setChecked(false);
					MyApplication.transportation = transportation;
					Page.manualRefresh = true;
					new PlacesEntityDirectionsTask((ContentPage) page, false, true).execute();
				}
				else
				{
					buttonView.setChecked(true);
				}
			}
		});
	}
	
	@Override
	public void updateView(JSONObject jsonContent) {
		super.updateView(jsonContent);
		
		if (!exceptionCaught)
		{
			try
			{
				//Draw the path on the map
				JSONObject route = jsonContent.getJSONObject("route");
				((PageWithMap) page).drawPath(route.getJSONArray("path"));
				
				//Populate the directions
				JSONArray waypoints = route.getJSONArray("waypoints");
				
				LinearLayout waypointsLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.directions_page, null);
				
				//direction text:
				//time
				int time = route.getInt("total_time");
				String approxTime;
				if (time > 60)
				{
					approxTime = time/60 + "min";
				}
				else
				{
					approxTime = time + "s";
				}
				
				//distance
				int distance = route.getInt("total_distance");
				String distanceText;
				if (distance > 1000)
				{
					distanceText = distance/1000 + "km";
				}
				else
				{
					distanceText = distance + "m";
				}
				String routeText = "This route will take " + approxTime + " and cover " + distanceText;
				
				//transportation buttons
				ToggleButton footButton = (ToggleButton) waypointsLayout.findViewById(R.id.footButton);
				ToggleButton bicycleButton = (ToggleButton) waypointsLayout.findViewById(R.id.bicycleButton);
				ToggleButton carButton = (ToggleButton) waypointsLayout.findViewById(R.id.carButton);
				
				//transport type
				String type = jsonContent.getString("type");
				if (type.equals("foot"))
				{
					routeText = routeText + " on " + type;
					footButton.setChecked(true);
				}
				else
				{
					routeText = routeText + " by " + type;
					if (type.equals("car"))
					{
						carButton.setChecked(true);
					}
					else
					{
						bicycleButton.setChecked(true);
					}
				}
				routeText = routeText + ".";
				//set the behaviour of the 3 buttons here, because otherwise the page will keep reloading every time
				//the ToggleButton.setChecked method is invoked in the above if statement
				setTransportChanged(footButton, "foot");
				setTransportChanged(bicycleButton, "bicycle");
				setTransportChanged(carButton, "car");
				
				((TextView) waypointsLayout.findViewById(R.id.directionsHeader)).setText(routeText);
				LinearLayout waypointsList = (LinearLayout) waypointsLayout.findViewById(R.id.directionsList);
				
				//display list of directions:
				for (int i = 0; i < waypoints.length(); i++)
				{
					JSONObject waypoint = waypoints.getJSONObject(i);
					
					LinearLayout waypointLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.plain_text_search_result, null);
					
					String waypointText = new String();
					
					if (waypoint.has("instruction") & !waypoint.isNull("instruction"))
					{
						waypointText = "<b>" + (i+1) + ". " + waypoint.getString("instruction") + "</b> <br/>" +
								waypoint.getString("additional"); 
					}
					((TextView) waypointLayout.findViewById(R.id.plainTextResultText)).setText(Html.fromHtml(waypointText));
					waypointLayout.setLayoutParams(Page.paramsWithLine);
					waypointsList.addView(waypointLayout);
				}
				page.getContentLayout().removeAllViews();
				page.getContentLayout().addView(waypointsLayout);
				
				((ContentPage) page).doneProcessingJSON();
			} catch (Exception e) {
				e.printStackTrace();
				otherException = true;
			}
			
		}
	}
}
