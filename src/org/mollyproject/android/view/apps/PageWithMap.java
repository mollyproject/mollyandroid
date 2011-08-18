package org.mollyproject.android.view.apps;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.LocationTracker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public abstract class PageWithMap extends ContentPage {
	protected MapView mapView;
	protected LinearLayout mapLayout;
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
		ScrollView scr = (ScrollView) contentLayout.getParent();
		originalLayout.removeAllViews();
		scr.removeAllViews();

		mapLayout = (LinearLayout) layoutInflater.inflate(R.layout.map_view, contentLayout, false);
		setContentView(mapLayout);

		mapLayout.addView(breadcrumbs,0);
		mapLayout.addView(contentLayout);
		mapView = (MapView) mapLayout.findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        MapController mapController = mapView.getController();
        mapController.setZoom(16);
        mapController.setCenter(new GeoPoint(LocationTracker.DEFAULT_LAT,LocationTracker.DEFAULT_LON));
	}
	
	public MapView getMapView()
	{
		return mapView;
	}
}
