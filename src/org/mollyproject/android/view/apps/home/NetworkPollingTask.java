package org.mollyproject.android.view.apps.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;

public class NetworkPollingTask extends BackgroundTask<Void,Void,ImageAdapter>
{
	//check for connection everytime the app is started and also spawn the location thread
	//if necessary
	protected boolean appsLoaded;
	
	public NetworkPollingTask(HomePage homePage, boolean toDestroy, boolean dialog) {
		super(homePage,toDestroy, dialog);
		appsLoaded = false;
	}

	@Override
	protected ImageAdapter doInBackground(Void... arg0) {
		try {
			//Establish a connection
			if (!appsLoaded)
			{
				System.out.println("Router called");
	    		JSONObject output = MyApplication.router.requestJSON(
						page.getName(), null, null);
				
				JSONArray availableApps = output.getJSONArray("applications");
				MyApplication.availableApps = availableApps;
				List<JSONObject> appsList = new ArrayList<JSONObject>(); 
				for (int i = 0; i < availableApps.length(); i++)
				{
					JSONObject app = availableApps.getJSONObject(i);
					if (app.getBoolean("display_to_user"))
					{
						appsList.add(app);
					}
				}
				return new ImageAdapter(page, appsList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
		return null;
	}
	
	@Override
	public void updateView(ImageAdapter newAdapter) {
		((HomePage) page).updateList(newAdapter);
		appsLoaded = true;
		HomePage.firstHomeLoad = false;
	}
}