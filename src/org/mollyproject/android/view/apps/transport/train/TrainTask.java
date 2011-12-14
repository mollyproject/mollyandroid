package org.mollyproject.android.view.apps.transport.train;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.transport.TransportPage;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TrainTask extends BackgroundTask<JSONObject, Void, JSONObject> {
	public TrainTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}

	public static LinearLayout parseTrainEntity(JSONObject entity, Page page, 
				LinearLayout trainLayout, LayoutInflater layoutInflater) throws JSONException
	{
		LinearLayout stationLayout = (LinearLayout) layoutInflater.inflate
				(R.layout.transport_train_station_layout, trainLayout, false);
		
		LinearLayout stationDetailsLayout = (LinearLayout) stationLayout.findViewById(R.id.stationDetailsLayout);
		JSONObject ldb = entity.getJSONObject("metadata").getJSONObject("ldb");
		
		//Announcements:
		if (ldb.has("nrccMessages"))
		{
			JSONArray announcements = ldb.getJSONObject("nrccMessages").getJSONArray("message");
			TextView announcementsText = (TextView) stationLayout.findViewById(R.id.stationMessage);
			String announcementMessage = new String();
			if (announcements.length() > 0)
			{
				announcementMessage = announcementMessage + "Announcements:" + "<br/>";
			}
			for (int i = 0; i < announcements.length(); i++)
			{
				announcementMessage = announcementMessage + "- " + announcements.getString(i) + "<br/>";
			}
			announcementsText.setText(Html.fromHtml(announcementMessage));
		}
		
		//Header
		LinearLayout header = (LinearLayout) layoutInflater.inflate(R.layout.transport_train_result, null);
		header.setLayoutParams(Page.paramsWithLine);
		((TextView) header.findViewById(R.id.trainDestination)).setTypeface(Typeface.DEFAULT_BOLD);
		((TextView) header.findViewById(R.id.trainDestination)).setTextSize(14);
		((TextView) header.findViewById(R.id.platformNumber)).setTypeface(Typeface.DEFAULT_BOLD);
		((TextView) header.findViewById(R.id.platformNumber)).setTextSize(14);
		((TextView) header.findViewById(R.id.trainScheduledTime)).setTypeface(Typeface.DEFAULT_BOLD);
		((TextView) header.findViewById(R.id.trainScheduledTime)).setTextSize(14);
		((TextView) header.findViewById(R.id.trainExpectedTime)).setTypeface(Typeface.DEFAULT_BOLD);
		((TextView) header.findViewById(R.id.trainExpectedTime)).setTextSize(14);
		
		stationDetailsLayout.addView(header);
		
		JSONArray services = ldb.getJSONObject("trainServices").getJSONArray("service");
		for (int i = 0; i < services.length(); i++)
		{
			//show the info for each service
			JSONObject service = services.getJSONObject(i);
			
			LinearLayout trainResultLayout = (LinearLayout) layoutInflater.inflate(R.layout.transport_train_result, 
					null);
			
			TextView destination = (TextView) trainResultLayout.findViewById(R.id.trainDestination);
			destination.setText(service.getJSONObject("destination").
					getJSONArray("location").getJSONObject(0).getString("locationName"));
			
			TextView platform = (TextView) trainResultLayout.findViewById(R.id.platformNumber);
			if (service.has("platform"))
			{
				platform.setText(service.getString("platform"));
			}
			else
			{
				platform.setText("N/A");
			}
				
			TextView scheduledTime = (TextView) trainResultLayout.findViewById(R.id.trainScheduledTime);
			
			TextView expectedTime = (TextView) trainResultLayout.findViewById(R.id.trainExpectedTime);
			
			if (service.has("std")) //(TrainPage.board.equals(TrainPage.DEPARTURES))
			{
				scheduledTime.setText(service.getString("std"));
				expectedTime.setText(service.getString("etd"));
			}
			else if (service.has("sta")) //(TrainPage.board.equals(TrainPage.ARRIVALS))
			{
				scheduledTime.setText(service.getString("sta"));
				expectedTime.setText(service.getString("eta"));
			}
			
			if (service.has("problems"))
			{
				if (service.getBoolean("problems"))
				{
					//Delayed or cancelled trains are painted in red
					destination.setTextColor(Color.RED);
					destination.setText(destination.getText() + "(!)");
					platform.setTextColor(Color.RED);
					scheduledTime.setTextColor(Color.RED);
					expectedTime.setTextColor(Color.RED);
				}
			}
			trainResultLayout.setLayoutParams(Page.paramsWithLine);
			stationDetailsLayout.addView(trainResultLayout);
		}
		//National Rail Enquiries logo
		ImageView nreLogo = new ImageView(page);
		nreLogo.setBackgroundResource(R.drawable.bg_white);
		nreLogo.setImageResource(R.drawable.powered_by_nre);
		nreLogo.setPadding(0, 5, 0, 5);
		nreLogo.setLayoutParams(Page.paramsWithLine);
		stationDetailsLayout.addView(nreLogo);
		
		return stationLayout;
	}
	
	@Override
	public void updateView(JSONObject jsonContent) {
		try
		{
			LinearLayout transportLayout = page.getContentLayout(); //this is the transportLayout in transport_layout.xml
			
			//get the train info
			JSONObject entity = jsonContent.getJSONObject("entity");
			JSONObject ldb = entity.getJSONObject("metadata").getJSONObject("ldb");
			
			//Update the page title every time the page is refreshed
			TextView pageTitle = (TextView) transportLayout.findViewById(R.id.transportTitle);
			pageTitle.setPadding(0, 10, 0, 10);
			String nrTime = ldb.getString("generatedAt").substring(0, 19) + " GMT";
			
			pageTitle.setText(jsonContent.getJSONObject("entity").getString("title") + " " + MyApplication.hourFormat.format
					(MyApplication.trainDateFormat.parse(nrTime)));
			
			LinearLayout trainLayout = (LinearLayout) transportLayout.findViewById(R.id.transportDetailsLayout);
			trainLayout.removeAllViews();
			
			LinearLayout stationLayout = parseTrainEntity(entity, page, trainLayout, page.getLayoutInflater());
			trainLayout.addView(stationLayout);
			
			//Change page title if needed
			if (TrainPage.board.equals(TrainPage.DEPARTURES))
			{
				TransportPage.transportPageTitle.setText("Rail Departures");
			}
			else if (TrainPage.board.equals(TrainPage.ARRIVALS))
			{
				TransportPage.transportPageTitle.setText("Rail Arrivals");
			}
		} catch (JSONException e)
		{
			e.printStackTrace();
			jsonException = true;
		} catch (ParseException e) {
			e.printStackTrace();
			parseException = true;
		} finally {
			TrainPageRefreshTask.trainNeedsRefresh = true;
		}
	}

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		try {
			if (params.length > 0)
			{
				return params[0];
			}
			else 
			{
				JSONObject jsonContent = MyApplication.router.onRequestSent(page.getName(), 
						page.getAdditionalParams(), Router.OutputFormat.JSON, page.getQuery());
				MyApplication.transportCache = jsonContent;
				return jsonContent;
			}
		} catch (SocketException e){
			e.printStackTrace();
			cancel(true);
			new TrainTask(page,toDestroyPageAfterFailure,dialogEnabled).execute();
		}catch (UnknownHostException e) {
			e.printStackTrace();
			unknownHostException = true;
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (IOException e) {
			e.printStackTrace();
			ioException = true;
		} catch (ParseException e) {
			e.printStackTrace();
			parseException = true;
		}
		return null;
	}
	
}
