package org.mollyproject.android.view.apps.results_release;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import android.text.Html;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultReleaseTask extends BackgroundTask<JSONObject, Void, ListMultimap<Date,String>>{

	//protected ArrayListMultimap<Date,String> examsByDate;
	public ResultReleaseTask(ResultsReleasePage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(ListMultimap<Date,String> examsByDate) {
		// TODO Auto-generated method stub
		if (examsByDate != null)
		{
			TreeSet<Date> sortedDates  = new TreeSet<Date>(Collections.reverseOrder());
			sortedDates.addAll(examsByDate.keySet());
			Iterator<Date> dates = sortedDates.iterator();
			
			LayoutInflater inflater = page.getLayoutInflater();
			LinearLayout releasesLayout = (LinearLayout) inflater.inflate(R.layout.general_search_results_page, 
					((ResultsReleasePage) page).getContentLayout(), false);
			((ResultsReleasePage) page).getContentLayout().addView(releasesLayout);
			
			TextView headerText = (TextView) releasesLayout.findViewById(R.id.searchResultsHeader);
			headerText.setText("Sorted by Latest first");
			
			LinearLayout resultsLayout = (LinearLayout) releasesLayout.findViewById(R.id.generalResultsList);
			
			while(dates.hasNext())
			{
				LinearLayout thisResult = (LinearLayout) inflater.inflate
						(R.layout.plain_text_search_result, ((ResultsReleasePage) page).getContentLayout(),false);
				thisResult.setLayoutParams(Page.paramsWithLine);
				String allText = new String();
				
				Date thisDate = (dates.next());
				allText = "<b>"+ "<font size = 24>" + MyApplication.myDateFormat.format(thisDate) 
						+ "</font>" + "</b>";
				
				List<String> results = (List<String>) examsByDate.get(thisDate);
				for (String result : results)
				{
					allText = allText + "<br/>" + result;
				}
				TextView resultsText = ((TextView) thisResult.findViewById(R.id.plainTextResultText));
				resultsText.setText(Html.fromHtml(allText));
				resultsLayout.addView(thisResult);
			}
		}
	}

	@Override
	protected  ListMultimap<Date,String> doInBackground(JSONObject... args) {
		// TODO Auto-generated method stub
		ListMultimap<Date,String> examsByDate = ArrayListMultimap.create();
		try {
			JSONObject output = args[0];
			
			//Process the json text received
			if (output != null)
			{
				JSONArray entries = (JSONArray) output.get("entries");
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
						
						Date updatedDate = MyApplication.defaultDateFormat.parse
											(entry.getString("updated"));
						Date myDate = new Date(MyApplication.myDateFormat.format(updatedDate));
						examsByDate.put(myDate, title);
						//System.out.println(myDate+" "+title);
					}
				}
				System.out.println(output.toString(2));
				return examsByDate;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
			
		} catch (ParseException e) {
			e.printStackTrace();
			parseException = true;
		} catch (NullPointerException e) {
			e.printStackTrace();
			nullPointerException = true;
		}
		return null;
	}

}
