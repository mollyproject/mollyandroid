package org.mollyproject.android.view.apps;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
	protected boolean manualRefresh;
	protected boolean favouritable;
	protected boolean isFavourite;
	protected LinearLayout detachedHistoryLayout;
	protected LinearLayout locationLayout;
	public static final int DIALOG_LOCATION = 0;
	public static final int DIALOG_LOCATION_HISTORY = 1;
	
	
	//use someLayout.setLayoutParams() with this paramsWithLine as a parameter makes
	//a gap of 5px below the LinearLayout, this is used here to make gaps between views
	public static LinearLayout.LayoutParams paramsWithLine = new LinearLayout.LayoutParams
			(LinearLayout.LayoutParams.FILL_PARENT, 
			LinearLayout.LayoutParams.FILL_PARENT);
	static { paramsWithLine.setMargins(0, 0, 0, 2); }
	
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
	    switch (item.getItemId()) {
	        case R.id.reload:
	        	manualRefresh = true;
	        	onResume();
	            break;
	        case R.id.location:
	        	showDialog(DIALOG_LOCATION);
	        	break;
	        case R.id.favourite:
	        	try {
		        	if(MyApplication.csrfToken != null)
		        	{
		        		
	        			System.out.println("Fav pressed");
			        	//post the favourite on to the web server
			        	List<NameValuePair> params = new ArrayList<NameValuePair>();
			             
			            params.add(new BasicNameValuePair("csrfmiddlewaretoken", MyApplication.csrfToken));
			            params.add(new BasicNameValuePair("format", "json"));
			            params.add(new BasicNameValuePair("language_code", "en"));
			            System.out.println("fav link " + MyApplication.favouriteURL);
			            params.add(new BasicNameValuePair("URL", MyApplication.favouriteURL));
			            if (!isFavourite)
			        	{
			            	params.add(new BasicNameValuePair("favourite", ""));
			        	}
			            else
			        	{
			            	params.add(new BasicNameValuePair("unfavourite", "Unfavourite"));
			        	}
			             
						List<String> output = MyApplication.router.post(params,
								MyApplication.router.reverse(MollyModule.FAVOURITES, null));
						isFavourite = new JSONObject(output.get(0)).getBoolean("is_favourite");
					} 
	        	}
	        	catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), 
							"This operation cannot be complete, please check your connection and try again.", 
								Toast.LENGTH_SHORT).show();
				}
	        	break;
	        	
	    }
	    return true;
	}
	
	public static void searchFromField(Page page, EditText searchField, String application)
	{
		if (searchField.getText().length() > 0)
    	{
    		if (searchField.getText().toString().toLowerCase().trim().equals("nyan cat")
    				|| searchField.getText().toString().trim().toLowerCase().equals("nyan"))
    		{
    			//Easter Egg 1
    			String url = "http://www.youtube.com/watch?v=QH2-TGUlwu4";
    			Intent procrastinateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    			page.startActivity(Intent.createChooser(procrastinateIntent, "U r not advized 2 " +
    					"proc33d unless u haz good connection"));
    		}
    		else if (application == null)
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
			detachedHistoryLayout = (LinearLayout) getLayoutInflater().inflate
					(R.layout.manual_location_history_dialog, null);
			
			final LinearLayout historyLayout = (LinearLayout) detachedHistoryLayout.findViewById(R.id.historyLayout); 
			
			final TextView currentLocation = (TextView) locationLayout.findViewById(R.id.locationText);
			if (MyApplication.currentLocation != null)
			{
				try {
					currentLocation.setText("Your current location is " + '\n' + 
							MyApplication.currentLocation.getString("name") + " within approx. " 
							+ MyApplication.currentLocation.getString("accuracy"));
				} catch (JSONException e) {
					e.printStackTrace();
					currentLocation.setText("Cannot get your current location.");
				}
			}
			
			final RelativeLayout showHistoryLayout = (RelativeLayout) locationLayout.findViewById(R.id.showHistoryLayout);
			showHistoryLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showDialog(Page.DIALOG_LOCATION_HISTORY);
				}
			});
			
			new LocationListTask(null,Page.this, false, true).execute(historyLayout,currentLocation);
			
			RelativeLayout autoLocLayout = (RelativeLayout) locationLayout.findViewById(R.id.autoLocLayout);
			autoLocLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						LocationTracker.autoLoc = true;
						new LocationListTask(null,Page.this, false, true).execute(historyLayout,currentLocation);
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(Page.this, "Your new location cannot be updated. Please try again later", 
								Toast.LENGTH_SHORT).show();
					} 
				}
			});
			
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
							try {
								new LocationListTask(manualLocationField.getText().toString(), Page.this, 
										false, true).execute(historyLayout,currentLocation);
							} catch (Exception e) {
								e.printStackTrace();
								Toast.makeText(Page.this, "Your new location cannot be updated. Please try " +
										"again later", Toast.LENGTH_SHORT).show();
							}
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
						new LocationListTask(manualLocationField.getText().toString(), Page.this, 
								false, true).execute(historyLayout,currentLocation);
					} catch (Exception e) {
						Toast.makeText(Page.this, "Your new location cannot be updated. Please try " +
								"again later", Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			dialog = new Dialog(this);
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
		currentLocTextView.setText("Your current location is " + '\n' + 
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
	
	public abstract String getQuery() throws UnsupportedEncodingException;
	
	public abstract String getAdditionalParams();
    
    public abstract String getName();
    
    public abstract LinearLayout getContentLayout();
    
    public abstract void setContentLayout(LinearLayout contentLayout);
}
