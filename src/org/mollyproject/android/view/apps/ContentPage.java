package org.mollyproject.android.view.apps;

import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.home.HomePage;

import roboguice.inject.InjectView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public abstract class ContentPage extends Page {
	@InjectView(R.id.appBreadcrumb) protected Button appBreadcrumb;
	@InjectView (R.id.parentBreadcrumb) protected Button parentBreadcrumb;
	@InjectView (R.id.homeBreadcrumb) protected Button homeBreadcrumb;
	@InjectView (R.id.breadcrumbs) protected LinearLayout breadcrumbs;
	@InjectView (R.id.extraTextView) protected TextView extraTextView;
	@InjectView (R.id.contentLayout) protected LinearLayout contentLayout;
	@InjectView (R.id.contentScroll) protected ScrollView contentScroll;
	protected boolean dialogOnSetup = false; //set this to true if a progress dialog is needed when calling PageSetupTask
	protected boolean loaded = false;
	protected boolean jsonDownloaded = false;
	protected boolean jsonProcessed = false;
	protected JSONObject jsonContent;
	//aka ImplementedPage
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_page_template);
		//Construct breadcrumbs here, will move to background thread later
		//the first breadcrumb is always the home page
		homeBreadcrumb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), MyApplication.getPageClass("home:index"));
				HomePage.firstHomeLoad = true;
				startActivityForResult(myIntent,0);
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (!loaded || MyApplication.destroyed)// || manualRefresh)
		{
			//The page is either not loaded or all its data has been erased 
			//after sleeping for a while and its cache is claimed
			loaded = true;
			jsonDownloaded = false;
			jsonProcessed = false;
			if (MyApplication.destroyed)
			{
				MyApplication.destroyed = false;
			}
			new PageSetupTask(this).execute();
		}
		if (!jsonProcessed)// || manualRefresh)
		{
			//if manual refresh is true it should always be set back to false upon termination of a task
			refresh();
		}
	}
	
	public void setJSONContent(JSONObject jsonContent)
	{
		this.jsonContent = jsonContent;
	}
	
	public JSONObject getJSONContent()
	{
		return jsonContent;
	}
	
	public synchronized boolean downloadedJSON()
	{
		return jsonDownloaded;
	}
	
	public synchronized boolean jsonProcessed()
	{
		return jsonProcessed;
	}
	
	public void doneProcessingJSON()
	{
		 jsonProcessed = true;
	}
	
	protected class PageSetupTask extends BackgroundTask<Void, Void, JSONObject>
	{
		//new breadcrumb parser
		public PageSetupTask(Page page) {
			super(page, true, dialogOnSetup);
		}
		
		@Override
		protected JSONObject doInBackground(Void... arg0) {
			//Download the breadcrumbs
			try {
				jsonContent = MyApplication.router.requestJSON(getName(), 
						getAdditionalParams(), getQuery());
				jsonDownloaded = true;
				return jsonContent;
			} catch (Exception e) {
				e.printStackTrace();
				otherException = true;
			}
		
			return null;
		}
		@Override
		public void updateView(JSONObject jsonContent) {
			try {
				JSONObject breadcrumbs = jsonContent.getJSONObject("breadcrumbs");
				//app/index breadcrumb
				final String app = breadcrumbs.getString("application");
				JSONObject index = breadcrumbs.getJSONObject("index");
				appBreadcrumb.setBackgroundResource
						(MyApplication.getImgResourceId(index.getString("page_name")+"_bc"));
				appBreadcrumb.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent myIntent = new Intent(v.getContext(), MyApplication.getPageClass(app+":index"));
						startActivityForResult(myIntent, 0);
					}
				});
				//Page title
				String title = breadcrumbs.getString("page_title"); 
				
				//check for parent breadcrumb
				if (!breadcrumbs.isNull("parent"))
				{
					JSONObject parent = breadcrumbs.getJSONObject("parent");
					
					if (!breadcrumbs.getBoolean("parent_is_index"))
					{
						//parent is not index, all breadcrumbs visible
						appBreadcrumb.setEnabled(true);
						
						final String parentName = parent.getString("page_name");
						parentBreadcrumb.setBackgroundResource(R.drawable.bg_blue);
						parentBreadcrumb.setText("....");
						parentBreadcrumb.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent myIntent = new Intent(v.getContext(), 
										MyApplication.getPageClass(parentName));
								startActivityForResult(myIntent, 0);
							}
						});
						extraTextView.setText(title);
					}
					else {
						extraTextView.setText("");
						parentBreadcrumb.setText(title); 
						parentBreadcrumb.setEnabled(false);
						appBreadcrumb.setEnabled(true);
					}
				}
				else { 
					parentBreadcrumb.setText(title); 
					parentBreadcrumb.setEnabled(false); 
					appBreadcrumb.setEnabled(false); 
				}
				contentLayout.invalidate();
				
				//set favourite
				if (jsonContent.has("is_favouritable"))
				{
					//if it is not favouritable then the values stay false 
					if (jsonContent.getBoolean("is_favouritable"))
					{
						page.setFavable(jsonContent.getBoolean("is_favouritable"));
						page.setFav(jsonContent.getBoolean("is_favourite"));
						System.out.println(isFavourite);
						MyApplication.favouriteURL = jsonContent.getString("favourite_url");
					}
				}
				
			} catch (Exception e) {
				//otherException = true;
				e.printStackTrace();
			} 
		}
	}
	
	public LinearLayout getBreadcrumbs()
	{
		return breadcrumbs;
	}
	
	public LinearLayout getContentLayout()
	{
		return contentLayout;
	}
	
	public void setContentLayout(LinearLayout contentLayout)
	{
		//this is called when another layout is needed to be populated rather than the original contentLayout
		this.contentLayout = contentLayout;
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
