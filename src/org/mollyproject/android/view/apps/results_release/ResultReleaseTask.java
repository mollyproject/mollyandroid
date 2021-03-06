package org.mollyproject.android.view.apps.results_release;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultReleaseTask extends JSONProcessingTask{
	String monthNames[] = new DateFormatSymbols().getMonths();
	public ResultReleaseTask(ContentPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(JSONObject examsByDate) {
		try {
			LinearLayout releasesLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.results_release_page, null);
			
			TextView headerText = (TextView) releasesLayout.findViewById(R.id.releasesHeader);
			String header = "<b> Recent releases </b> <br/> <br/>" + "<small> This page lists the most recent schools to have had results released. " +
					"You can check your results by logging in to the Student Self-Service website: </small>";
			headerText.setText(Html.fromHtml(header));
			
			Button selfServiceButton = (Button) releasesLayout.findViewById(R.id.selfServiceButton);
			selfServiceButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent myIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.studentsystem.ox.ac.uk/"));
					page.startActivity(Intent.createChooser(myIntent, "Going to http://www.studentsystem.ox.ac.uk/..."));
				}
			});
			
			LinearLayout resultsLayout = (LinearLayout) releasesLayout.findViewById(R.id.releasesList);
			
			//sort the keys first
			Iterator<String> dates = examsByDate.keys();
			TreeSet<String> sortedDates = new TreeSet<String>(new DateComparator());
			while(dates.hasNext())
			{
				sortedDates.add(dates.next());
			}
			//sortedDates should contain all the sorted dates now
			
			Iterator<String> newDates = sortedDates.iterator();
			while(newDates.hasNext())
			{
				String myDate = newDates.next();
				JSONArray results = examsByDate.getJSONArray(myDate);
				LinearLayout thisDateResult = (LinearLayout) page.getLayoutInflater().inflate(R.layout.plain_text_search_result,null);
				thisDateResult.setLayoutParams(Page.paramsWithLine);
				TextView dateText = ((TextView) thisDateResult.findViewById(R.id.plainTextResultText));
				dateText.setText(Html.fromHtml("<b>" + myDate + "</b>"));
				resultsLayout.addView(thisDateResult);
				for (int i = 0; i < results.length(); i++)
				{
					String title = results.getString(i);
					LinearLayout thisResult = (LinearLayout) page.getLayoutInflater().inflate(R.layout.plain_text_search_result,null);
					thisResult.setBackgroundResource(R.drawable.bg_white);
					thisResult.setLayoutParams(Page.paramsWithLine);
					TextView resultText = ((TextView) thisResult.findViewById(R.id.plainTextResultText));
					resultText.setTextColor(page.getResources().getColor(R.color.blue));
					resultText.setText(title);
					resultsLayout.addView(thisResult);
				}
			}
			
			((ContentPage) page).doneProcessingJSON();
			
			((ContentPage) page).getContentLayout().removeAllViews();
			page.getContentLayout().addView(releasesLayout);
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
		((ContentPage) page).doneProcessingJSON();
	}

	@Override
	protected  JSONObject doInBackground(JSONObject... params) {
		JSONObject jsonContent;
		if (Page.manualRefresh)
		{
			jsonContent = super.doInBackground();
		}
		else
		{
			while (!((ContentPage) page).downloadedJSON())
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			jsonContent = ((ContentPage) page).getJSONContent();
		}
		
		try {
			
			//Process the json text received
			//What this part of the code does is that it groups the titles of the exams into groups by date 
			//in another JSONObject and passes it to the updateView for later ease 
			JSONArray entries = (JSONArray) jsonContent.get("entries");
			JSONObject examsByDate = new JSONObject();
			if (entries.length() > 0)
			{
				for (int i = 0; i < entries.length(); i++)
				{
					JSONObject entry = entries.getJSONObject(i);
					//trim off result
					String title = entry.getString("title")
							.replace("OxfordExams: Results for ","")
							.replace(" now available", "");
					title = title.substring(0, title.length() - 7);
					
					Date updatedDate = MyApplication.defaultDateFormat.parse
										(entry.getString("published"));
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(updatedDate);
					String myDate = calendar.get(Calendar.DATE) + " " + monthNames[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
					if (!examsByDate.has(myDate))
					{
						examsByDate.put(myDate, new JSONArray());
					}
					examsByDate.getJSONArray(myDate).put(title);
				}
			}
			return examsByDate;
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
		return null;
	}
	
	private class DateComparator implements Comparator<String> 
	{
		@Override
		public int compare(String object1, String object2) {
			try {
				Date date1 = MyApplication.myDateFormat.parse(object1);
				Date date2 = MyApplication.myDateFormat.parse(object2);
				
				return date2.compareTo(date1); // Latest date first
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}
		
	}

}
