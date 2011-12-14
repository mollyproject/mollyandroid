package org.mollyproject.android.view.apps.places.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlacesResultsTask extends JSONProcessingTask
{
	protected boolean locationNotFound = false;
	public PlacesResultsTask(PlacesResultsPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}
	
	public static LinearLayout parseOxpoint(JSONObject jsonValue, final Page page) throws JSONException
	{
		LinearLayout unitLayout = (LinearLayout) page.getLayoutInflater().inflate
				(R.layout.clickable_search_result, null);
		
		TextView unitText = (TextView) unitLayout.findViewById(R.id.clickableResultText);
		
		String oxpoint = jsonValue.getString("uri");
		int initIndex = oxpoint.lastIndexOf("/") + 1;
		final String oxpointValue = oxpoint.substring(initIndex, oxpoint.length());//this is the identifier_value
		
		unitLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyApplication.placesArgs[0] = PlacesResultsPage.OXPOINTS;
				MyApplication.placesArgs[1] = oxpointValue;
				System.out.println(oxpointValue);
				Intent myIntent = new Intent(page.getApplicationContext(), 
						MyApplication.getPageClass(MollyModule.PLACES_ENTITY));
				page.startActivityForResult(myIntent, 0);
			}
		});
		unitLayout.setLayoutParams(Page.paramsWithLine);
		
		if (jsonValue.has("title"))
		{
			unitText.setText("title");
		}
		else
		{
			unitText.setText(oxpoint);
		}
		return unitLayout;
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			JSONObject entity = jsonContent.getJSONObject("entity");
			page.getContentLayout().removeAllViews();
			List<OverlayItem> overlayItems= new ArrayList<OverlayItem>();
			locationNotFound = entity.isNull("location");
			if (locationNotFound)
			{
				throw new Exception();
			}
			String title = entity.getString("title");
			
			GeoPoint point = null; //the centre of the map
			JSONObject metadata = entity.getJSONObject("metadata");
			if (entity.getString("identifier_scheme").equals(PlacesResultsPage.OXPOINTS))
			{
		        JSONObject jsonOxpoints = metadata.getJSONObject(PlacesResultsPage.OXPOINTS);
		        
		        point = new GeoPoint(jsonOxpoints.getDouble("geo_lat"), 
		        		jsonOxpoints.getDouble("geo_long"));
		        
		        //extra information: parent unit
		        if (entity.has("parent"))
		        {
		        	if (!entity.isNull("parent"))
		        	{
		        		LinearLayout parentLayout = (LinearLayout) page.getLayoutInflater().inflate
		        				(R.layout.general_search_results_page, null);
		        		parentLayout.setBackgroundResource(R.drawable.shape_white);
		        		TextView headerText = (TextView) parentLayout.findViewById(R.id.searchResultsHeader);
		        		headerText.setText("Parent unit:");
		        		headerText.setTextColor(Color.BLACK);
		        		page.getContentLayout().addView(parentLayout);
		        		
			        	final JSONObject parentUnit = entity.getJSONObject("parent");
		        		//LinearLayout parent = parseOxpoint(parentUnit, page);
			        	LinearLayout parent = (LinearLayout) page.getLayoutInflater().inflate(R.layout.clickable_search_result, null);
			        	((TextView) parent.findViewById(R.id.clickableResultText)).setText(parentUnit.getString("title"));
			        	
			        	parent.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								try {
									MyApplication.placesArgs[0] = parentUnit.getString("identifier_scheme");
									MyApplication.placesArgs[1] = parentUnit.getString("identifier_value");
									Intent myIntent = new Intent(page.getApplicationContext(), MyApplication.getPageClass(MollyModule.PLACES_ENTITY));
									page.startActivityForResult(myIntent, 0);
								} catch (Exception e) {
									e.printStackTrace();
									Toast.makeText(page.getApplicationContext(), "This operation is currently not available. Please try again later.", 
											Toast.LENGTH_SHORT).show();
								}
							}
						});
			        	parent.setLayoutParams(Page.paramsWithLine);
		        		parentLayout.addView(parent);
		        	}
		        }
		        /*
		        if (jsonOxpoints.has("dct_isPartOf"))
		        {
		        	if (!jsonOxpoints.isNull("dct_isPartOf"))
		        	{
		        		JSONObject parentUnit = jsonOxpoints.getJSONObject("dct_isPartOf");
		        		LinearLayout parent = parseOxpoint(parentUnit, page);
		        		page.getContentLayout().addView(parent);
		        	}
		        }*/
		        
		        //extra info: children units
		        if (jsonOxpoints.has("passiveProperties"))
		        {
		        	JSONObject passiveProperties = jsonOxpoints.getJSONObject("passiveProperties");
		        	if (passiveProperties.has("dct_isPartOf"))
		        	{
		        		JSONArray children = passiveProperties.getJSONArray("dct_isPartOf");
		        		
		        		LinearLayout childrenLayout = (LinearLayout) page.getLayoutInflater().inflate
		        				(R.layout.general_search_results_page, null);
		        		childrenLayout.setBackgroundResource(R.drawable.shape_white);
		        		TextView headerText = (TextView) childrenLayout.findViewById(R.id.searchResultsHeader);
		        		headerText.setText("Children unit(s):");
		        		headerText.setTextColor(Color.BLACK);
		        		page.getContentLayout().addView(childrenLayout);
		        		
			        	for (int i = 0; i < children.length(); i++)
			        	{
			        		LinearLayout child = parseOxpoint(children.getJSONObject(i), page);
			        		child.setLayoutParams(Page.paramsWithLine);
			        		childrenLayout.addView(child);
			        	}
		        	}
		        }
			}
			else if (entity.getString("identifier_scheme").equals(PlacesResultsPage.OSM))
			{
				JSONObject attrs = metadata.getJSONObject(PlacesResultsPage.OSM).getJSONObject("attrs");
				point = new GeoPoint(attrs.getDouble("lat"), attrs.getDouble("lon"));
			}

	        MapView mapView = ((PageWithMap) page).getMapView();
	        
	        MapController mapController = mapView.getController();
	        mapController.setCenter(point);

	        OverlayItem markerOverlay = new OverlayItem(title, "", point);
	        overlayItems.add(markerOverlay);
	        
	        ((PageWithMap) page).populateMarkers(overlayItems);
	        PlacesResultsPage.firstLoad = false;
	        ((ContentPage) page).doneProcessingJSON();
        } catch (Exception e)
		{
			e.printStackTrace();
			if (!locationNotFound)
			{
				otherException = true;
			}
			else
			{
				((PageWithMap) page).hideMap();
				TextView sorryText = new TextView(page.getApplicationContext());
				sorryText.setGravity(Gravity.CENTER);
				sorryText.setBackgroundResource(R.drawable.shape_white);
				sorryText.setTextColor(page.getResources().getColor(R.color.blue));
				sorryText.setText("We do not yet have a location for this.");
				sorryText.setTextSize(18);
				sorryText.setPadding(5, 10, 5, 10);
				page.getContentLayout().addView(sorryText);
			}
		}
	}
	
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		if (Page.manualRefresh)
		{
			return super.doInBackground();
		}
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ((ContentPage) page).getJSONContent();
	}
	
}
