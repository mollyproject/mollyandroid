package org.mollyproject.android.view.apps.transport;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.transport.bus.BusPage;
import org.mollyproject.android.view.apps.transport.par.ParkAndRidePage;
import org.mollyproject.android.view.apps.transport.train.TrainPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.widget.TabHost;

public class TransportPageTask extends BackgroundTask<Void, Void, String>{

	public TransportPageTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(String outputs) {
		System.out.println(outputs);
		JSONObject jsonContent = ((ContentPage) page).getJSONContent();
		MyApplication.transportCache = jsonContent;
        try {
        	Intent myIntent;
    		TabHost.TabSpec spec;
    		Resources res = page.getResources();
    		String tabTag = new String();
			if (jsonContent.getJSONObject("public_transport").getBoolean("bus") == true & TransportPage.firstLoad == true)
			{
				//Bus tab
				tabTag = TransportPage.BUS;
				myIntent = new Intent().setClass(page.getApplicationContext(), BusPage.class);
			    spec = TransportPage.transportTabHost.newTabSpec(tabTag)
			    	.setIndicator("", res.getDrawable(R.drawable.bus_blue)).setContent(myIntent);
			    TransportPage.transportTabHost.addTab(spec);
			}
			if (jsonContent.getBoolean("train_station") == true & TransportPage.firstLoad == true)
			{
				//Train tab
				tabTag = TransportPage.RAIL;
			    myIntent = new Intent().setClass(page.getApplicationContext(), TrainPage.class);
			    spec = TransportPage.transportTabHost.newTabSpec(tabTag)
			    	.setIndicator("", res.getDrawable(R.drawable.train_blue)).setContent(myIntent);
			    TransportPage.transportTabHost.addTab(spec);
			} 
			//Park and rides tab
			tabTag = TransportPage.PAR;
		    myIntent = new Intent().setClass(page.getApplicationContext(), ParkAndRidePage.class);
		    spec = TransportPage.transportTabHost.newTabSpec(tabTag)
		    	.setIndicator("", res.getDrawable(R.drawable.pride_blue)).setContent(myIntent);
		    TransportPage.transportTabHost.addTab(spec);
			
			((ContentPage) page).doneProcessingJSON();
			SharedPreferences settings = page.getSharedPreferences(MyApplication.PREFS_NAME, 0);
			if (settings.contains("lastTab"))
			{
				//somehow settings doesn't work in this task for the first run of the app so I use public static
				System.out.println("Transport task " + settings.getString("lastTab",MyApplication.lastTransportTag));
				TransportPage.transportTabHost.setCurrentTabByTag
								(settings.getString("lastTab", MyApplication.lastTransportTag));
			}
			
			((ContentPage) page).doneProcessingJSON();
        } catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
		TransportPage.mlam.dispatchResume();
	}
	@Override
	protected String doInBackground(Void... arg0) {
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return "Do not return null here";
	}

}
