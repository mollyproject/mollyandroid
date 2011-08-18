package org.mollyproject.android.view.apps.home;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.Router;

public class NetworkPollingTask extends BackgroundTask<Void,Void,ImageAdapter>
{
	//check for connection everytime the app is started and also spawn the location thread
	//if necessary
	protected boolean loaded;
	
	public NetworkPollingTask(HomePage homePage, boolean toDestroy, boolean dialog) {
		super(homePage,toDestroy, dialog);
		loaded = false;
	}

	@Override
	protected ImageAdapter doInBackground(Void... arg0) {
		try {
			//Establish a connection
			if (!loaded)
			{
				System.out.println("Router called");
	    		JSONObject output = page.getRouter().onRequestSent(
						page.getName(), null,
						Router.OutputFormat.JSON,null);
				
				JSONArray availableApps = output.getJSONArray("applications");
				List<String> appsList = new ArrayList<String>(); 
				for (int i = 0; i < availableApps.length(); i++)
				{
					JSONObject app = availableApps.getJSONObject(i);
					if (app.getBoolean("display_to_user"))
					{
						appsList.add(app.getString("local_name")+":index");
					}
				}
				return new ImageAdapter(page, appsList);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			malformedURLException = true;
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (NullPointerException e)
		{
			e.printStackTrace();
			nullPointerException = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			unknownHostException = true;
		} catch (IOException e) {
			e.printStackTrace();
			ioException = true;
		} catch (ParseException e) {
			e.printStackTrace();
			parseException = true;
		}
		return null;
	}
	
	@Override
	public void updateView(ImageAdapter newAdapter) {
		((HomePage) page).updateGrid(newAdapter);
		loaded = true;
	}
}