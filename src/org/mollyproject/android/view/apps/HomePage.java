package org.mollyproject.android.view.apps;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.Renderer;
import android.content.Intent;
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
        
    	myApp.addBreadCrumb(SelectionManager.getName(HomePage.class));
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
    	if (router.getLocThread() != null)
    	{
	    	if (router.getLocThread().isInterrupted())
	    	{
	    		System.out.println("LocThread needs to restart");
	    		try {
					router.spawnNewLocThread
					(router.getCookieManager().getCSRFToken(new URL (Router.mOX)));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
    	}
    	else
    	{
    		try {
				jsonContent = new JSONObject(router.onRequestSent(SelectionManager.getName(getClass()),Router.JSON,null));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	myApp.getRouter().getLocThread().stopThread();
    	myApp.getRouter().getLocThread().interrupt();
    }
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            onDestroy();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}