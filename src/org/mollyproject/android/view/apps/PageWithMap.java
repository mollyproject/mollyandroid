package org.mollyproject.android.view.apps;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.LocationTracker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ToggleButton;

public abstract class PageWithMap extends ContentPage {
	protected MapView mapView;
	protected LinearLayout mapLayout;
	protected ScrollView scr;
	protected ToggleButton toggleMapButton;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//somehow map doesn't show up if added directly to contentLayout, thus leading to
		//this destructive approach (for this page only)
		
		//so the order of the views from top down is:
		//1. breadcrumbs
		//2. the map
		//Anything that needs to be added should now be added to mapLayout
		
		LinearLayout originalLayout = (LinearLayout) breadcrumbs.getParent();
		scr = (ScrollView) contentLayout.getParent();
		originalLayout.removeAllViews();

		mapLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.map_view, null);
		setContentView(mapLayout);

		mapLayout.addView(breadcrumbs,0);
		mapLayout.addView(scr);
		
		mapView = (MapView) mapLayout.findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        MapController mapController = mapView.getController();
        mapController.setZoom(16);
        mapController.setCenter(new GeoPoint(LocationTracker.DEFAULT_LAT,LocationTracker.DEFAULT_LON));
        
        toggleMapButton = (ToggleButton) mapLayout.findViewById(R.id.toggleMapButton);
		toggleMapButton.setChecked(true);
		toggleMapButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        // Perform action on clicks
		        if (toggleMapButton.isChecked()) 
		        {
		        	mapLayout.addView(mapView, 2);
		        	System.out.println("Checked");
		        } 
		        else 
		        {
		        	System.out.println("Unchecked");
		        	mapLayout.removeView(mapView);
		        }
		    }
		});
	}
	
	public void drawPath(JSONArray points) throws JSONException
	{
		PathOverlay pathOverlay = new PathOverlay(getResources().getColor(R.color.gray), getApplicationContext());
		
		GeoPoint geoPoint;
		for (int i = 0; i < points.length(); i++)
		{
			JSONArray point = points.getJSONArray(i);
			geoPoint = new GeoPoint(point.getDouble(1), point.getDouble(0));
			pathOverlay.addPoint(geoPoint);
		}
		System.out.println("Points on path: " + pathOverlay.getNumberOfPoints());
		mapView.getOverlays().add(0, pathOverlay);
	}
	
	public void populateMarkers(List<OverlayItem> overlayItems)
	{
		mapView.getOverlays().clear();
		//populate the markers on the map's overlay
		ItemizedIconOverlay<OverlayItem> overlay = new ItemizedIconOverlay<OverlayItem>(this,overlayItems, null);
        mapView.getOverlays().add(overlay);
	}
	
	public boolean hideMap()
	{
		return toggleMapButton.performClick();
	}
	
	public LinearLayout getMapLayout()
	{
		return mapLayout;
	}
	
	public MapView getMapView()
	{
		return mapView;
	}
}
