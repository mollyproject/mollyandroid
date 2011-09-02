package org.mollyproject.android.view.apps;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.LocationTracker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

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
	
	public LinearLayout getMapLayout()
	{
		return mapLayout;
	}
	
	public MapView getMapView()
	{
		return mapView;
	}
}
