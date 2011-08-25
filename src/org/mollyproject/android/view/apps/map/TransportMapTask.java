package org.mollyproject.android.view.apps.map;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.transport.BusPageRefreshTask;
import org.mollyproject.android.view.apps.transport.TrainPageRefreshTask;

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
		
		try {
			JSONObject jsonContent = ((ContentPage) page).getJSONContent();
			MyApplication.transportCache = jsonContent;
			JSONObject metadata = jsonContent.getJSONObject("entity").getJSONObject("metadata");
			
			//Must distinguish between bus stops and train stations
			if (metadata.has("real_time_information"))
			{
				//parse entity of bus
				new BusPageRefreshTask(page, false, false).execute();
			}
			else if (metadata.has("ldb"))
			{
				//parse entity of rail
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
		return null;
	}

}
