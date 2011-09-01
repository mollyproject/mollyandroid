package org.mollyproject.android.view.apps.search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class NewSearchTask extends BackgroundTask<Void, Void, String>{

	public NewSearchTask(NewSearchPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(String outputs) {
		try {
			//Set up the tabs
			JSONArray availableApps = MyApplication.availableApps;
			Intent myIntent;
			TabHost.TabSpec spec;
			Resources res = page.getResources();
			String tabTag = new String();
			NewSearchPage.searchTabHost.setup();
			
			for (int i = 0; i < availableApps.length(); i++)
			{
				JSONObject app = availableApps.getJSONObject(i);
				if (app.getBoolean("display_to_user"))
				{
					//tabTag = app.getString("title");
					//myIntent = new Intent().setClass(page.getApplicationContext(), SearchPage.class);
				    spec = NewSearchPage.searchTabHost.newTabSpec(app.getString("local_name") + ":index")
				    	.setIndicator(null, res.getDrawable(MyApplication.getImgResourceId
				    			(app.getString("local_name") + ":index_img"))).setContent(R.id.searchTabLayout);
				    NewSearchPage.searchTabHost.addTab(spec);
				}
			}
			NewSearchPage.searchTabHost.setOnTabChangedListener(new OnTabChangeListener() {

				@Override
				public void onTabChanged(String tabId) {
					String tag = NewSearchPage.searchTabHost.getCurrentTabTag();
					populatePage(tag, (LinearLayout) NewSearchPage.searchTabHost.findViewById(R.id.searchTabLayout));
				}
				
			});
			
			//A small bug: onTabChanged not triggered
			NewSearchPage.searchTabHost.setCurrentTab(1);
			NewSearchPage.searchTabHost.setCurrentTab(0);
			//NewSearchPage.searchTabHost.getcont;
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
	}
	
	public void populatePage(String app, LinearLayout searchTabLayout)
	{
		if (app.equals(MollyModule.CONTACT_PAGE))
		{
			setTexts("Search for a contact", "Type the name of the person you want to " +
					"search for into the search bar", searchTabLayout);
		}
		else if (app.equals(MollyModule.LIBRARY_PAGE))
		{
			setTexts("Search for a book", "Type the name or ISBN code of the book you want to " +
					"search for into the search bar", searchTabLayout);
		}
		else if (app.equals(MollyModule.PLACES_PAGE))
		{
			setTexts("Search for a place", "Type the street, name or post code of the place you want to " +
					"search for into the search bar", searchTabLayout);
		}
	}
	
	public void setTexts(CharSequence titleText, CharSequence helpText, LinearLayout searchTabLayout)
	{
		try
		{
			TextView title = (TextView) searchTabLayout.findViewById(R.id.searchTabTitle);
			title.setText(titleText);
			
			TextView help = (TextView) searchTabLayout.findViewById(R.id.searchTabHelp);
			help.setText(helpText);
		} catch (NullPointerException e)
		{
			//Dont need to do anything here
			e.printStackTrace();
		}
	}
	
	@Override
	protected String doInBackground(Void... params) {
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return "Do not return null here";
	}

}
