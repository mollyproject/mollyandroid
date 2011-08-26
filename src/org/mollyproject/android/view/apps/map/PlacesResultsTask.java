package org.mollyproject.android.view.apps.map;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.view.apps.ContentPage;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

public class PlacesResultsTask extends BackgroundTask<Void,Void,JSONObject>
{
	public PlacesResultsTask(PlacesResultsPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
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

	        MapView mapView = ((PlacesResultsPage) page).getMapView();
	        
	        MapController mapController = mapView.getController();
	        mapController.setCenter(point);

	        OverlayItem markerOverlay = new OverlayItem(title, "", point);
	        overlayItems.add(markerOverlay);
	        ItemizedIconOverlay<OverlayItem> overlay = new ItemizedIconOverlay<OverlayItem>(page,overlayItems, null);
	        mapView.getOverlays().add(overlay);
	        
	        ((ContentPage) page).doneProcessingJSON();
        } catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected JSONObject doInBackground(Void... params) {
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
