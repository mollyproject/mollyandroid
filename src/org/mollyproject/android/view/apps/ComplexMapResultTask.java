package org.mollyproject.android.view.apps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.controller.BackgroundTask;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

public class ComplexMapResultTask extends BackgroundTask<Void,Void,JSONObject>{

	public ComplexMapResultTask(PageWithMap page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			MapView mapView = ((PageWithMap) page).getMapView();
			JSONObject jsonMap = jsonContent.getJSONObject("map");
			
			JSONArray jsonMapCentre = jsonMap.getJSONArray("map_centre");
			//set map centre
			//WARNING: lon comes before lat here
			//0: lon
        	//1: lat
			GeoPoint mapCentre = new GeoPoint(jsonMapCentre.getDouble(1),jsonMapCentre.getDouble(0));
			MapController mapController = mapView.getController();
	        mapController.setCenter(mapCentre);
	        
	        //process all markers/overlay items
			JSONArray jsonMarkers = jsonMap.getJSONArray("markers");
	        List<OverlayItem> overlayItems= new ArrayList<OverlayItem>();
	        for (int i = 0; i < jsonMarkers.length(); i++)
	        {
	        	JSONArray marker = jsonMarkers.getJSONArray(i);
	        	//0: lat
	        	//1: lon
	        	//2: icon to use
	        	//3: title
	        	GeoPoint markerPosition = new GeoPoint(marker.getDouble(0),marker.getDouble(1));
	        	OverlayItem markerOverlay = new OverlayItem(marker.getString(3), "", markerPosition);
	        	overlayItems.add(markerOverlay);
	        }
	        ItemizedIconOverlay<OverlayItem> overlay = new ItemizedIconOverlay<OverlayItem>(page,overlayItems, null);
	        mapView.getOverlays().add(overlay);
	        ((ContentPage) page).doneProcessingJSON();
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
	}

	@Override
	protected JSONObject doInBackground(Void... arg0) {
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
