package org.mollyproject.android.view.apps.library;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.Page;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.ArrayListMultimap;

public abstract class LibraryResultsTask<A,B,C> extends BackgroundTask<A,B,C> {
	//display the library search results
	protected ArrayListMultimap<String,JSONObject> cache;
	protected List<JSONObject> cachedJSONPages;
	protected String query = new String();
	
	public LibraryResultsTask(LibraryResultsPage libraryResultsPage, boolean toDestroy, boolean dialog)
	{
		super(libraryResultsPage, toDestroy, dialog);
		cache = (ArrayListMultimap<String, JSONObject>) 
			((MyApplication) page.getApplication()).getLibCache();
		//get the query from myApp
		Map<String,String> bookArgs = ((MyApplication) page.getApplication()).getLibraryArgs();
		for (String key : bookArgs.keySet())
		{
			if (bookArgs.get(key).length() > 0)
			{
				query = query + "&" + key + "=" + bookArgs.get(key);
			}
		}
	}
	
	protected JSONObject getNextResultsPage(Page page) throws JSONException
	{
		int curPageNum = ((LibraryResultsPage) page).getCurPageNum();
		JSONObject nextJSONPage = new JSONObject();
		if (!cache.containsKey(query) || 
				(cache.containsKey(query) & cache.get(query).size()<=curPageNum))
		{
			JSONObject nextResults = page.getRouter().exceptionHandledOnRequestSent(
					page.getName(), null,
					page, Router.OutputFormat.JSON, query+"&page="+curPageNum);
			nextJSONPage = nextResults.getJSONObject("page");
			((MyApplication) page.getApplication()).updateLibCache(query, nextJSONPage);
		}
		else 
		{
			cachedJSONPages = (List<JSONObject>) cache.get(query);
			nextJSONPage = cachedJSONPages.get(curPageNum);
		}
		((LibraryResultsPage) page).increaseCurPageNum();
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
			textView.setTextSize(size);
			textView.setText(text);
			
			TextView moreTextView = new TextView(page);
			moreTextView.setTextSize(16);
			moreTextView.setTypeface(Typeface.DEFAULT_BOLD);
			moreTextView.setText(additionalText);
			
			textLayout.addView(moreTextView);
			textLayout.addView(textView);
			
			parentViewGroup.addView(textLayout);
		}
	}
	
	protected void populateResults(Page page, JSONObject nextJSONPage, 
			LinearLayout resultsLayout, TextView resultsNo) throws JSONException
	{
		//prepare the json and get the page of results needed to display
		int curPageNum = ((LibraryResultsPage) page).getCurPageNum();
		JSONArray newObjects = nextJSONPage.getJSONArray("objects");
		TextView pageNumView = new TextView(page);
		pageNumView.setTextColor(R.color.blue);
		pageNumView.setText("Page "+curPageNum);
		pageNumView.setGravity(Gravity.CENTER);
		if (curPageNum > 1)
		{
			resultsLayout.addView(pageNumView);
		}
		for (int i = 0; i < newObjects.length(); i++)
		{
			LinearLayout thisResultLayout = new LinearLayout(page);
			thisResultLayout.setOrientation(LinearLayout.VERTICAL);
			JSONObject thisResult = newObjects.getJSONObject(i);
			
			//next step: display the title, author, publishers, no. of available libraries
			addTextField(page,thisResult,"title","",thisResultLayout,18);
			addTextField(page,thisResult,"author","Author: ",thisResultLayout,16);
			addTextField(page,thisResult,"publisher","Publisher: ",thisResultLayout,16);
			addTextField(page,thisResult,"holding_libraries","Libraries: ",thisResultLayout,16);
			thisResultLayout.setBackgroundResource(R.drawable.bg_blue);
			thisResultLayout.setPadding(10, 10, 0, 10);
			thisResultLayout.setLayoutParams(Page.paramsWithLine);	
			resultsLayout.addView(thisResultLayout);
		}
		
		if (nextJSONPage.getInt("num_objects") == 0)
		{
			resultsNo.setText("Search cannot find anything. Either there is a problem with your query" +
					" or OLIS is not responding");
		}
		else
		{
			resultsNo.setText("Search returned " + nextJSONPage.getString("num_objects") + " items." + '\n'
			+ "Displaying "+curPageNum+" page(s) of "+nextJSONPage.getString("num_pages")+" pages. " +
					"Notice: these results are unordered.");
		}
	}

}
