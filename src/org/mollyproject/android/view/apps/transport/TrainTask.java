package org.mollyproject.android.view.apps.transport;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.Page;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TrainTask extends BackgroundTask<JSONObject, Void, JSONObject> {

	public TrainTask(TrainPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try
		{
			LinearLayout transportLayout = ((TrainPage) page).getContentLayout(); //this is the transportLayout in transport_layout.xml
			LayoutInflater layoutInflater = page.getLayoutInflater();
			
			LinearLayout trainLayout = (LinearLayout) transportLayout.findViewById(R.id.transportDetailsLayout);
			trainLayout.removeAllViews();
			
			//get the train info
			JSONObject entity = jsonContent.getJSONObject("entity");
			JSONArray services = entity.getJSONObject("metadata").getJSONObject("ldb")
					.getJSONObject("trainServices").getJSONArray("service");
			
			//Update the page title every time the page is refreshed
			TextView pageTitle = (TextView) transportLayout.findViewById(R.id.transportTitle);
			pageTitle.setText(entity.getString("title") + " " + TransportPage.hourFormat.format(new Date()));
			
			//Header
			LinearLayout header = (LinearLayout) layoutInflater.inflate(R.layout.transport_train_result, 
					((TrainPage) page).getContentLayout(),false);
			header.setLayoutParams(Page.paramsWithLine);
			((TextView) header.findViewById(R.id.trainDestination)).setTypeface(Typeface.DEFAULT_BOLD);
			((TextView) header.findViewById(R.id.platformNumber)).setTypeface(Typeface.DEFAULT_BOLD);
			((TextView) header.findViewById(R.id.trainScheduledTime)).setTypeface(Typeface.DEFAULT_BOLD);
			((TextView) header.findViewById(R.id.trainExpectedTime)).setTypeface(Typeface.DEFAULT_BOLD);
			
			trainLayout.addView(header);
			
			for (int i = 0; i < services.length(); i++)
			{
				JSONObject service = services.getJSONObject(i);
				
				LinearLayout trainResultLayout = (LinearLayout) layoutInflater.inflate(R.layout.transport_train_result, 
						((TrainPage) page).getContentLayout(),false);
				
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
				scheduledTime.setText(service.getString("std"));
				
				TextView expectedTime = (TextView) trainResultLayout.findViewById(R.id.trainExpectedTime);
				expectedTime.setText(service.getString("etd"));
				
				if (service.getBoolean("problems"))
				{
					destination.setTextColor(Color.RED);
					destination.setText(destination.getText() + "(!)");
					platform.setTextColor(Color.RED);
					scheduledTime.setTextColor(Color.RED);
					expectedTime.setTextColor(Color.RED);
				}
				
				trainResultLayout.setLayoutParams(Page.paramsWithLine);
				trainLayout.addView(trainResultLayout);
			}
		} catch (JSONException e)
		{
			e.printStackTrace();
			jsonException = true;
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
				JSONObject jsonContent = page.getRouter().onRequestSent(page.getName(), page.getAdditionalParams(), 
						Router.OutputFormat.JSON, null);
				myApp.setTransportCache(jsonContent);
				return jsonContent;
			}
		} catch (UnknownHostException e) {
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
