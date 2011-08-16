package org.mollyproject.android.view.apps.map;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		try {
			List<OverlayItem> overlayItems= new ArrayList<OverlayItem>();
			JSONObject entity = jsonContent.getJSONObject("entity");
			String title = entity.getString("title");

			GeoPoint point = null;
			JSONObject metadata = entity.getJSONObject("metadata");
			if (entity.getString("identifier_scheme").equals("oxpoints"))
			{
		        JSONObject oxpoints = metadata.getJSONObject("oxpoints");

		        point = new GeoPoint(oxpoints.getDouble("geo_lat"), 
		        		oxpoints.getDouble("geo_long"));
			}
			else if (entity.getString("identifier_scheme").equals("osm"))
			{
				JSONObject attrs = metadata.getJSONObject("osm").getJSONObject("attrs");
				point = new GeoPoint(attrs.getDouble("lat"), attrs.getDouble("lon"));

			}
	        OverlayItem marker = new OverlayItem(title, "", point);
	        overlayItems.add(marker);

	        ItemizedIconOverlay<OverlayItem> overlay = new ItemizedIconOverlay<OverlayItem>(this,overlayItems, null);

	        MapController mapController = mapView.getController();
	        mapController.setCenter(point);

	        mapView.getOverlays().add(overlay);
        } catch (JSONException e) {
			e.printStackTrace();
		}
	}


	@Override
	public String getAdditionalParams() {
		String[] args = myApp.getPlacesArgs();
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