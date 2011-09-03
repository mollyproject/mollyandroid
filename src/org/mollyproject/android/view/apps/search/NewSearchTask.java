package org.mollyproject.android.view.apps.search;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.contact.AbstractContactPage;
import org.mollyproject.android.view.apps.library.AbstractLibraryPage;

import android.content.Intent;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class NewSearchTask extends BackgroundTask<Void, Void, String>{
	protected LinearLayout searchTabLayout;
	public NewSearchTask(NewSearchPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(String outputs) {
		try {
			//Set up the tabs
			JSONArray availableApps = MyApplication.availableApps;
			TabHost.TabSpec spec;
			Resources res = page.getResources();
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
			searchTabLayout = (LinearLayout) NewSearchPage.searchTabHost
								.findViewById(R.id.searchTabLayout);
			page.setContentLayout(searchTabLayout); // now searchTabLayout is the content layout
			NewSearchPage.searchTabHost.setOnTabChangedListener(new OnTabChangeListener() {
				@Override
				public void onTabChanged(String tabId) {
					String tag = NewSearchPage.searchTabHost.getCurrentTabTag();
					System.out.println(NewSearchPage.searchTabHost.getCurrentTabTag());
					populatePage(tag, searchTabLayout);
				}
			});
			
			//A small bug: onTabChanged not triggered
			NewSearchPage.searchTabHost.setCurrentTab(1);
			if (!MyApplication.currentApp.equals(MollyModule.HOME_PAGE))
			{
				NewSearchPage.searchTabHost.setCurrentTabByTag(MyApplication.currentApp);
			}
			else
			{
				NewSearchPage.searchTabHost.setCurrentTab(0);
			}
			
			((ContentPage) page).doneProcessingJSON();
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
	}
	
	public void populatePage(String app, LinearLayout searchTabLayout)
	{
		resetSearchTabLayout();//remove any search bars from the layout
		
		//creates a simpler version of each specific search
		if (app.equals(MollyModule.CONTACT_PAGE))
		{
			//Contact search
			setTexts("Search for a contact", "Type the name of the person you want to " +
					"search for into the search bar", searchTabLayout);
			LinearLayout contactSearchBar = (LinearLayout) page.getLayoutInflater().inflate
					(R.layout.contact_search_bar, null);
			searchTabLayout.addView(contactSearchBar);
			AbstractContactPage.setUpContactSearchBar(page, contactSearchBar);
		}
		else if (app.equals(MollyModule.LIBRARY_PAGE))
		{
			//Library Search
			setTexts("Search for a book", "Type the name or ISBN code of the book you want to " +
					"search for into the search bar", searchTabLayout);
			LinearLayout librarySearchBar = (LinearLayout) page.getLayoutInflater().inflate
					(R.layout.library_search_bar, null);
			searchTabLayout.addView(librarySearchBar);
			AbstractLibraryPage.setUpLibrarySearchBar(page, new HashMap<String,String>(), librarySearchBar);
		}
		else if (app.equals(MollyModule.PLACES_PAGE))
		{
			setTexts("Search for a place", "Type the street, name or post code of the place you want to " +
					"search for into the search bar", searchTabLayout);
			RelativeLayout placesSearchBar = (RelativeLayout) page.getLayoutInflater().inflate
					(R.layout.search_bar, null);
			searchTabLayout.addView(placesSearchBar);
			
			EditText searchField = (EditText) placesSearchBar.findViewById(R.id.searchField);
			Button searchButton = (Button) placesSearchBar.findViewById(R.id.searchButton);
			
			Page.setEnterKeySearch(searchField, page, "places");
			Page.setClickSearch(searchButton, searchField, page, "places"); 
		}
		else if (app.equals(MollyModule.TRANSPORT_PAGE))
		{
			setTexts("Search for a bus or train", "Type the bus route or train you want to " +
					"search for into the search bar", searchTabLayout);
			RelativeLayout placesSearchBar = (RelativeLayout) page.getLayoutInflater().inflate
					(R.layout.search_bar, null);
			searchTabLayout.addView(placesSearchBar);
			
			EditText searchField = (EditText) placesSearchBar.findViewById(R.id.searchField);
			Button searchButton = (Button) placesSearchBar.findViewById(R.id.searchButton);
			
			Page.setEnterKeySearch(searchField, page, "transport");
			Page.setClickSearch(searchButton, searchField, page, "transport"); 
		}
		else if (app.equals(MollyModule.PODCAST_PAGE))
		{
			setTexts("Search for a podcast", "Type the name of the podcast you want to " +
					"search for into the search bar", searchTabLayout);
			RelativeLayout placesSearchBar = (RelativeLayout) page.getLayoutInflater().inflate
					(R.layout.search_bar, null);
			searchTabLayout.addView(placesSearchBar);
			
			EditText searchField = (EditText) placesSearchBar.findViewById(R.id.searchField);
			Button searchButton = (Button) placesSearchBar.findViewById(R.id.searchButton);
			
			Page.setEnterKeySearch(searchField, page, "podcasts");
			Page.setClickSearch(searchButton, searchField, page, "podcasts"); 
		}
		else
		{
			setTexts("Search unavailable", "Sorry this search function has not been available yet", searchTabLayout);
		}
	}
	
	private void resetSearchTabLayout()
	{
		//remove any children at index > 1 from searchTabLayout, should only be called after it is initiated
		for(int i = 2; i < searchTabLayout.getChildCount(); i++)
		{
			searchTabLayout.removeViewAt(i);
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
