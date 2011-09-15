package org.mollyproject.android.view.apps.places;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;
import org.mollyproject.android.view.apps.places.entity.PlacesResultsPage;
import org.mollyproject.android.view.apps.transport.BusPageRefreshTask;
import org.mollyproject.android.view.apps.transport.BusTask;
import org.mollyproject.android.view.apps.transport.TrainPageRefreshTask;
import org.mollyproject.android.view.apps.transport.TrainTask;
import org.mollyproject.android.view.apps.transport.TransportPage;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.graphics.Color;
import android.graphics.Typeface;
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
				TransportMapPageRefreshTask.transportType = TransportPage.BUS;
				//parse entity of bus
				LinearLayout originalBusLayout = BusTask.parseBusEntity(entity, page, 
						contentLayout, page.getLayoutInflater());
				//Customise the background:
				LinearLayout originalStopDetailsLayout = (LinearLayout) originalBusLayout.findViewById(R.id.stopDetailsLayout);
				originalStopDetailsLayout.setBackgroundResource(R.drawable.bg_white);
				//New page now looks like:
				//0 - breadcrumbs
				//1 - map
				//2 - contentLayout
				TextView stopText = (TextView) originalBusLayout.getChildAt(0);
				stopText.setClickable(false);
				stopText.setText(MyApplication.hourFormat.format(new Date()) 
						+ " - Real time information from this stop:");
				
				contentLayout.removeAllViews();
				contentLayout.addView(originalBusLayout);
			}
			else if (metadata.has("ldb"))
			{ 
				TransportMapPageRefreshTask.transportType = TransportPage.RAIL;
				//parse entity of rail
				LinearLayout originalTrainStationLayout = TrainTask.parseTrainEntity(entity, page, 
						contentLayout, page.getLayoutInflater()); 
				
				contentLayout.addView(originalTrainStationLayout);
			}
			
			//Associations
			/*TextView associationHeader = new TextView(page);
			associationHeader.setTextSize(16);
			associationHeader.setBackgroundResource(R.drawable.)*/
			//Parse associations, each association is a group of entities
			JSONArray associations = jsonContent.getJSONArray("associations");
			for (int i = 0; i < associations.length(); i++)
			{
				JSONObject association = associations.getJSONObject(i);
				
				//the title, stored as "type" in the JSONObject
				TextView assocTitle = new TextView(page);
				assocTitle.setText("Other information available from " + association.getString("type") + " nearby:");
				assocTitle.setTextSize(16);
				assocTitle.setPadding(5, 5, 5, 5);
				assocTitle.setTypeface(null, Typeface.BOLD);
				assocTitle.setTextColor(Color.BLACK);
				assocTitle.setBackgroundColor(Color.WHITE);
				contentLayout.addView(assocTitle);
				
				JSONArray assocEntities = association.getJSONArray("entities");
				for (int j = 0; j < assocEntities.length(); j++)
				{
					JSONObject assocEntity = assocEntities.getJSONObject(j);
					JSONObject assocMetadata = assocEntity.getJSONObject("metadata");
					//Must distinguish between bus stops and train stations before parsing the entity
					if (assocMetadata.has("real_time_information"))
					{
						//Bus
						//parse entity of bus
						LinearLayout assocOriginalBusLayout = BusTask.parseBusEntity(assocEntity, page, 
								page.getContentLayout(), page.getLayoutInflater());
						contentLayout.addView(assocOriginalBusLayout);
					}
					else if (assocMetadata.has("ldb"))
					{
						//Train
						//parse entity of bus
						LinearLayout assocOriginalTrainLayout = TrainTask.parseTrainEntity(entity, page, 
								page.getContentLayout(), page.getLayoutInflater());
						assocOriginalTrainLayout.setLayoutParams(Page.paramsWithLine);
						contentLayout.addView(assocOriginalTrainLayout);
					}
				}
			}
			
			//Show stuff on map (the location of the stop)
			if (!TransportMapPageRefreshTask.overlayRendered)
			{
				System.out.println("Overlay should be added now");
				List<OverlayItem> overlayItems= new ArrayList<OverlayItem>();
				String title = entity.getString("title");
	
		        MapView mapView = ((PlacesResultsPage) page).getMapView();
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
		} catch (SocketException e){
			e.printStackTrace();
			cancel(true);
			new TransportMapTask((PlacesResultsPage) page,toDestroyPageAfterFailure,dialogEnabled).execute();
		}catch (JSONException e) {
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
