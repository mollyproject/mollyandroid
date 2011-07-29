package org.mollyproject.android.view.apps;

import java.util.ArrayList;

import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.breadcrumbs.ImprovedBreadCrumbBar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public abstract class Page extends Activity {
	protected ImprovedBreadCrumbBar bcBar;
	protected MyApplication myApp;
	protected ArrayList<String> trail;
	protected JSONObject jsonContent;
	protected String jsonText;
	protected LinearLayout contentLayout;
	protected Router router;
	//public abstract void refresh();
	
	//use someLayout.setLayoutParams() with this paramsWithLine as a parameter makes
	//a gap of 5px below the LinearLayout, this is used hee to make gaps between views
	public static LinearLayout.LayoutParams paramsWithLine = new LinearLayout.LayoutParams
			(LinearLayout.LayoutParams.FILL_PARENT, 
			LinearLayout.LayoutParams.FILL_PARENT);
	static { paramsWithLine.setMargins(0, 0, 0, 5); }
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		myApp = (MyApplication) getApplication();
		bcBar = new ImprovedBreadCrumbBar(getInstance());
		router = myApp.getRouter();
	}
	
	public abstract Page getInstance();
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	public static AlertDialog popupErrorDialog(String title, String message, Context context)
	{
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		return alertDialog;
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
                startActivity(Intent.createChooser(browseIntent, "Calling number..."));
			}
    	});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.icon:     
	        	Toast.makeText(this, "You pressed the icon!", Toast.LENGTH_LONG).show();
	        	break;
	        case R.id.text:     
	        	Toast.makeText(this, "You pressed the text!", Toast.LENGTH_LONG).show();
	            break;
	        case R.id.icontext:
	        	
	            break;
	    }
	    return true;
	}
	
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	myApp.getRouter().getLocThread().stopThread();
    	myApp.getRouter().getLocThread().interrupt();
    }
}
