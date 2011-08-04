package org.mollyproject.android.view.apps;

import java.io.IOException;
import java.net.UnknownHostException;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class ContentPage extends Page {
	//cannot use guice injections because these following views need to be used by the subclasses as well
	protected Button appBreadcrumb;
	protected Button parentBreadcrumb;
	protected Button homeBreadcrumb;
	protected LinearLayout breadcrumbs;
	protected TextView extraTextView;
	//aka ImplementedPage
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.content_page_template);
		contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
		appBreadcrumb = (Button) findViewById(R.id.appBreadcrumb);
		parentBreadcrumb = (Button) findViewById(R.id.parentBreadcrumb);
		homeBreadcrumb = (Button) findViewById(R.id.homeBreadcrumb);
		breadcrumbs = (LinearLayout) findViewById(R.id.breadcrumbs);
		
		pDialog = ProgressDialog.show(this, "", "Loading Page...", true, false);

		//Construct breadcrumbs here, will move to background thread later
		//the first breadcrumb is always the home page
		homeBreadcrumb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), myApp.test("home").getClass());
				startActivityForResult(myIntent,0);
			}
		});
		
		new PageSetupTask(this).execute();
		
	}
	
	protected class PageSetupTask extends BackgroundTask<Void, Void, JSONObject>
	{
		//new breadcrumb parser
		public PageSetupTask(Page page) {
			super(page);
		}
		@Override
		protected JSONObject doInBackground(Void... arg0) {
			//Download the breadcrumbs
			try {
				JSONObject jsonOutput = new JSONObject(router.onRequestSent(myApp.getUnimplementedLocator(), 
						Router.OutputFormat.JSON, null));
				JSONObject breadcrumbs = jsonOutput.getJSONObject("breadcrumbs");
				return breadcrumbs;
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
			return null;
		}

		@Override
		public void updateView(JSONObject breadcrumbs) {
			// TODO Auto-generated method stub
			try {
				//app/index breadcrumb
				final String app = breadcrumbs.getString("application");
				JSONObject index = breadcrumbs.getJSONObject("index");
				appBreadcrumb.setBackgroundResource(SelectionManager.getBCImg(index.getString("view_name")));
				appBreadcrumb.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent myIntent = new Intent(v.getContext(), myApp.test(app).getClass());
						startActivityForResult(myIntent, 0);
					}
				});
				//Page title
				String title = breadcrumbs.getString("page_title"); 
				
				//check for parent breadcrumb
				if (!breadcrumbs.isNull("parent"))
				{
					JSONObject parent = breadcrumbs.getJSONObject("parent");
					//change page title if necessary
					title = parent.getString("title");
					if (!index.getBoolean("parent_is_index"))
					{
						final String parentName = parent.getString("view_name");
						parentBreadcrumb.setBackgroundResource(SelectionManager.getBCImg(parentName));
						parentBreadcrumb.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent myIntent = new Intent(v.getContext(), myApp.test(parentName.replace(":index", "")).getClass());
								startActivityForResult(myIntent, 0);
							}
						});
						extraTextView.setText(title);
					}
					else { 
						parentBreadcrumb.setText(title); 
						parentBreadcrumb.setEnabled(false); 
						appBreadcrumb.setEnabled(false); }
				}
				else { parentBreadcrumb.setText(title); 
					parentBreadcrumb.setEnabled(false); 
					appBreadcrumb.setEnabled(false); 
					}
				
			} catch (JSONException e) {
				e.printStackTrace();
				Page.popupErrorDialog("JSON Exception", 
						"There might be a problem with JSON output " +
						"from server. Please try again.", page, true);
			} finally {
				pDialog .dismiss();
			}
			
		}
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
