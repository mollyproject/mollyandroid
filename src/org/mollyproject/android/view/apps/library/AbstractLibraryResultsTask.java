package org.mollyproject.android.view.apps.library;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.ArrayListMultimap;

public abstract class AbstractLibraryResultsTask<A,B,C> extends BackgroundTask<A,B,C> {
	//display the library search results
	protected ArrayListMultimap<String,JSONObject> cache;
	protected List<JSONObject> cachedJSONPages;
	protected String query = new String();
	
	public AbstractLibraryResultsTask(LibraryResultsPage libraryResultsPage, boolean toDestroy, boolean dialog)
	{
		super(libraryResultsPage, toDestroy, dialog);
		cache = (ArrayListMultimap<String, JSONObject>) MyApplication.libraryCache;
		//get the query from page
		query = ((LibraryResultsPage) page).getQuery();
	}
	
	protected JSONObject getNextResultsPage(Page page) throws JSONException, UnknownHostException, IOException, ParseException
	{
		((LibraryResultsPage) page).increaseCurPageNum();
		int curPageNum = ((LibraryResultsPage) page).getCurPageNum();
		
		JSONObject nextJSONPage = new JSONObject();
		if (!cache.containsKey(query) || 
				(cache.containsKey(query) & cache.get(query).size()<=curPageNum))
		{
			JSONObject nextResults = MyApplication.router.requestJSON(
					page.getName(), null, ((LibraryResultsPage) page).getQuery()+"&page="+curPageNum);
			nextJSONPage = nextResults.getJSONObject("page");
			MyApplication.libraryCache.put(query, nextJSONPage);
		}
		else 
		{
			cachedJSONPages = (List<JSONObject>) cache.get(query);
			nextJSONPage = cachedJSONPages.get(curPageNum);
		}
		
		return nextJSONPage;
	}
	
	protected void addTextField(Page page, JSONObject jsonSource, String jsonKey, 
			String additionalText, ViewGroup parentViewGroup, float size) throws JSONException 
	{
		//add a text with the content specified in the json object to the specified page 
		if (!jsonSource.isNull(jsonKey))
		{
			LinearLayout textLayout = new LinearLayout(page);
			
			String text = jsonSource.getString(jsonKey);
			TextView textView = new TextView(page);
			textView.setTextColor(Color.WHITE); //somehow R.color doesn't work here
			textView.setTextSize(size);
			textView.setText(text);
			
			TextView moreTextView = new TextView(page);
			moreTextView.setTextColor(Color.WHITE); 
			moreTextView.setTextSize(16);
			moreTextView.setTypeface(Typeface.DEFAULT_BOLD);
			moreTextView.setText(additionalText);
			
			textLayout.addView(moreTextView);
			textLayout.addView(textView);
			
			parentViewGroup.addView(textLayout);
		}
	}
	
	protected void populateResults(final Page page, JSONObject nextJSONPage, 
			LinearLayout resultsLayout, TextView resultsNo) throws JSONException
	{
		//prepare the json and get the page of results needed to display
		int curPageNum = ((LibraryResultsPage) page).getCurPageNum();
		JSONArray books = nextJSONPage.getJSONArray("objects");
		TextView pageNumView = new TextView(page);
		pageNumView.setTextColor(R.color.blue);
		pageNumView.setText("Page "+curPageNum);
		pageNumView.setGravity(Gravity.CENTER);
		if (curPageNum > 1)
		{
			resultsLayout.addView(pageNumView);
		}
		//display each of the results and point to their corresponding library book result page
		for (int i = 0; i < books.length(); i++)
		{
			LinearLayout thisResultLayout = new LinearLayout(page);
			thisResultLayout.setOrientation(LinearLayout.VERTICAL);
			final JSONObject thisResult = books.getJSONObject(i);
			
			//next step: display the title, author, publishers, no. of available libraries
			addTextField(page,thisResult,"title","",thisResultLayout,18);
			addTextField(page,thisResult,"author","Author: ",thisResultLayout,16);
			addTextField(page,thisResult,"publisher","Publisher: ",thisResultLayout,16);
			addTextField(page,thisResult,"holding_libraries","Libraries: ",thisResultLayout,16);
			thisResultLayout.setBackgroundResource(R.drawable.bg_blue);
			thisResultLayout.setPadding(10, 10, 0, 10);
			thisResultLayout.setLayoutParams(Page.paramsWithLine);
			
			thisResultLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						MyApplication.bookControlNumber = thisResult.getString("control_number");
						Intent myIntent = new Intent(page,MyApplication.getPageClass(MollyModule.LIBRARY_BOOK_RESULT_PAGE));
						page.startActivityForResult(myIntent, 0);
					} catch (JSONException e) {
						e.printStackTrace();
						//To-do: pop up error
					}
				}
			});
			
			resultsLayout.addView(thisResultLayout);
		}
		
		if (nextJSONPage.getInt("num_objects") == 0)
		{
			resultsNo.setText("Search cannot find anything. Either there is a problem with your query" +
					" or server is not responding");
		}
		else
		{
			resultsNo.setText("Search returned " + nextJSONPage.getString("num_objects") + " items." + '\n'
			+ "Displaying "+curPageNum+" page(s) of "+nextJSONPage.getString("num_pages")+" pages. " +
					"Notice: these results are unordered.");
		}
	}

}
