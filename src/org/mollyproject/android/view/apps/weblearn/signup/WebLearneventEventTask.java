package org.mollyproject.android.view.apps.weblearn.signup;

import java.text.DateFormatSymbols;
import java.util.Calendar;
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
	String monthNames[] = new DateFormatSymbols().getMonths();
	String dayNames[] = new DateFormatSymbols().getWeekdays();
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
					Calendar start = Calendar.getInstance(); 
					start.setTime(MyApplication.defaultDateFormat.parse(event.getString("start")));
					Calendar end = Calendar.getInstance(); 
					end.setTime(MyApplication.defaultDateFormat.parse(event.getString("end")));
					
					TextView dateText = (TextView) eventLayout.findViewById(R.id.eventDate);
					if (start.get(Calendar.YEAR) == end.get(Calendar.YEAR))
					{
						//same-year event
						if (start.get(Calendar.MONTH) == end.get(Calendar.MONTH))
						{
							//same month
							if (start.get(Calendar.DATE) == end.get(Calendar.DATE))
							{
								//same-day
								//Date: startHour/Minute - endHour/Minute : WeekDay Date Month   
								dateText.setText(start.get(Calendar.HOUR) +":" + start.get(Calendar.MINUTE) + start.get(Calendar.AM_PM) +
										" - " + end.get(Calendar.HOUR) +":" + end.get(Calendar.MINUTE) + end.get(Calendar.AM_PM) + ": " +
										dayNames[start.get(Calendar.DAY_OF_WEEK)] + " " + start.get(Calendar.DAY_OF_MONTH) + monthNames[start.get(Calendar.MONTH)]);
							}
							else
							{
								//different day
								//Date: startHour/Minute startWeekDay startDate - endHour/Minute endWeekDay endDate Month
								dateText.setText(start.get(Calendar.HOUR) +":" + start.get(Calendar.MINUTE) + start.get(Calendar.AM_PM) +
										dayNames[start.get(Calendar.DAY_OF_WEEK)] + " " + start.get(Calendar.DAY_OF_MONTH) + " - " +
										end.get(Calendar.HOUR) +":" + end.get(Calendar.MINUTE) + end.get(Calendar.AM_PM) + " " +
										dayNames[end.get(Calendar.DAY_OF_WEEK)] + " " + end.get(Calendar.DAY_OF_MONTH) + ", " + 
										start.get(Calendar.MONTH));
							}
						}
						else
						{
							//different month
							//Date: startHour/Minute startWeekDay startDate startMonth - endHour/Minute endWeekDay endDate endMonth 
							dateText.setText(start.get(Calendar.HOUR) +":" + start.get(Calendar.MINUTE) + start.get(Calendar.AM_PM) +
									dayNames[start.get(Calendar.DAY_OF_WEEK)] + " " + start.get(Calendar.DAY_OF_MONTH) + monthNames[start.get(Calendar.MONTH)] + " - " +
									end.get(Calendar.HOUR) +":" + end.get(Calendar.MINUTE) + end.get(Calendar.AM_PM) + " " +
									dayNames[end.get(Calendar.DAY_OF_WEEK)] + " " + end.get(Calendar.DAY_OF_MONTH) + monthNames[end.get(Calendar.MONTH)]);
						}
					}
					else
					{
						//event in different years
						//Date: startHour/Minute startWeekDay startDate startMonth startYear - endHour/Minute endWeekDay endDate endMonth endYear 
						dateText.setText(start.get(Calendar.HOUR) +":" + start.get(Calendar.MINUTE) + start.get(Calendar.AM_PM) +
								dayNames[start.get(Calendar.DAY_OF_WEEK)] + " " + start.get(Calendar.DAY_OF_MONTH) + monthNames[start.get(Calendar.MONTH)] + " " + start.get(Calendar.YEAR) + " - " +
								end.get(Calendar.HOUR) +":" + end.get(Calendar.MINUTE) + end.get(Calendar.AM_PM) + " " +
								dayNames[end.get(Calendar.DAY_OF_WEEK)] + " " + end.get(Calendar.DAY_OF_MONTH) + monthNames[end.get(Calendar.MONTH)] + " " + end.get(Calendar.YEAR));
					}
					
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
