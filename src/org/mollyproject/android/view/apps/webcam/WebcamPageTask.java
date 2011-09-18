package org.mollyproject.android.view.apps.webcam;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.UnimplementedPage;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WebcamPageTask extends JSONProcessingTask{

	public WebcamPageTask(ContentPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void updateView(JSONObject outputs) {
		// TODO Auto-generated method stub
		try
			{
			LinearLayout contentLayout = ((ContentPage) page).getContentLayout();
			contentLayout.removeAllViews();
			
			LayoutInflater layoutInflater = page.getLayoutInflater();
			
			LinearLayout feedsLayout = (LinearLayout) layoutInflater.inflate(R.layout.general_search_results_page, null);
			
			contentLayout.addView(feedsLayout);
			
			TextView header = (TextView) feedsLayout.findViewById(R.id.searchResultsHeader);
			header.setText("Webcams");
			
			LinearLayout resultsLayout = (LinearLayout) feedsLayout.findViewById(R.id.generalResultsList);
			resultsLayout.setOrientation(LinearLayout.VERTICAL);
			
			JSONArray webcams = outputs.getJSONArray("webcams");
			for (int i = 0; i < webcams.length(); i++)
			{
				final JSONObject webcam = webcams.getJSONObject(i);
				
				LinearLayout thisResult = (LinearLayout) layoutInflater.inflate
						(R.layout.clickable_search_result, null);
				resultsLayout.addView(thisResult);
				thisResult.setLayoutParams(Page.paramsWithLine);
				((TextView) thisResult.findViewById(R.id.clickableResultText)).setText(webcam.getString("title"));
				final String slug = webcam.getString("slug");
				thisResult.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						MyApplication.webcamSlug = slug;
						Intent myIntent = new Intent(page, MyApplication.getPageClass(MollyModule.WEBCAM));
						page.startActivityForResult(myIntent, 0);
					}
				});
			}
			((ContentPage) page).doneProcessingJSON();
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
	}
	
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		if (Page.manualRefresh)
		{
			return super.doInBackground();
		}
		
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ((ContentPage) page).getJSONContent();
	}
}
