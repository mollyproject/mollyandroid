package org.mollyproject.android.view.apps.results_release;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.text.Html;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultReleaseTask extends BackgroundTask<JSONObject, Void, JSONObject>{

	//protected ArrayListMultimap<Date,String> examsByDate;
	public ResultReleaseTask(ResultsReleasePage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(JSONObject examsByDate) {
		try {
			LayoutInflater inflater = page.getLayoutInflater();
			((ResultsReleasePage) page).getContentLayout().removeAllViews();
			
			LinearLayout releasesLayout = (LinearLayout) inflater.inflate(R.layout.general_search_results_page, 
					null);
			page.getContentLayout().addView(releasesLayout);
			
			TextView headerText = (TextView) releasesLayout.findViewById(R.id.searchResultsHeader);
			headerText.setText("Sorted by Latest first");
			
			LinearLayout resultsLayout = (LinearLayout) releasesLayout.findViewById(R.id.generalResultsList);
			
			@SuppressWarnings("unchecked")
			Iterator<String> dates = examsByDate.keys();
			
			while(dates.hasNext())
			{
				LinearLayout thisResult = (LinearLayout) inflater.inflate
						(R.layout.plain_text_search_result, ((ResultsReleasePage) page).getContentLayout(),false);
				thisResult.setLayoutParams(Page.paramsWithLine);
				String allText = new String();
				
				String myDate = dates.next();
				allText = "<b>"+ myDate + "</b>";
				
				//List<String> results = (List<String>) examsByDate.getString(myDate);
				JSONArray results = examsByDate.getJSONArray(myDate);
				for (int i = 0; i < results.length(); i++)
				{
					String title = results.getString(i);
					allText = allText + "<br/>" + title;
				}
				TextView resultsText = ((TextView) thisResult.findViewById(R.id.plainTextResultText));
				resultsText.setText(Html.fromHtml(allText));
				resultsLayout.addView(thisResult);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
		((ContentPage) page).doneProcessingJSON();
	}

	@Override
	protected  JSONObject doInBackground(JSONObject... params) {
		try {
			while (!((ContentPage) page).downloadedJSON())
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		
			JSONObject jsonContent = ((ContentPage) page).getJSONContent();
			//Process the json text received
			JSONArray entries = (JSONArray) jsonContent.get("entries");
			JSONObject examsByDate = new JSONObject();
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
					String myDate = MyApplication.myDateFormat.format(updatedDate);
					if (!examsByDate.has(myDate))
					{
						examsByDate.put(myDate, new JSONArray());
					}
					examsByDate.getJSONArray(myDate).put(title);
					//examsByDate.put(MyApplication.myDateFormat.format(updatedDate), title); // time in hourless format
				}
			}
			return examsByDate;
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
