package org.mollyproject.android.view.apps.weblearn.signup;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.widget.LinearLayout;
import android.widget.TextView;

public class WebLearneventEventTask extends JSONProcessingTask {

	public WebLearneventEventTask(ContentPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			LinearLayout eventsLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.general_search_results_page, null);
			
			((TextView) eventsLayout.findViewById(R.id.searchResultsHeader)).setText("Available Events");
			
			JSONArray events = jsonContent.getJSONArray("events");
			LinearLayout eventsList = (LinearLayout) eventsLayout.findViewById(R.id.generalResultsList);
			eventsList.setBackgroundResource(R.drawable.shape_white);
			if (events.length() == 0)
			{
				
			}
			else 
			{
				//Sort the events:
				//events.
				TreeSet<JSONObject> sortedEvents = new TreeSet<JSONObject>(new JSONStartDateComparator());
				for (int i = 0; i < events.length(); i++)
				{
					sortedEvents.add(events.getJSONObject(i));
				}
				Iterator<JSONObject> eventsIter = sortedEvents.iterator();
				while(eventsIter.hasNext())
				{
					JSONObject event = eventsIter.next();
					//MyApplication.weblearnSignupSlug = jsonContent.getString("site");
					//MyApplication.weblearnEventId = event.getString("id");
					LinearLayout eventLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.weblearn_signup_event, null);
					eventLayout.setLayoutParams(Page.paramsWithLine);
					((TextView) eventLayout.findViewById(R.id.eventTitle)).setText(event.getString("title"));
					
					//start/end dates
					Date start = MyApplication.defaultDateFormat.parse(event.getString("start"));
					Date end = MyApplication.defaultDateFormat.parse(event.getString("end"));
					
					eventsList.addView(eventLayout);
				}
			}
			
			page.getContentLayout().removeAllViews();
			page.getContentLayout().addView(eventsLayout);
			
			((ContentPage) page).doneProcessingJSON();
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
	}
	
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		if(Page.manualRefresh)
		{
			return super.doInBackground();
		}
		
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
	
	private class JSONStartDateComparator implements Comparator<JSONObject>
	{

		@Override
		public int compare(JSONObject arg0, JSONObject arg1) {
			try {
				Date date0 = MyApplication.defaultDateFormat.parse(arg0.getString("start"));
				Date date1 = MyApplication.defaultDateFormat.parse(arg1.getString("start"));
				return date0.compareTo(date1);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			
		}
		
	}
}
