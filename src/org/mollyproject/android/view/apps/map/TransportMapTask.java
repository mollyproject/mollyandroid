package org.mollyproject.android.view.apps.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;
import org.mollyproject.android.view.apps.transport.BusPageRefreshTask;
import org.mollyproject.android.view.apps.transport.BusTask;
import org.mollyproject.android.view.apps.transport.TrainPageRefreshTask;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TransportMapTask extends BackgroundTask<Void,Void,JSONObject>{
	protected BusPageRefreshTask busPageRefreshTask;
	protected TrainPageRefreshTask trainPageRefreshTask;
	
	public TransportMapTask(PlacesResultsPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			MyApplication.transportCache = jsonContent;
			JSONObject entity = jsonContent.getJSONObject("entity");
			
			JSONObject metadata = entity.getJSONObject("metadata");
			//Must distinguish between bus stops and train stations
			if (metadata.has("real_time_information"))
			{
				//parse entity of bus
				LinearLayout originalBusLayout = BusTask.parseBusEntity(entity, page, 
						page.getContentLayout(), page.getLayoutInflater());
				
				//Structure of the original bus layout:
				//stopLayout
				//- child 0: nearbyStop (TextView)
				//- child 1: stopDetailsLayout (LinearLayout)
				//-- child 0 - rest: transport_bus_result (LinearLayout)
				//--- child 0: Service (TextView)
				//--- child 1: Destination (TextView)
				//--- child 2: Time (TextView)
				
				//Now: extract information from this layout
				//New page now looks like:
				//0 - breadcrumbs
				//1 - map
				//2 - contentLayout
				TextView stopText = (TextView) originalBusLayout.getChildAt(0);
				stopText.setText("Real time information at " + MyApplication.hourFormat.format(new Date()));
				stopText.setTextColor(Color.BLACK);
				stopText.setBackgroundResource(R.drawable.bg_white);
				LinearLayout contentLayout = (LinearLayout) ((PageWithMap) page).getMapLayout().getChildAt(2);
				/*if (!TransportMapPageRefreshTask.overlayRendered)
				{
					//Neat way to check if this is the first time the page is rendered
					contentLayout.addView(originalBusLayout);
				}
				else
				{
					contentLayout.removeAllViews();
					((PageWithMap) page).getMapLayout().addView(originalBusLayout,1);
				}*/
				contentLayout.removeAllViews();
				//contentLayout.addView(originalBusLayout);
			}
			else if (metadata.has("ldb"))
			{ 
				//parse entity of rail
			}
			
			//Show stuff on map (the location of the stop)
			if (!TransportMapPageRefreshTask.overlayRendered)
			{
				List<OverlayItem> overlayItems= new ArrayList<OverlayItem>();
				String title = entity.getString("title");
	
		        MapView mapView = ((PlacesResultsPage) page).getMapView();
		        // 0- long
		        // 1 - lat
		        GeoPoint point = new GeoPoint(entity.getJSONArray("location").getDouble(1), 
		        		entity.getJSONArray("location").getDouble(0));
		        
		        MapController mapController = mapView.getController();
		        mapController.setCenter(point);
	
		        OverlayItem markerOverlay = new OverlayItem(title, "", point);
		        overlayItems.add(markerOverlay);
		        ItemizedIconOverlay<OverlayItem> overlay = new ItemizedIconOverlay<OverlayItem>(page,overlayItems, null);
		        mapView.getOverlays().add(overlay);
		        TransportMapPageRefreshTask.overlayRendered = true;
			}
			((ContentPage) page).doneProcessingJSON();
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} finally {
			TransportMapPageRefreshTask.transportMapNeedsRefresh = true;
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
