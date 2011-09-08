package org.mollyproject.android.view.apps.map;

import org.json.JSONException;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlacesPage extends ContentPage {
	protected LinearLayout placesLayout;
	public static TextView currentLocation;
	public static RelativeLayout nearby;
	protected EditText searchField;
	protected Button searchButton;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		placesLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.places, null);
		contentLayout.addView(placesLayout);
		
		searchField = (EditText) placesLayout.findViewById(R.id.searchField);
		searchField.setWidth(LayoutParams.FILL_PARENT);
		//search using application=places
    	setEnterKeySearch(searchField, this, "places");
    	
    	searchButton = (Button) placesLayout.findViewById(R.id.searchButton);
    	setClickSearch(searchButton, searchField, this, "places");
    	
    	//Functions of these following views are defined in PlacesTask
    	nearby = (RelativeLayout) placesLayout.findViewById(R.id.nearby);
    	currentLocation = (TextView) placesLayout.findViewById(R.id.currentLocation);
	}
	
	@Override
	public void onResume() {
		manualRefresh = true; //always reload this page for the newest location 
		super.onResume();
	}
	
	@Override
	public void refresh() {
		new PlacesTask(this, false, true).execute();
	}
	
	@Override
	public Page getInstance() {
		return this;
	}
	@Override
	public String getAdditionalParams() {
		return null;
	}
	@Override
	public String getName() {
		return MollyModule.PLACES_PAGE;
	}
	@Override
	public String getQuery() {
		return null;
	}
	
}
