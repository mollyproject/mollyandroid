package org.mollyproject.android.view.apps;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.LocationTracker;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.feedback.FeedbackPage;
import org.mollyproject.android.view.apps.library.AbstractLibraryPage;
import org.mollyproject.android.view.apps.search.NewSearchPage;
import roboguice.activity.RoboActivity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class Page extends RoboActivity {
	protected MyApplication myApp;
	protected SharedPreferences.Editor editor;
	protected SharedPreferences settings;
	public static boolean manualRefresh;
	protected boolean favouritable;
	protected boolean isFavourite;
	//The next 3 LinearLayouts are for use in the Location menu
	protected LinearLayout detachedSuggestionsLayout;
	protected LinearLayout detachedHistoryLayout;
	protected LinearLayout locationLayout;
	
	public static final int DIALOG_LOCATION = 0;
	public static final int DIALOG_LOCATION_HISTORY = 1;
	public static final int DIALOG_LOCATION_SUGGESTIONS = 2; 
	
	protected String name;
	protected String additionalArgs;
	protected String query;
	//protected String query
	
	//use someLayout.setLayoutParams() with this paramsWithLine as a parameter makes
	//a gap of 5px below the LinearLayout, this is used here to make gaps between views
	public static LinearLayout.LayoutParams paramsWithLine = new LinearLayout.LayoutParams
			(LinearLayout.LayoutParams.FILL_PARENT, 
			LinearLayout.LayoutParams.FILL_PARENT);
	static { paramsWithLine.setMargins(0, 0, 0, 2); }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//save state for next session
		super.onSaveInstanceState(outState);
		
		//save state: weblearn announcement id
		try {
			outState.putString("weblearnAnnouncementSlug", MyApplication.weblearnAnnouncementSlug);
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//save state: oauth token and verifier
		try {
			outState.putString("oauthToken", MyApplication.oauthToken);
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		try {
			if (MyApplication.oauthVerifier != null)
			{
				outState.putString("oauthVerifier", MyApplication.oauthVerifier);
			}
			else
			{
				outState.remove("oauthVerifier");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//save state: webcam slug
		try {
			outState.putString("webcamSlug", MyApplication.webcamSlug);
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//save state: last search app
		try {
			outState.putInt("lastSearchApp", MyApplication.lastSearchApp);
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//save state: locator for UnimplementedPage
		try {
			outState.putString("locator", MyApplication.locator);
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//save state: auto location settings
		try {
			outState.putBoolean("autoLoc", LocationTracker.autoLoc);
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//save state: last contactquery & medium
		try {
			outState.putString("contactQuery", MyApplication.contactQuery[0]);
			outState.putString("contactMedium", MyApplication.contactQuery[1]);
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//save state: library queries (book, title, author)
		try {
			for (String key : MyApplication.libraryQuery.keySet())
			{
				outState.putString(key, MyApplication.libraryQuery.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//save state: last known location
		try {
			outState.putString("currentLocation", MyApplication.currentLocation.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//save state: last placesArgs
		try {
			outState.putString("identifier_scheme", MyApplication.placesArgs[0]);
			outState.putString("identifier_value", MyApplication.placesArgs[1]);
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
	
		//save state: podcast slugs
		try {
			outState.putString("podcastsSlug", MyApplication.podcastsSlug);
			outState.putString("indPodcastSlug", MyApplication.indPodcastSlug);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//save state: general query
		try {
			outState.putString("generalQuery", MyApplication.generalQuery[0]);
			if (MyApplication.generalQuery.length > 1)
			{
				outState.putString("generalApplication", MyApplication.generalQuery[1]);
			}
			else
			{
				outState.remove("generalApplication");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//save state: locator (unimplemented page)
		try {
			outState.putString("unimplementedLocator", MyApplication.locator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		//restore instance state
		super.onRestoreInstanceState(savedInstanceState);
		
		//load state: weblearn announcement id
		try {
			MyApplication.weblearnAnnouncementSlug = savedInstanceState.getString("weblearnAnnouncementSlug");
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//load state: oauth token and verifier
		try {
			MyApplication.oauthToken = savedInstanceState.getString("oauthToken");
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		try {
			if (savedInstanceState.containsKey("oauthVerifier"))
			{
				MyApplication.oauthVerifier = savedInstanceState.getString("oauthVerifier");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		
		//load state: webcam slug
		try {
			MyApplication.webcamSlug = savedInstanceState.getString("webcamSlug");
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//load state: last search app
		try {
			MyApplication.lastSearchApp = savedInstanceState.getInt("lastSearchApp");
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//load state: locator for UnimplementedPage
		try {
			MyApplication.locator = savedInstanceState.getString("locator");
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//load state: auto location settings
		try {
			LocationTracker.autoLoc = savedInstanceState.getBoolean("autoLoc");
		} catch (Exception e) {
			//Do nothing
			e.printStackTrace();
		}
		
		//load state: last contactquery & medium
		try {
			MyApplication.contactQuery = new String[2];
			MyApplication.contactQuery[0] = savedInstanceState.getString("contactQuery");
			MyApplication.contactQuery[1] = savedInstanceState.getString("contactMedium");
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//load state: library query
		try {
			MyApplication.libraryQuery = new HashMap<String,String>();
			MyApplication.libraryQuery.put(AbstractLibraryPage.TITLE, savedInstanceState.getString(AbstractLibraryPage.TITLE));
			MyApplication.libraryQuery.put(AbstractLibraryPage.ISBN, savedInstanceState.getString(AbstractLibraryPage.ISBN));
			MyApplication.libraryQuery.put(AbstractLibraryPage.AUTHOR, savedInstanceState.getString(AbstractLibraryPage.AUTHOR));
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//load state: last known location
		try {
			MyApplication.currentLocation = new JSONObject(savedInstanceState.getString("currentLocation"));
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//load state: last placesArgs
		try {
			MyApplication.placesArgs = new String[2];
			MyApplication.placesArgs[0] = savedInstanceState.getString("identifier_scheme");
			MyApplication.placesArgs[1] = savedInstanceState.getString("identifier_value");
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//load state: podcast slugs
		try {
			MyApplication.podcastsSlug = savedInstanceState.getString("podcastsSlug");
			MyApplication.indPodcastSlug = savedInstanceState.getString("indPodcastSlug");
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//load state: general query
		try {
			if (!savedInstanceState.containsKey("generalApplication"))//MyApplication.generalQuery.length > 1)
			{
				MyApplication.generalQuery = new String[1];
				MyApplication.generalQuery[0] = savedInstanceState.getString("generalQuery");
			}
			else
			{
				MyApplication.generalQuery = new String[2];
				MyApplication.generalQuery[0] = savedInstanceState.getString("generalQuery");
				MyApplication.generalQuery[1] = savedInstanceState.getString("generalApplication");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
		
		//save state: locator (unimplemented page)
		try {
			MyApplication.locator = savedInstanceState.getString("unimplementedLocator");
		} catch (Exception e) {
			e.printStackTrace();
			//Do nothing
		}
	}
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		myApp = (MyApplication) getApplication();
		MyApplication.destroyed = false;
		settings = getSharedPreferences(MyApplication.PREFS_NAME, 0);
		editor = settings.edit();
		manualRefresh = false;
		name = null;
		additionalArgs = null;
	}
	
	public void setFavable(boolean b)
	{
		favouritable = b;
	}
	
	public void setFav(boolean b)
	{
		System.out.println(b);
		isFavourite = b;
	}
	
	public boolean isFavourite()
	{
		return isFavourite;
	}
	
	public abstract Page getInstance();
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem favouriteItem = menu.findItem(R.id.favourite);
	    favouriteItem.setVisible(favouritable);
	    
	    if (isFavourite)
	    {
	    	favouriteItem.setTitle("UnFav");
	    }
	    else
	    {
	    	favouriteItem.setTitle("Fav");
	    }
	    
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;
	    switch (item.getItemId()) {
	        case R.id.reload:
	        	manualRefresh = true;
	        	refresh();//onResume();
	            break;
	        case R.id.location:
	        	showDialog(DIALOG_LOCATION);
	        	break;
	        case R.id.favourite:
	        	new FavouriteOptionsTask(this, false, true).execute();
	        	break;
	        	
	        case R.id.feedback:
	        	myIntent = new Intent(getApplicationContext(), MyApplication.getPageClass(MollyModule.FEEDBACK_PAGE));
	        	startActivityForResult(myIntent, 0);
	        	break;
        	
	        case R.id.manage_favourites:
	        	myIntent = new Intent(getApplicationContext(), MyApplication.getPageClass(MollyModule.FAVOURITES));
	        	startActivityForResult(myIntent, 0);
	        	break;
	    }
	    return true;
	}
	
	public static void searchFromField(Page page, EditText searchField, String application)
	{
		if (searchField.getText().length() > 0)
    	{
    		if (searchField.getText().toString().trim().equalsIgnoreCase("nyan cat")
    				|| searchField.getText().toString().trim().equalsIgnoreCase("nyan"))
    		{
    			//Easter Egg 1
    			String url = "http://www.youtube.com/watch?v=QH2-TGUlwu4";
    			Intent procrastinateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    			page.startActivity(Intent.createChooser(procrastinateIntent, "U r not advized 2 " +
    					"proc33d unless u haz good connection"));
    		}
    		else
    		{
	    		if (application == null)
	    		{
	    			String[] argsToPass = new String[1];
	    			argsToPass[0] = searchField.getText().toString();
	    			MyApplication.generalQuery = argsToPass;
	    		}
	    		else
	    		{
	    			String[] argsToPass = new String[2];
	    			argsToPass[0] = searchField.getText()
	    						.toString();
	    			argsToPass[1] = application;
	    			MyApplication.generalQuery = argsToPass;
	    		}
	    		Intent myIntent = new Intent(page.getApplicationContext(),
    					MyApplication.getPageClass(MollyModule.SEARCH_PAGE));
	    		page.startActivityForResult(myIntent, 0);
    		}
    		
    	}
    	else
    	{
    		Toast.makeText(page.getApplicationContext(), "No query found. " + 
    				"Please enter some search criteria", Toast.LENGTH_SHORT).show();
    	}
	}
	
	public static void setEnterKeySearch(final EditText searchField, final Page page, final String application)
	{
		searchField.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                case KeyEvent.KEYCODE_ENTER:
		                	searchFromField(page, searchField, application);
		                	return true;
		                default:
		                    break;
		            }
		        }
				return false;
			}
		});
	}
	
	public static void setClickSearch(View view, final EditText searchField, final Page page, final String application)
	{
		view.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				searchFromField(page, searchField, application);
			}
		});
	}
	
	public void setEmailClick(View view, final String finalAdd)
	{
		view.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
                Intent emailIntent = new Intent(
                			android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { finalAdd });
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			}
		});
	}
	
	public void setPhoneClick(View view, final String phoneNumber)
	{
		view.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//call the number clicked
        		Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:"+phoneNumber));
                startActivity(Intent.createChooser(phoneIntent, "Calling number..."));
			}
    	});
	}
	
	public void setURLClick(View view, final String urlStr)
	{
		view.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//go to the url clicked
				Intent browseIntent = new Intent( Intent.ACTION_VIEW , Uri.parse(urlStr) );
                startActivity(Intent.createChooser(browseIntent, "Connecting..."));
			}
    	});
	}
	
	public static void populateViews(List<View> outputs, ViewGroup contentLayout)
	{
		for (int i = 0; i < outputs.size(); i++)
		{
			System.out.println("adding view...");
			contentLayout.addView(outputs.get(i));
		}
		contentLayout.invalidate();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		Dialog dialog;
 		switch (id) {
 			//logically, this cannot appear before the location layout is updated, so there is no 
 			//chance suggestionsLayout can be null, except you are doing something wrong
 			case DIALOG_LOCATION_SUGGESTIONS:
 				dialog = new Dialog(this);
				dialog.setTitle("Did you mean...");
				dialog.setContentView(detachedSuggestionsLayout);
				return dialog;
				
			case DIALOG_LOCATION_HISTORY:
				//logically, this cannot appear before the location layout is updated, so there is no 
				//chance historyLayout can be null, except you are doing something wrong
				dialog = new Dialog(this);
				dialog.setTitle("Location History");
				dialog.setContentView(detachedHistoryLayout);
				return dialog;
				
			case DIALOG_LOCATION:
				locationLayout = (LinearLayout) getLayoutInflater().inflate
							(R.layout.manual_location_dialog, null);
				
				//the 2 layouts suggestionsLayout and historyLayout are used for bringing up the suggestions and the history
				//and doesn't directly belong to locationLayout but still need to be included to be updated in LocationListTask
				detachedHistoryLayout = (LinearLayout) getLayoutInflater().inflate
						(R.layout.manual_location_subdialog, null);
				
				final LinearLayout historyLayout = (LinearLayout) detachedHistoryLayout.findViewById(R.id.subLocationLayout);
				
				detachedSuggestionsLayout = (LinearLayout) getLayoutInflater().inflate
						(R.layout.manual_location_subdialog, null);
				
				final LinearLayout suggestionsLayout = (LinearLayout) detachedSuggestionsLayout.findViewById(R.id.subLocationLayout); 
				
				//The order in the next part of this code is the same as the order on the layout file
				final TextView currentLocation = (TextView) locationLayout.findViewById(R.id.locationText);
				if (MyApplication.currentLocation != null)
				{
					try {
						currentLocation.setText("Your last updated location is " + '\n' + 
								MyApplication.currentLocation.getString("name") + " within approx. " 
								+ MyApplication.currentLocation.getString("accuracy"));
					} catch (JSONException e) {
						e.printStackTrace();
						currentLocation.setText("Cannot get your current location.");
					}
				} 
				else
				{
					currentLocation.setText("Cannot get your current location.");
				}
				
				final RelativeLayout showSuggestionsLayout = (RelativeLayout) locationLayout.findViewById(R.id.showSuggestionLayout);
				
				//Enable the button if there is some info available
				try {
					if (MyApplication.currentLocation.getJSONArray("alternatives").length() > 0)
					{
						showSuggestionsLayout.setBackgroundResource(R.drawable.bg_blue);
						showSuggestionsLayout.setClickable(true);
						showSuggestionsLayout.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								showDialog(DIALOG_LOCATION_SUGGESTIONS);
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
					//Do nothing
				}
				
				final RelativeLayout showHistoryLayout = (RelativeLayout) locationLayout.findViewById(R.id.showHistoryLayout);
				
				//Enable the button if there is some info available
				try {
					if (MyApplication.currentLocation.getJSONArray("history").length() > 0)
					{
						showHistoryLayout.setBackgroundResource(R.drawable.bg_blue);
						showHistoryLayout.setClickable(true);
						showHistoryLayout.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								showDialog(Page.DIALOG_LOCATION_HISTORY);
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
					//Do nothing
				}
				
				final EditText manualLocationField = (EditText) locationLayout.findViewById(R.id.manualLocationField);
				manualLocationField.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if (event.getAction() == KeyEvent.ACTION_DOWN)
						{
							switch (keyCode) {
							case KeyEvent.KEYCODE_ENTER:
							case KeyEvent.KEYCODE_DPAD_CENTER:
								LocationTracker.autoLoc = false;
								new LocationListTask(manualLocationField.getText().toString(), null, null, null, Page.this, 
											false, true).execute(historyLayout,suggestionsLayout, 
												showHistoryLayout, showSuggestionsLayout, currentLocation); //Get location by geocoded method
								return true;
							default:
								break;
							}
						}
						return false;
					}
				});
				
				Button setLocation = (Button) locationLayout.findViewById(R.id.setLocationButton);
				setLocation.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						LocationTracker.autoLoc = false;
						try {
							new LocationListTask(manualLocationField.getText().toString(), null, null, null, Page.this, 
									false, true).execute(historyLayout, suggestionsLayout, 
											showHistoryLayout, showSuggestionsLayout, currentLocation); //Get location by geocoded method
						} catch (Exception e) {
							Toast.makeText(Page.this, "Your new location cannot be updated. Please try " +
									"again later", Toast.LENGTH_SHORT).show();
						}
					}
				});
				
				RelativeLayout autoLocLayout = (RelativeLayout) locationLayout.findViewById(R.id.autoLocLayout);
				autoLocLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							LocationTracker.autoLoc = true;
							new LocationListTask(null,null, null, null, Page.this, false, true).execute
												(historyLayout, suggestionsLayout, showHistoryLayout, showSuggestionsLayout, currentLocation); // Get location automatically
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(Page.this, "Your new location cannot be updated. Please try again later", 
									Toast.LENGTH_SHORT).show();
						} 
					}
				});
				
				dialog = new LocationDialog(this);
				dialog.setTitle("Update your location");
				dialog.setContentView(locationLayout);
				return dialog;
			default:
				break;
		}
		return super.onCreateDialog(id);
	}
	
	public static void updateLocText(TextView currentLocTextView) throws JSONException
	{
		currentLocTextView.setText("Your last updated location is " + '\n' + 
				MyApplication.currentLocation.getString("name") + " within approx. " 
				+ MyApplication.currentLocation.getString("accuracy"));
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		if (MyApplication.router == null)
		{
			try {
				//router = 
				MyApplication.router = new Router(myApp);;
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this.getApplicationContext(), "Network Connection cannot be set up. " + 
						"Please try again later", Toast.LENGTH_SHORT).show();
			}
		}
		else if (MyApplication.destroyed)
		{
			//myApp has been claimed by garbage collector/killed to save memory for other apps
			//don't set destroyed back to false right here because the onResume() methods of the 
			//subclasses will also check for it to take appropriate actions
			MyApplication.router.setApp(myApp);
		}
		
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH & !this.getName().equals(MollyModule.SEARCH_PAGE))
        {
   			MyApplication.currentApp = this.getName();
        	Intent myIntent = new Intent (getApplicationContext(), NewSearchPage.class);
        	startActivityForResult(myIntent, 0);
        	return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	public abstract String getQuery() throws UnsupportedEncodingException; //to be customised because of the UTF-8 Encoding
	
	public String getAdditionalParams()
	{
		return additionalArgs;
	}
    
    public String getName()
    {
    	return name;
    }
    
    public abstract LinearLayout getContentLayout();
    
    public abstract void setContentLayout(LinearLayout contentLayout);
    
    public abstract void refresh(); //some thing to update the page separately from onResume
}
