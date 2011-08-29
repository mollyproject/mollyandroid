package org.mollyproject.android.view.apps.map;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;
import org.mollyproject.android.view.apps.transport.BusPageRefreshTask;
import org.mollyproject.android.view.apps.transport.BusTask;
import org.mollyproject.android.view.apps.transport.TrainPageRefreshTask;
import org.mollyproject.android.view.apps.transport.TrainTask;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.graphics.Color;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class TransportMapTask extends BackgroundTask<JSONObject,Void,JSONObject>{
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
			LinearLayout contentLayout = page.getContentLayout();
			MyApplication.transportCache = jsonContent;
			JSONObject entity = jsonContent.getJSONObject("entity");
			JSONObject metadata = entity.getJSONObject("metadata");
			//Must distinguish between bus stops and train stations
			if (metadata.has("real_time_information"))
			{
				//parse entity of bus
				LinearLayout originalBusLayout = BusTask.parseBusEntity(entity, page, 
						page.getContentLayout(), page.getLayoutInflater());
				originalBusLayout.setLayoutParams(Page.paramsWithLine);
				//Structure of the original bus layout:
				//stopLayout
				//- child 0: nearbyStop (TextView)
				//- child 1: stopDetailsLayout (LinearLayout)
				//-- child 0 - rest: transport_bus_result (LinearLayout)
				//--- child 0: Service (TextView)
				//--- child 1: Destination (TextView)
				//--- child 2: Time (TextView)
				
				//Now: extract information from this layout and restructure it
				LinearLayout stopDetailsLayout = (LinearLayout) originalBusLayout.getChildAt(1);
				try
				{
					for (int i = 0; i < stopDetailsLayout.getChildCount(); i++)
					{
						LinearLayout newBusResult = (LinearLayout) page.getLayoutInflater().inflate
								(R.layout.transport_bus_result_2,stopDetailsLayout, false);
						System.out.println(stopDetailsLayout.getChildCount());
						LinearLayout oldResult = (LinearLayout) stopDetailsLayout.getChildAt(i);
						
						TextView newServiceNumber = (TextView) newBusResult.findViewById(R.id.newServiceNumber);
						newServiceNumber.setText(((TextView) oldResult.getChildAt(0)).getText());
						
						TextView newBusDestination = (TextView) newBusResult.findViewById(R.id.newBusDestination);
						newBusDestination.setText(((TextView) oldResult.getChildAt(1)).getText());
						
						TextView newBusDueTime = (TextView) newBusResult.findViewById(R.id.newBusDueTime);
						TextView newClosestBus = (TextView) newBusResult.findViewById(R.id.newClosestBus);
						String times = ((TextView) oldResult.getChildAt(2)).getText().toString();
						int firstCommaIndex = times.indexOf(',');
						
						if (firstCommaIndex == -1)
						{
							//Only 1 result visible
							newBusDueTime.setText("Next: N/A");
							if (times.equals("DUE"))
							{
								newClosestBus.setText(times);
							}
						}
						else
						{
							//More than 1 results visible
							newClosestBus.setText(times.substring(0, firstCommaIndex));
							newBusDueTime.setText("Next: " + times.substring(firstCommaIndex+1, 
									times.length()));
						}
						
						//Now replace the layout with the new one:
						stopDetailsLayout.removeViewAt(i);
						stopDetailsLayout.addView(newBusResult,i);
					}
				} catch (ClassCastException e)
				{
					//No info is available
					//Do nothing
				}
				
				//New page now looks like:
				//0 - breadcrumbs
				//1 - map
				//2 - contentLayout
				TextView stopText = (TextView) originalBusLayout.getChildAt(0);
				stopText.setText("Real time information at " + MyApplication.hourFormat.format(new Date()));
				stopText.setTextColor(Color.BLACK);
				stopText.setBackgroundResource(R.drawable.bg_white);
				ScrollView scr = (ScrollView) ((PageWithMap) page).getMapLayout().getChildAt(2);
				scr.setMinimumHeight(page.getWindowManager().getDefaultDisplay().getHeight()/3);
				contentLayout.removeAllViews();
				contentLayout.addView(originalBusLayout);
			}
			else if (metadata.has("ldb"))
			{ 
				//parse entity of rail
				LinearLayout originalTrainStationLayout = TrainTask.parseTrainEntity(entity, page, 
						contentLayout, page.getLayoutInflater()); 
				
				contentLayout.addView(originalTrainStationLayout);
			}
			
			//Show stuff on map (the location of the stop)
			if (!TransportMapPageRefreshTask.overlayRendered)
			{
				System.out.println("Overlay should be added now");
				List<OverlayItem> overlayItems= new ArrayList<OverlayItem>();
				String title = entity.getString("title");
	
		        MapView mapView = ((PlacesResultsPage) page).getMapView();
		        mapView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
		        		page.getWindowManager().getDefaultDisplay().getHeight()*2/5));
		        // 0 - long
		        // 1 - lat
		        GeoPoint point = new GeoPoint(entity.getJSONArray("location").getDouble(1), 
		        		entity.getJSONArray("location").getDouble(0));
		        
		        MapController mapController = mapView.getController();
		        mapController.setCenter(point);
	
		        OverlayItem markerOverlay = new OverlayItem(title, "", point);
		        overlayItems.add(markerOverlay);
		        ItemizedIconOverlay<OverlayItem> overlay = new ItemizedIconOverlay<OverlayItem>
		        									(page,overlayItems, null);
		        mapView.getOverlays().add(overlay);
		        TransportMapPageRefreshTask.overlayRendered = true;
			}
			((ContentPage) page).doneProcessingJSON();
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} finally {
			PlacesResultsPage.firstLoad = false;
			TransportMapPageRefreshTask.transportMapNeedsRefresh = true;
		}
	}

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		try
		{
			if (params.length > 0)
			{
				//first run, fed JSON data
				return params[0];
			}
			else
			{
				JSONObject jsonContent = MyApplication.router.onRequestSent(page.getName(), page.getAdditionalParams(), 
						Router.OutputFormat.JSON, page.getQuery());
				MyApplication.transportCache = jsonContent;
				return jsonContent;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			unknownHostException = true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null; //if anything wrong happens, go here
	}

}
