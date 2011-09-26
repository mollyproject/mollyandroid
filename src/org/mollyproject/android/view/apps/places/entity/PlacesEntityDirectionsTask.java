package org.mollyproject.android.view.apps.places.entity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ComplexMapResultTask;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;

import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlacesEntityDirectionsTask extends ComplexMapResultTask {

	public PlacesEntityDirectionsTask(ContentPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
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
				
				LinearLayout waypointsLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.general_search_results_page, null);
				
				String routeText = "This route is " + route.getInt("total_distance") + 
						"m and takes approx. " + route.getInt("total_time") + "s";
				
				String type = jsonContent.getString("type");
				
				if (type.equals("foot"))
				{
					routeText = routeText + " on " + type;
				}
				else
				{
					routeText = routeText + " by " + type;
				}
				
				((TextView) waypointsLayout.findViewById(R.id.searchResultsHeader)).setText(routeText);
				
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
					waypointsLayout.addView(waypointLayout);
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
