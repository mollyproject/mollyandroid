package org.mollyproject.android.view.apps.weblearn.signup;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WebLearnSignupPageTask extends JSONProcessingTask {

	public WebLearnSignupPageTask(ContentPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			LinearLayout signupsLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.general_search_results_page, null);
			
			((TextView) signupsLayout.findViewById(R.id.searchResultsHeader)).setText("Sign-up events by WebLearn site");
			
			JSONArray sites = jsonContent.getJSONArray("sites");
			LinearLayout sitesList = (LinearLayout) signupsLayout.findViewById(R.id.generalResultsList);
			sitesList.setBackgroundResource(R.drawable.shape_white);
			for (int i = 0; i < sites.length(); i++)
			{
				final JSONArray site = sites.getJSONArray(i);
				LinearLayout signupLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.clickable_search_result, null);
				signupLayout.setLayoutParams(Page.paramsWithLine);
				((TextView) signupLayout.findViewById(R.id.clickableResultText)).setText(site.getString(1));
				signupLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							MyApplication.weblearnSignupSlug = site.getString(0);
							Intent myIntent = new Intent(page.getApplicationContext(), MyApplication.getPageClass(MollyModule.WEBLEARN_SIGNUP_SITE));
							page.startActivityForResult(myIntent, 0);
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(page.getApplicationContext(), "This page cannot be displayed, please try again later", Toast.LENGTH_SHORT).show();
						}
						
					}
				});
				sitesList.addView(signupLayout);
			}
			
			page.getContentLayout().removeAllViews();
			page.getContentLayout().addView(signupsLayout);
			
			((ContentPage) page).doneProcessingJSON();
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
	}
	
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		if(Page.manualRefresh)
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
