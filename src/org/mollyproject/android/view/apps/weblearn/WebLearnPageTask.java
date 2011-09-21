package org.mollyproject.android.view.apps.weblearn;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WebLearnPageTask extends JSONProcessingTask {

	public WebLearnPageTask(ContentPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			JSONObject userDetails = jsonContent.getJSONObject("user_details");
			LinearLayout weblearnLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.weblearn, null);
			Button loginButton = (Button) weblearnLayout.findViewById(R.id.loginButton);
			
			if (!userDetails.isNull("displayId"))
			{
				//Populate the authorised state
				MyApplication.weblearnState = WebLearnPage.STATE_AUTHORISED;
				((ViewGroup) loginButton.getParent()).removeView(loginButton);
				TextView welcomeText = (TextView) weblearnLayout.findViewById(R.id.weblearnWelcomeText);
				welcomeText.setText("Hello " + userDetails.getString("firstName") + ", welcome to mobile WebLearn preview!");
			}
			else
			{
				//Populate the default state
				MyApplication.weblearnState = WebLearnPage.STATE_DEFAULT;
				
				MyApplication.oauthToken = new String();
				MyApplication.oauthVerifier = null;
				
				loginButton.setMinWidth(page.getWindowManager().getDefaultDisplay().getWidth()/3);
				loginButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						new LoginInfoTask((ContentPage) page, false, true).execute();
					}
				});
			}
			
			JSONArray announcementsCollection = jsonContent.getJSONObject("announcements").getJSONArray("announcements_collection");
			LinearLayout announcementsLayout = (LinearLayout) weblearnLayout.findViewById(R.id.announcementsLayout);
			if (announcementsCollection.length() == 0)
			{
				LinearLayout noAnnouncementLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.plain_text_search_result, null);
				((TextView) noAnnouncementLayout.findViewById(R.id.plainTextResultText)).setText("No announcements right now.");
				announcementsLayout.addView(noAnnouncementLayout);
			}
			else
			{
				//there are some announcements
				for (int i = 0; i < announcementsCollection.length(); i++)
				{
					final JSONObject announcement = announcementsCollection.getJSONObject(i);
					LinearLayout announcementLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.clickable_search_result, null);
					((TextView) announcementLayout.findViewById(R.id.clickableResultText)).setText(announcement.getString("title"));
					
					announcementLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								MyApplication.weblearnAnnouncementSlug = announcement.getString("id");
								Intent myIntent = new Intent(page.getApplicationContext(), MyApplication.getPageClass(MollyModule.WEBLEARN_ANNOUNCEMENT));
								page.startActivityForResult(myIntent, 0);
							} catch (Exception e) {
								e.printStackTrace();
								Toast.makeText(page.getApplicationContext(), "This announcement is not available. Please try again later.", Toast.LENGTH_SHORT).show();
							}
						}
					});
					
					announcementLayout.setLayoutParams(Page.paramsWithLine);
					announcementsLayout.addView(announcementLayout);
				}
			}
			
			weblearnLayout.findViewById(R.id.signupButton).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent myIntent = new Intent(page.getApplicationContext(), MyApplication.getPageClass(MollyModule.WEBLEARN_SIGNUP_INDEX));
					page.startActivityForResult(myIntent, 0);
				}
			});
			
			page.getContentLayout().removeAllViews();
			page.getContentLayout().addView(weblearnLayout);
			
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
	
	private class LoginInfoTask extends JSONProcessingTask
	{

		public LoginInfoTask(ContentPage page,
				boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
			super(page, toDestroyPageAfterFailure, dialogEnabled);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void updateView(JSONObject output) {
			try {
				MyApplication.locator = output.getString("authorize_url");
				Intent myIntent = new Intent(page.getApplicationContext(), MyApplication.getPageClass(MollyModule.WEBLEARN_LOGIN));
				page.startActivityForResult(myIntent, 0);
			} catch (Exception e) {
				e.printStackTrace();
				operationException = true;
			}
		}
		
		@Override
		protected JSONObject doInBackground(JSONObject... params) {
			//this task has its own download procedure because the query is a bit different than normal and it uses MollyModule.WEBLEARN anyway
			try {
				return MyApplication.router.onRequestSent(page.getName(), null, Router.OutputFormat.JSON, "&force_login");
			} catch (Exception e) {
				e.printStackTrace();
				operationException = true;
			}
			return null;
		}
	}
	
}
