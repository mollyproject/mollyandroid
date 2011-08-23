package org.mollyproject.android.view.apps.transport;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

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
		((MyApplication) page.getApplication()).setTransportCache(jsonContent);
		((ContentPage) page).doneProcessingJSON();
        try {
        	Intent myIntent;
    		TabHost.TabSpec spec;
    		Resources res = page.getResources();
    		String tabTag = new String();
			if (jsonContent.getJSONObject("public_transport").getBoolean("bus") == true & ((TransportPage)page).firstLoad)
			{
				tabTag = "Bus";
				myIntent = new Intent().setClass(page.getApplicationContext(), BusPage.class);
			    spec = ((TransportPage) page).tabHost.newTabSpec("Bus")
			    	.setIndicator("Bus", res.getDrawable(R.drawable.android_button)).setContent(myIntent);
			    ((TransportPage) page).tabHost.addTab(spec);
			}
			if (jsonContent.getBoolean("train_station") == true & ((TransportPage)page).firstLoad)
			{
				tabTag = "Train";
			    myIntent = new Intent().setClass(page.getApplicationContext(), TrainPage.class);
			    spec = ((TransportPage) page).tabHost.newTabSpec("Train")
			    	.setIndicator("Train", res.getDrawable(R.drawable.android_button)).setContent(myIntent);
			    ((TransportPage) page).tabHost.addTab(spec);
			}
			((ContentPage) page).doneProcessingJSON();
			/*SharedPreferences settings = page.getSharedPreferences(MyApplication.PREFS_NAME, 0);
			if (settings.contains("lastTab"))
			{
				((TransportPage) page).tabHost.setCurrentTabByTag(settings.getString("lastTab",tabTag));
			}*/
        } catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
		((TransportPage) page).getLAM().dispatchResume();
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
		System.out.println("HERE");
		
		return "Do not return null here";
	}

}
