package org.mollyproject.android.view.apps.map;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.LocationTracker;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;


public class PlacesResultsPage extends ContentPage {
	protected MapView mapView;
	protected String[] args;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		args = myApp.getPlacesArgs();
		//somehow map doesn't show up if added directly to contentLayout, thus leading to
		//this destructive approach (for this page only)
		LinearLayout originalLayout = (LinearLayout) breadcrumbs.getParent();
		ScrollView scr = (ScrollView) contentLayout.getParent();
		originalLayout.removeAllViews();
		scr.removeAllViews();

		LinearLayout mapLayout = (LinearLayout) layoutInflater.inflate(R.layout.map_view, contentLayout, false);
		setContentView(mapLayout);

		mapLayout.addView(breadcrumbs,0);
		mapLayout.addView(contentLayout);
		mapView = (MapView) mapLayout.findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        MapController mapController = mapView.getController();
        mapController.setZoom(16);
        //GeoPoint point2 = new GeoPoint(51496994, -134733);
        mapController.setCenter(new GeoPoint(LocationTracker.DEFAULT_LAT,LocationTracker.DEFAULT_LON));
	}

	public MapView getMapView()
	{
		return mapView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!jsonProcessed)
		{
			new PlacesResultsTask(this, true, true).execute();
		}
	}


	@Override
	public String getAdditionalParams() {
		
		String argsText = new String();
		for (String arg : args)
		{
			argsText = argsText + "&arg=" + arg;
		}
		return argsText;
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getName() {
		return MollyModule.PLACES_ENTITY;
	}

	@Override
	public String getQuery() {
		return null;
	}
}