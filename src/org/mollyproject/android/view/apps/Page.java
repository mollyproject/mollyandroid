package org.mollyproject.android.view.apps;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import roboguice.activity.RoboActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public abstract class Page extends RoboActivity {
	protected MyApplication myApp;
	protected LayoutInflater layoutInflater; //a layout inflater helps bringing a pre-designed xml layout into the UI
	protected SharedPreferences.Editor editor;
	protected SharedPreferences settings;
	
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
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public abstract Page getInstance();
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	public static void popupErrorDialog(String title, String message, final Context context)
	{
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog.show();
	}
	
	public static void popupErrorDialog(String title, String message, final Page page, 
			final boolean toFinish)
	{
		final AlertDialog alertDialog = new AlertDialog.Builder(page).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (toFinish)
				{
					page.finish();
				}
			}
		});
		alertDialog.show();
	}
	
	public void setEnterKeySearch(final EditText searchField, final Page page, final String application)
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
		                	if (searchField.getText().length() > 0)
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
		                		Intent myIntent = new Intent(page, 
		                					MyApplication.getPageClass(MollyModule.SEARCH_PAGE));
		                		page.startActivityForResult(myIntent, 0);
		                	}
		                	else
		                	{
		                		Toast.makeText(getApplicationContext(), "No query found. " + 
		                				"Please enter some search criteria", Toast.LENGTH_SHORT).show();
		                	}
		                	return true;
		                default:
		                    break;
		            }
		        }
				return false;
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
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.reload:
	        	onResume();
	            break;
	    }
	    return true;
	}
	
	/*public Router getRouter()
	{
		return router;
	}*/
	
	public LayoutInflater getLayoutInflater()
	{
		return layoutInflater;
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
	
	public abstract String getQuery() throws UnsupportedEncodingException;
	
	public abstract String getAdditionalParams();
    
    public abstract String getName();
    
    public abstract LinearLayout getContentLayout();
}
