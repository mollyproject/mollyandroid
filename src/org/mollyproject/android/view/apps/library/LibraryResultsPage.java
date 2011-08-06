package org.mollyproject.android.view.apps.library;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import com.google.common.collect.ArrayListMultimap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LibraryResultsPage extends ContentPage {

	protected int curPageNum;
	protected String query;
	protected ArrayListMultimap<String,JSONObject> cache;
	protected List<JSONObject> cachedJSONPages;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		curPageNum = 0;
		cache = myApp.getLibCache();
		query = myApp.getLibraryQuery().toLowerCase();
		
		new LibraryFirstResultTask(this,false).execute();
	}
	
	private List<View> connectAndGenerate(Page page, String queryWithPage) throws JSONException
	{
		router.waitForRequests();
		JSONObject results = router.exceptionHandledOnRequestSent(MollyModule.getName(this.getClass()),
				this, Router.OutputFormat.JSON, queryWithPage);
		
		return generatePage(this, results);
	}
	
	private List<View> generatePage(final Page page, JSONObject results) throws JSONException
	{
		List<View> outputs = new ArrayList<View>();
		final JSONObject jsonPage = results.getJSONObject("page");
		
		final ScrollView scr = new ScrollView(page);
		
		final LinearLayout pageLayout = new LinearLayout(page);
		pageLayout.setOrientation(LinearLayout.VERTICAL);
		pageLayout.setBackgroundResource(R.drawable.bg_white);
		final LinearLayout resultsLayout = new LinearLayout(page);
		resultsLayout.setOrientation(LinearLayout.VERTICAL);

		final TextView resultsNo = new TextView(page);
		resultsNo.setTextSize(16);
		resultsNo.setTextColor(R.color.black);
		resultsNo.setPadding(10, 20, 0, 20);
		resultsNo.setBackgroundResource(R.drawable.bg_white);
		
		populateResults(page,resultsLayout, resultsNo);
		
		pageLayout.addView(resultsLayout);
		scr.addView(pageLayout);
		
		outputs.add(resultsNo);
		outputs.add(scr);
		
		Button nextButton = new Button(page);
		nextButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT, 1f));
		nextButton.setText("View more results");
		nextButton.setEnabled(false);
		
		LinearLayout bottomButtonsLayout = new LinearLayout(page);
		bottomButtonsLayout.addView(nextButton);
		
		pageLayout.addView(bottomButtonsLayout);
		
		if (jsonPage.getBoolean("has_next"))
		{
			//generate the next page dynamically
			nextButton.setEnabled(true);
			nextButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					try {
						populateResults(page,resultsLayout, resultsNo);
					} catch (JSONException e) {
						e.printStackTrace();
						Page.popupErrorDialog("JSON Exception", 
								"There might be a problem with JSON output " +
								"from server. Please try again later.", page, true);
					}
				}
			});
		}
		return outputs;
	}
	
	private void populateResults(Page page, LinearLayout resultsLayout, TextView resultsNo) throws JSONException
	{
		JSONObject nextJSONPage = new JSONObject();
		System.out.println(cache.toString());
		if (!cache.containsKey(query) || 
				(cache.containsKey(query) & cache.get(query).size()<=curPageNum))
		{
			JSONObject nextResults = router.exceptionHandledOnRequestSent(
					MollyModule.getName(page.getClass()),
					page, Router.OutputFormat.JSON, query+"?page="+curPageNum);
			nextJSONPage = nextResults.getJSONObject("page");
			myApp.updateLibCache(query, nextJSONPage);
		}
		else 
		{
			cachedJSONPages = (List<JSONObject>) cache.get(query);
			nextJSONPage = cachedJSONPages.get(curPageNum);
		}
		curPageNum++;
		JSONArray newObjects = nextJSONPage.getJSONArray("objects");
		TextView pageNumView = new TextView(page);
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
			thisResultLayout.setLayoutParams(paramsWithLine);	
			resultsLayout.addView(thisResultLayout);
		}
		
		resultsNo.setText("Search returned " + nextJSONPage.getString("num_objects") + " items." + '\n'
		+ "Displaying "+curPageNum+" page(s) of "+nextJSONPage.getString("num_pages")+" pages.");
	}
	
	private void addTextField(Page page, JSONObject jsonSource, String jsonKey, 
			String additionalText, ViewGroup parentViewGroup, float size) throws JSONException 
	{
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
	
	@Override
	public Page getInstance() {
		return this;
	}

	private class LibraryFirstResultTask extends  BackgroundTask<LinearLayout, Void, List<View>>
	{
		public LibraryFirstResultTask(Page page, boolean b)
		{
			super(page,b);
		}
		@Override
		protected List<View> doInBackground(LinearLayout... arg0) {
			try {
				return connectAndGenerate(page,query+"&page="+curPageNum);
			} catch (JSONException e) {
				jsonException = true;
			}
			return null;
		}
		
		@Override
		public void updateView(List<View> outputs) {
			populateViews(outputs,contentLayout);
		}
	}
	
}
