package org.mollyproject.android.view.apps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import roboguice.inject.InjectView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class ContentPage extends Page {
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
		
		//Construct breadcrumbs here, will move to background thread later
		//the first breadcrumb is always the home page
		homeBreadcrumb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), myApp.test("home").getClass());
				startActivityForResult(myIntent,0);
			}
		});
		
		try {
			//read the breadcrumb JSONArray
			//Structure of the array:
			//"breadcrumbs": ["library", ["Library search", "/library/"], ["Library search", "/library/"], true, "Search Results"]
			jsonContent = new JSONObject(router.exceptionHandledOnRequestSent
					(myApp.getUnimplementedLocator(),
					getInstance(), Router.OutputFormat.JSON, null));
			JSONArray breadcrumbsArray = jsonContent.getJSONArray("breadcrumbs");
			
			final String rootLocator = breadcrumbsArray.getString(0);
			//temporary, use :index for now:
			rootBreadcrumb.setBackgroundResource(SelectionManager.getBCImg(rootLocator+":index"));
			rootBreadcrumb.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent myIntent = new Intent(v.getContext(), 
							myApp.test(rootLocator).getClass());
					startActivityForResult(myIntent,0);
				}
			});
			Button extraBreadcrumb = (Button) findViewById(R.id.extraBreadcrumb);
			if(!breadcrumbsArray.isNull(1))
			{
				//1 dynamic breadcrumb is visible
				String urlPiece = breadcrumbsArray.getJSONArray(1).getString(1);
				String jsonResponse = Router.getFrom(Router.mOX+urlPiece+"/?format=json");
				final String name = new JSONObject(jsonResponse).getString("view_name").replace(":index", "");
				
				if (!name.equals(rootLocator))
				{
					//that dynamic breadcrumb is different from the root locator 
					extraBreadcrumb.setText("");
					extraBreadcrumb.setBackgroundResource(SelectionManager.getBCImg(name+":index"));
					extraBreadcrumb.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent myIntent = new Intent(v.getContext(), 
									myApp.test(name).getClass());
							startActivityForResult(myIntent,0);
						}
					});
					
					if(!breadcrumbsArray.isNull(2))
					{
						//2 dynamic breadcrumbs are available
						String urlPiece2 = breadcrumbsArray.getJSONArray(2).getString(1);
						String jsonResponse2 = Router.getFrom(Router.mOX+urlPiece2+"/?format=json");
						final String name2 = new JSONObject(jsonResponse2).getString("view_name").replace(":index","");
						
						if (!name2.equals(name))
						{
							//currently at page specified by the last breadcrumb, different
							//from the previous 2, all breadcrumbs are visible
							extraBreadcrumb.setText("...");
							extraTextView = (TextView) findViewById(R.id.extraTextView);
							extraTextView.setText(breadcrumbsArray.getJSONArray(2).getString(0));
						}
					}
					else
					{
						//second extra breadcrumb is null, and name is not rootlocator
						//so currently at extrabreadcrumb's page,
						//only home page and rootlocator page breadcrumbs are visible
						//with the current (extrabreadcrumb) page's name
						extraBreadcrumb.setEnabled(false);
						extraBreadcrumb.setText(breadcrumbsArray.getString(4));
					}
				}
				else
				{
					//name actually is rootlocator, so currently at rootlocator page
					rootBreadcrumb.setEnabled(false);
					extraBreadcrumb.setEnabled(false);
					extraBreadcrumb.setText(breadcrumbsArray.getString(4));
				}
			}
			/*else
			{
				extraBreadcrumb.setText(breadcrumbsArray.getString(4));
			}*/
			
		} catch (JSONException e) {
			e.printStackTrace();
			popupErrorDialog("JSON Exception", 
					"There might be a problem with JSON output " +
					"from server. Please try again later.", ContentPage.this.getInstance());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	@Override
	public void onResume()
	{
		super.onResume();
		System.out.println(SelectionManager.getName(getInstance().getClass())+" onResume");
		//myApp.updateBreadCrumb(SelectionManager.getName(getInstance().getClass()));
		System.out.println("Reconstructing");
		//bcBar.reconstruct();
	}
}
