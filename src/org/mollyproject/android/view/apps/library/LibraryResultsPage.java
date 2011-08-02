package org.mollyproject.android.view.apps.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.ResultsDisplayPage;
import org.mollyproject.android.view.apps.contact.ContactResultsPage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LibraryResultsPage extends ResultsDisplayPage {

	protected int curPageNum;
	protected ProgressDialog pDialog;
	
	protected Map<Integer,String> cache;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		curPageNum = 1;
		cache = new HashMap<Integer,String>();
		query = myApp.getLibraryQuery();
		
		pDialog = ProgressDialog.show(this, "", "Loading...", true, false);
		
		new LibraryResultsTask().execute();
		
		/*try {
			connectAndGenerate(query+"&page="+curPageNum);
		} catch (JSONException e) {
			e.printStackTrace();
			Page.popupErrorDialog("JSON Exception", 
					"There might be a problem with JSON output " +
					"from server. Please try again later.", this, true);
		}*/
	}
	
	private List<View> connectAndGenerate(String queryWithPage) throws JSONException
	{
		String jsonOutput = router.exceptionHandledOnRequestSent(SelectionManager.getName(this.getClass()),
				this, Router.OutputFormat.JSON, queryWithPage);
		return generatePage(jsonOutput);
	}
	
	private List<View> generatePage(String jsonOutput) throws JSONException
	{
		List<View> outputs = new ArrayList<View>();
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
		
		outputs.add(resultsNo);
		outputs.add(scr);
		
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
						e.printStackTrace();
						Page.popupErrorDialog("JSON Exception", 
								"There might be a problem with JSON output " +
								"from server. Please try again later.", LibraryResultsPage.this, true);
						
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
							String newJSONOutput = router.exceptionHandledOnRequestSent
								(SelectionManager.getName(LibraryResultsPage.this.getClass()), 
									LibraryResultsPage.this,Router.OutputFormat.JSON, query+"&page="+curPageNum);
							generatePage(newJSONOutput);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Page.popupErrorDialog("JSON Exception", 
								"There might be a problem with JSON output " +
								"from server. Please try again later.", LibraryResultsPage.this, true);
					}
					
				}
			});
		}
		return outputs;
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

	private class LibraryResultsTask extends  AsyncTask<LinearLayout, Void, List<View>>
	{
		protected boolean jsonException = false;
		@Override
		protected List<View> doInBackground(LinearLayout... arg0) {
			try {
				connectAndGenerate(query+"&page="+curPageNum);
			} catch (JSONException e) {
				jsonException = true;
			}
			return null;
		}
		
		protected void onPostExecute(List<View> outputs)
		{
			if (jsonException)
			{
				jsonException = false;
				popupErrorDialog("JSON Exception", 
						"There might be a problem with JSON output " +
						"from server. Please try again.", LibraryResultsPage.this, true);
			}
			pDialog.dismiss();
		}
	}
	
}
