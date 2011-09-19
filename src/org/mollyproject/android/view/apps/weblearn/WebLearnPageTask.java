package org.mollyproject.android.view.apps.weblearn;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

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
			if (!userDetails.isNull("displayId")) //& MyApplication.weblearnState == WebLearnPage.STATE_AUTHORISED)
			{
				//Populate the authorised state
				MyApplication.weblearnState = WebLearnPage.STATE_AUTHORISED;
			}
			else
			{
				//Populate the default state
				MyApplication.weblearnState = WebLearnPage.STATE_DEFAULT;
				
				LinearLayout weblearnLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.weblearn, null);
				
				Button loginButton = (Button) weblearnLayout.findViewById(R.id.loginButton);
				loginButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//Intent
					}
				});
				
				page.getContentLayout().removeAllViews();
				page.getContentLayout().addView(weblearnLayout);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
				Intent myIntent = new Intent(page.getApplicationContext(), My)
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
