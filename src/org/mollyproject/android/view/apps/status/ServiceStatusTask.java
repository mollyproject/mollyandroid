package org.mollyproject.android.view.apps.status;

import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.transport.TransportPage;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServiceStatusTask extends BackgroundTask<Void,Void,JSONObject> {
	public final static String UP = "up";
	public final static String DOWN = "down";
	public final static String UNKNOWN = "unknown";
	public final static String PARTIAL = "partial";
	public ServiceStatusTask(ServiceStatusPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			LinearLayout contentLayout = ((ContentPage) page).getContentLayout();
			LayoutInflater layoutInflater = page.getLayoutInflater();
			//get all the services in json
			JSONArray services = jsonContent.getJSONArray("services");
			
			for (int i = 0; i < services.length(); i++)
			{
				LinearLayout serviceLayout = (LinearLayout) layoutInflater.inflate
							(R.layout.service_status, contentLayout, false);
				contentLayout.removeAllViews();
				contentLayout.addView(serviceLayout);
				
				JSONObject service = services.getJSONObject(i);
				
				String lastUpdated = service.getString("last_updated").substring(0, 19) + " GMT" + 
						service.getString("last_updated").substring(19, 25);
				System.out.println(lastUpdated);
				//pageTitle.setText(jsonContent.getJSONObject("entity").getString("title") + " " + TransportPage.hourFormat.format
					//	(MyApplication.trainDateFormat.parse(nrTime)));
				
				//String name = service.getString("name");
				((TextView) serviceLayout.findViewById(R.id.serviceTitle)).setText(service.getString("name") 
						+ " last updated at " + MyApplication.hourFormat.format
						(MyApplication.updateDateFormat.parse(lastUpdated)));
				((TextView) serviceLayout.findViewById(R.id.serviceTitle)).setTextSize(18);
				
				JSONArray announcements = service.getJSONArray("announcements");
				
				JSONArray subServices = service.getJSONArray("services");
				
				LinearLayout subServicesLayout = (LinearLayout) serviceLayout
								.findViewById(R.id.subServicesLayout);
				
				for (int j = 0; j < subServices.length(); j ++)
				{
					JSONObject subService = subServices.getJSONObject(j);
					
					LinearLayout subServiceLayout = (LinearLayout) layoutInflater.inflate
							(R.layout.general_search_result, serviceLayout, false);
					subServicesLayout.addView(subServiceLayout);
					subServiceLayout.setLayoutParams(Page.paramsWithLine);
					//String subName = subService.getString("name");
					((TextView) subServiceLayout.findViewById(R.id.generalSearchText))
									.setText(subService.getString("name"));
					
					String status = subService.getString("status");
					//Look up the right image
					((ImageView) subServiceLayout.findViewById(R.id.generalSearchIcon))
								.setImageResource(MyApplication.getImgResourceId(status));
				}
			}
			((ContentPage) page).doneProcessingJSON();
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (ParseException e) {
			e.printStackTrace();
			parseException = true;
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
