package org.mollyproject.android.view.apps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackGroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import roboguice.inject.InjectView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class ContentPage extends Page {
	//cannot use guice injections because these following views need to be used by the subclasses as well
	protected Button rootBreadcrumb;
	protected Button homeBreadcrumb;
	protected LinearLayout breadcrumbs;
	protected TextView extraTextView;
	//aka ImplementedPage
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.content_page_template);
		contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
		rootBreadcrumb = (Button) findViewById(R.id.rootBreadcrumb);
		homeBreadcrumb = (Button) findViewById(R.id.homeBreadcrumb);
		breadcrumbs = (LinearLayout) findViewById(R.id.breadcrumbs);
		
		//pDialog = ProgressDialog.show(this, "", "Loading Page...", true, false);

		//Construct breadcrumbs here, will move to background thread later
		//the first breadcrumb is always the home page
		homeBreadcrumb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), myApp.test("home").getClass());
				startActivityForResult(myIntent,0);
			}
		});
		
		//new PageSetupTask(this,pDialog).execute();
		
	}
	
	protected class PageSetupTask extends BackGroundTask<Void, Void, JSONArray>
	{
		public PageSetupTask(Page page, ProgressDialog pDialog) {
			super(page);
		}
		
		@Override
		protected JSONArray doInBackground(Void... arg0) {
			return null;
			//Async Task goes here
		}

		@Override
		public void updateView(JSONArray outputs) {
			// TODO Auto-generated method stub
			
		}
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
