package org.mollyproject.android.view.pages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import com.google.common.collect.ArrayListMultimap;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ResultsPage extends ContentPage {
	protected Router router;
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		DateFormat defaultDateFormat = new SimpleDateFormat
				("EEE, d MMM yyyy HH:mm:ss Z");
		DateFormat myDateFormat = new SimpleDateFormat
				("d MMM yyy");
		
		ScrollView scr = new ScrollView(getApplicationContext());
		router = myApp.getRouter();
		String jsonText = null;
		
		LinearLayout allText = new LinearLayout(getApplicationContext());
		allText.setOrientation(LinearLayout.VERTICAL);
		
		ArrayListMultimap<Date,String> examsByDate = ArrayListMultimap.create();
		try {
			jsonText = router.onRequestSent(SelectionManager.getName(ResultsPage.class));
			//Process the json text received
			if (jsonText != null)
			{
				JSONObject jsonObj = new JSONObject(jsonText);
				JSONArray entries = (JSONArray) jsonObj.get("entries");
				if (entries.length() > 0)
				{

					for (int i = 0; i < entries.length(); i++)
					{
						JSONObject entry = entries.getJSONObject(i);
						//trim off result
						String title = entry.getString("course_code") + ": " +
								entry.getString("title")
								.replace("OxfordExams: Results for ","")
								.replace(" now available", "");
						title = title.substring(0, title.length() - 7);
						
						Date updatedDate = defaultDateFormat.parse
											(entry.getString("updated"));
						Date myDate = new Date(myDateFormat.format(updatedDate));
						examsByDate.put(myDate, title);
						//System.out.println(myDate+" "+title);
					}
				}
			}
			
			TreeSet<Date> sortedDates  = new TreeSet<Date>(Collections.reverseOrder());
			sortedDates.addAll(examsByDate.keySet());
			Iterator<Date> dates = sortedDates.iterator();
			while(dates.hasNext())
			{
				Date thisDate = (dates.next());
				TextView dateView = new TextView(getApplicationContext());
				dateView.setTextSize(24);
				dateView.setText('\n'+myDateFormat.format(thisDate));
				allText.addView(dateView);
				
				List<String> results = (List<String>) examsByDate.get(thisDate);
				for (String result : results)
				{
					String thisResult = result;
					TextView resultView = new TextView(getApplicationContext());
					resultView.setTextSize(18);
					resultView.setText(thisResult);
					allText.addView(resultView);
				}
			}
		} catch (Exception e) {
			String tryAgain = "There is a problem with loading the page." +'\n' +
					"Please try again later.";
			TextView tryAgainView = new TextView(getApplicationContext());
			tryAgainView.setText(tryAgain);
			allText.addView(tryAgainView);
		}
		scr.addView(allText);
		contentLayout.addView(scr,ViewGroup.LayoutParams.FILL_PARENT);
		setContentView(contentLayout);
	}
	
	public Page getInstance()
	{
		System.out.println("Called "+this);
		return this;
	}
	

}
