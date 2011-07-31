package org.mollyproject.android.view.apps;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class HomePage extends Page {
	
	protected ArrayList<Button> breadCrumbs;
	protected LinearLayout bcLayout;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        
    	myApp.updateBreadCrumb(SelectionManager.getName(HomePage.class));
        System.out.println("Home added breadcrumb");
    	
		LinearLayout contentLayout = new LinearLayout(this);
		contentLayout.setOrientation(LinearLayout.VERTICAL);
		contentLayout.addView(bcBar.getBar(), new ViewGroup.LayoutParams
				(getWindowManager().getDefaultDisplay().getWidth(), 
				getWindowManager().getDefaultDisplay().getHeight()/10));
		
		ScrollView scr = new ScrollView(this); 
		
		Button resultsButton = new Button(this);
		resultsButton.setText("Go to Results");
		resultsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), SelectionManager
						.getPageClass(SelectionManager.RESULTS_PAGE));
                startActivityForResult(myIntent, 0);
			}
		});
		
		Button contactsButton = new Button(this);
		contactsButton.setText("Go to Contacts");
		contactsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), SelectionManager
						.getPageClass(SelectionManager.CONTACT_PAGE));
                startActivityForResult(myIntent, 0);
			}
		});
		
		Button featureButton = new Button(this);
		featureButton.setText("Suggest new features");
		featureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), SelectionManager
						.getPageClass(SelectionManager.FEATURE_VOTE));
                startActivityForResult(myIntent, 0);
			}
		});
		
		Button libraryButton = new Button(this);
		libraryButton.setText("Go to Library Search");
		libraryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), SelectionManager
						.getPageClass(SelectionManager.LIBRARY_PAGE));
                startActivityForResult(myIntent, 0);
			}
		});
		
		Button placesButton = new Button(this);
		placesButton.setText("Go to Places");
		placesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), SelectionManager
						.getPageClass(SelectionManager.PLACES_PAGE));
                startActivityForResult(myIntent, 0);
			}
		});
		
		contentLayout.addView(contactsButton);
		contentLayout.addView(resultsButton);
		contentLayout.addView(libraryButton);
		contentLayout.addView(placesButton);
		contentLayout.addView(featureButton);
		
		scr.addView(contentLayout);
		setContentView(scr);
    }
    
    public Page getInstance()
    {
    	return this;
    }
	
    @Override
    public void onResume()
    {
    	super.onResume();
    	pDialog = ProgressDialog.show(this, "", "Loading...", true, false);
    	new NetworkPollingTask().execute();
    	//home page still contributes to breadcrumb update, but doesn't need a bar on it
    	//so no need to call bcBar.reconstruct
		myApp.updateBreadCrumb(SelectionManager.getName(getInstance().getClass()));
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private class NetworkPollingTask extends AsyncTask<Void,Void,Void>
    {
    	protected boolean jsonException = false;
    	protected boolean networkException = false;
    	
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				System.out.println("Router "+router);
				if (router.getLocThread() != null)
		    	{
			    	if (router.getLocThread().isInterrupted())
			    	{
			    		System.out.println("LocThread needs to restart");
						router.spawnNewLocThread();
			    	}
	    		}
				else
				{
					//LocThread is actually null, it is not there
					//this happens when either no connection has been made before
					//or the LocThread has been made null to prevent being wiped 
		    		String jsonText = router.onRequestSent(
							SelectionManager.getName(HomePage.this.getClass()),
							Router.JSON,null);
		    		System.out.println("JSON Text " + jsonText);
					jsonContent = new JSONObject(jsonText);
					router.spawnNewLocThread();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				networkException = true;
			} catch (JSONException e) {
				e.printStackTrace();
				jsonException = true;
			} catch (NullPointerException e)
			{
				e.printStackTrace();
				networkException = true;
			} catch (UnknownHostException e) {
				e.printStackTrace();
				networkException = true;
			} catch (IOException e) {
				e.printStackTrace();
				networkException = true;
			} 
			return null;
		}
    	
		protected void onPostExecute(Void result)
		{
			System.out.println(networkException +" "+ jsonException);
			pDialog.dismiss();
			
			if (networkException) 
			{
				popupErrorDialog("Cannot connect to m.ox.ac.uk", 
						"There might be a problem with internet connection. " +
						"Please try restarting the app", HomePage.this, true);
			}
			if (jsonException)
			{
				popupErrorDialog("JSON Exception", 
						"There might be a problem with JSON output " +
						"from server. Please try again later.", HomePage.this, true);
			}
			
		}
    }
    
}





























