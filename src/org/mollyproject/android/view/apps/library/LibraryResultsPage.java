package org.mollyproject.android.view.apps.library;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
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
	protected Map<Integer,String> cache;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ProgressDialog spinner = ProgressDialog.show(this,
               "", "Loading...", true);
		spinner.show();
		curPageNum = 1;
		cache = new HashMap<Integer,String>();
		query = myApp.getLibraryQuery();
		try {
			connectAndGenerate(query+"&page="+curPageNum);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		spinner.dismiss();
	}
	
	private void connectAndGenerate(String queryWithPage) throws JSONException
	{
		String jsonOutput = router.onRequestSent(SelectionManager.getName(this.getClass()),
				Router.JSON, queryWithPage);
		generatePage(jsonOutput);
	}
	
	private void generatePage(String jsonOutput) throws JSONException
	{
		if (!cache.containsKey(curPageNum))
		{
			cache.put(curPageNum, jsonOutput);
		}
		JSONObject results = new JSONObject(jsonOutput);
		final JSONObject page = results.getJSONObject("page");
		
		final ScrollView scr = new ScrollView(this);
		
		final LinearLayout pageLayout = new LinearLayout(this);
		pageLayout.setOrientation(LinearLayout.VERTICAL);
		pageLayout.setBackgroundResource(R.drawable.bg_white);
		LinearLayout resultsLayout = new LinearLayout(this);
		resultsLayout.setOrientation(LinearLayout.VERTICAL);

		final TextView resultsNo = new TextView(this);
		resultsNo.setTextSize(16);
		resultsNo.setTextColor(R.color.black);
		resultsNo.setPadding(10, 20, 0, 20);
		resultsNo.setBackgroundResource(R.drawable.bg_white);
		String notification = "Search returned " + page.getString("num_objects") + " items." + '\n'
						+ "Displaying page "+curPageNum+" of "+page.getString("num_pages")+" pages.";
		resultsNo.setText(notification);
		
		JSONArray objects = page.getJSONArray("objects");
		for (int i = 0; i < objects.length(); i++)
		{
			LinearLayout thisResultLayout = new LinearLayout(this);
			thisResultLayout.setOrientation(LinearLayout.VERTICAL);
			JSONObject thisResult = objects.getJSONObject(i);
			//next step: display the title, author, publishers, no. of available libraries
			
			addTextField(thisResult,"title","",thisResultLayout,18);
			
			addTextField(thisResult,"author","Author: ",thisResultLayout,16);
			addTextField(thisResult,"publisher","Publisher: ",thisResultLayout,16);
			addTextField(thisResult,"holding_libraries","Libraries: ",thisResultLayout,16);
			thisResultLayout.setBackgroundResource(R.drawable.bg_blue);
			thisResultLayout.setPadding(10, 10, 0, 10);
			thisResultLayout.setLayoutParams(paramsWithLine);	
			resultsLayout.addView(thisResultLayout);
		}
		
		pageLayout.addView(resultsLayout);
		scr.addView(pageLayout);
		
		contentLayout.addView(resultsNo);
		contentLayout.addView(scr);
		
		Button nextButton = new Button(this);
		nextButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT, 1f));
		nextButton.setText("Next Page >>");
		nextButton.setEnabled(false);
		
		Button prevButton = new Button(this);
		prevButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT, 1f));
		prevButton.setText("<< Prev Page");
		prevButton.setEnabled(false);
		
		LinearLayout bottomButtonsLayout = new LinearLayout(this);
		bottomButtonsLayout.addView(prevButton);
		bottomButtonsLayout.addView(nextButton);
		
		pageLayout.addView(bottomButtonsLayout);
		
		if (curPageNum > 1)
		{
			//read the previous page from cache
			prevButton.setEnabled(true);
			prevButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					pageLayout.removeAllViews();
					try {
						//contentLayout.removeView(resultsNo);
						curPageNum--;
						contentLayout.removeView(resultsNo);
						contentLayout.removeView(scr);
						generatePage(cache.get(curPageNum));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
		}
		
		if (page.getBoolean("has_next"))
		{
			//generate the next page dynamically
			nextButton.setEnabled(true);
			nextButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					pageLayout.removeAllViews();
					try {
						contentLayout.removeView(resultsNo);
						contentLayout.removeView(scr);
						curPageNum++;
						if (cache.containsKey(curPageNum))
						{
							generatePage(cache.get(curPageNum));
						}
						else 
						{
							String newJSONOutput = router.onRequestSent
								(SelectionManager.getName(LibraryResultsPage.this.getClass()), 
										Router.JSON, query+"&page="+curPageNum);
							generatePage(newJSONOutput);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
		}
	}
	
	private void addTextField(JSONObject jsonSource, String jsonKey, 
			String additionalText, ViewGroup parentViewGroup, float size) throws JSONException 
	{
		if (!jsonSource.isNull(jsonKey))
		{
			LinearLayout textLayout = new LinearLayout(this);
			
			String text = jsonSource.getString(jsonKey);
			TextView textView = new TextView(this);
			textView.setTextSize(size);
			textView.setText(text);
			
			TextView moreTextView = new TextView(this);
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

}
