package org.mollyproject.android.view.apps.library;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

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
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		curPageNum = 1;
		query = myApp.getLibraryQuery()+"&page="+curPageNum;
		try {
			generatePage(1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void generatePage(int pageNumber) throws JSONException
	{
		String queryWithPage = query+"&page="+pageNumber;
		String jsonOutput = router.onRequestSent(SelectionManager.getName(this.getClass()), Router.JSON, queryWithPage);
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
						+ "Displaying page "+page.getString("number")+" of "+page.getString("num_pages")+" pages.";
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
		if (page.getBoolean("has_next"))
		{
			Button viewMore = new Button(this);
			viewMore.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT, 1f));
			viewMore.setText("Next Page >>");
			viewMore.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					pageLayout.removeAllViews();
					try {
						contentLayout.removeView(resultsNo);
						curPageNum++;
						generatePage(curPageNum);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			pageLayout.addView(viewMore);
			System.out.println("Reached");
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
