package org.mollyproject.android.view.apps;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.Router;

import roboguice.inject.InjectView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class ContentPage extends Page {
	@InjectView(R.id.appBreadcrumb) protected Button appBreadcrumb;
	@InjectView (R.id.parentBreadcrumb) protected Button parentBreadcrumb;
	@InjectView (R.id.homeBreadcrumb) protected Button homeBreadcrumb;
	@InjectView (R.id.breadcrumbs) protected LinearLayout breadcrumbs;
	@InjectView (R.id.extraTextView) protected TextView extraTextView;
	@InjectView (R.id.contentLayout) protected LinearLayout contentLayout;
	protected boolean loaded = false;
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
				Intent myIntent = new Intent(v.getContext(), myApp.getPageClass("home:index"));
				startActivityForResult(myIntent,0);
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (!loaded)
		{
			new PageSetupTask(this).execute();
		}
	}
	
	protected class PageSetupTask extends BackgroundTask<Void, Void, JSONObject>
	{
		//new breadcrumb parser
		public PageSetupTask(Page page) {
			super(page, true, true);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				jsonContent = router.onRequestSent(getName(), 
						getAdditionalParams(), Router.OutputFormat.JSON, getQuery());
			} catch (UnknownHostException e) {
				e.printStackTrace();
				unknownHostException = true;
			} catch (JSONException e) {
				e.printStackTrace();
				jsonException = true;
			} catch (IOException e) {
				e.printStackTrace();
				ioException = true;
			}
		}
		
		@Override
		protected JSONObject doInBackground(Void... arg0) {
			//Download the breadcrumbs
			try {
				JSONObject breadcrumbs = jsonContent.getJSONObject("breadcrumbs");
				return breadcrumbs;
			} catch (JSONException e) {
				e.printStackTrace();
				jsonException = true;
			} catch (NullPointerException e) {
				e.printStackTrace();
				nullPointerException = true;
			}
		
			return null;
		}
		@Override
		public void updateView(JSONObject breadcrumbs) {
			// TODO Auto-generated method stub
			try {
				//app/index breadcrumb
				final String app = breadcrumbs.getString("application");
				JSONObject index = breadcrumbs.getJSONObject("index");
				appBreadcrumb.setBackgroundResource
						(myApp.getImgResourceId(index.getString("view_name")+"_bc"));
				appBreadcrumb.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent myIntent = new Intent(v.getContext(), myApp.getPageClass(app+":index"));
						startActivityForResult(myIntent, 0);
					}
				});
				//Page title
				String title = breadcrumbs.getString("page_title"); 
				
				//check for parent breadcrumb
				System.out.println(breadcrumbs.toString(1));
				if (!breadcrumbs.isNull("parent"))
				{
					JSONObject parent = breadcrumbs.getJSONObject("parent");
					//change page title if necessary
					
					if (!breadcrumbs.getBoolean("parent_is_index"))
					{
						//parent is not index, all breadcrumbs visible
						appBreadcrumb.setEnabled(true);
						
						final String parentName = parent.getString("view_name");
						parentBreadcrumb.setBackgroundResource(R.drawable.bg_blue);
						parentBreadcrumb.setText("....");
						parentBreadcrumb.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent myIntent = new Intent(v.getContext(), 
										myApp.getPageClass(parentName));
								startActivityForResult(myIntent, 0);
							}
						});
						extraTextView.setText(title);
					}
					else {
						title = parent.getString("title");
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
			} catch (JSONException e) {
				e.printStackTrace();
				Page.popupErrorDialog("JSON Exception", 
						"There might be a problem with JSON output " +
						"from server. Please try again.", page, true);
			} finally {
				loaded = true;
				contentLayout.invalidate();
			}
			
		}
	}
	
	public abstract String getQuery() throws UnsupportedEncodingException;
	
	public abstract String getAdditionalParams();
	
	public LinearLayout getContentLayout()
	{
		return contentLayout;
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
