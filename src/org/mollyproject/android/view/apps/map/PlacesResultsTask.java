package org.mollyproject.android.view.apps.map;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import android.view.LayoutInflater;

public class PlacesResultsTask extends BackgroundTask<JSONObject, Void, JSONObject> {

	public PlacesResultsTask(PlacesResultsPage placesResultsPage, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(placesResultsPage, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(JSONObject entity) {
		try {
			LayoutInflater inflater = page.getLayoutInflater();
			MapView mapView = (MapView) inflater.inflate(R.layout.map_view, 
					((PlacesResultsPage) page).getContentLayout(),false);
			((PlacesResultsPage) page).getContentLayout().addView(mapView);
			
	        mapView.setTileSource(TileSourceFactory.MAPNIK);
	        mapView.setBuiltInZoomControls(true);
	        MapController mapController = mapView.getController();
			mapController.setZoom(entity.getInt("zoom"));
	        JSONObject metadata = entity.getJSONObject("metadata");
	        GeoPoint point = new GeoPoint(metadata.getDouble("geo_lat"), 
	        		metadata.getDouble("geo_long"));
	        mapController.setCenter(point);
        } catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
	}

	@Override
	protected JSONObject doInBackground(JSONObject... args) {
		try {
			JSONObject jsonOutput = args[0];
			JSONObject entity = jsonOutput.getJSONObject("entity");
			return entity;
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
		return null;
	}

}
