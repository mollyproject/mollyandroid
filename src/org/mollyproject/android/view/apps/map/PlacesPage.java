package org.mollyproject.android.view.apps.map;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import roboguice.inject.InjectView;

import android.os.Bundle;

public class PlacesPage extends ContentPage {

	//@InjectView (R.id.mapview) MapView mapview;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		
		/*MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        MapController mapController = mapView.getController();
        mapController.setZoom(15);
        GeoPoint point2 = new GeoPoint(51496994, -134733);
        mapController.setCenter(point2);
		//contentLayout.addView(mapView);*/
	}
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }
	
	@Override
	public Page getInstance() {
		return this;
	}
	@Override
	public String getAdditionalParams() {
		return null;
	}
	@Override
	public String getName() {
		return MollyModule.PLACES_PAGE;
	}
	
}
