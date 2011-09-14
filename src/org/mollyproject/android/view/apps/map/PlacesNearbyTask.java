package org.mollyproject.android.view.apps.map;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.ComplexMapResultTask;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlacesNearbyTask extends ComplexMapResultTask {

	public PlacesNearbyTask(ContentPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}
	
	public static LinearLayout parsePlaceEntity(final JSONObject entity, final Page page) throws JSONException
	{
		LinearLayout resultLayout = (LinearLayout) page.getLayoutInflater().inflate
				(R.layout.clickable_search_result, null);
		TextView result = (TextView) resultLayout.findViewById(R.id.clickableResultText);
		
		//apply toUpperCase to the first character in the retrieved string then append it back
		String verbose = new String();
		if (entity.getInt("entities_found") > 1)
		{
			verbose = entity.getString("verbose_name_plural");
		}
		else
		{
			verbose = entity.getString("verbose_name_singular");
		}
		Character c = Character.toUpperCase(verbose.charAt(0));
		verbose = c.toString() + verbose.subSequence(1, verbose.length());

		verbose = verbose + " (" + entity.getString("entities_found") + " within " 
						+ entity.getString("max_distance") + ")";
		result.setText(verbose);
		resultLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					MyApplication.placesNearbySlug = entity.getString("slug");
					Intent myIntent = new Intent(page.getApplicationContext(), 
							MyApplication.getPageClass(MollyModule.PLACES_NEARBY_DETAIL));
					page.startActivityForResult(myIntent, 0);
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(page.getApplicationContext(), "This page is currently unavailable", 
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		resultLayout.setLayoutParams(Page.paramsWithLine);
		return resultLayout;
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		super.updateView(jsonContent);
		try {
			page.getContentLayout().removeAllViews();
			
			//set up an underlying white background to show the lines
			LinearLayout nearbyPlacesLayout = new LinearLayout(page.getApplicationContext());
			nearbyPlacesLayout.setLayoutParams(Page.paramsWithLine);
			nearbyPlacesLayout.setOrientation(LinearLayout.VERTICAL);
			nearbyPlacesLayout.setBackgroundResource(R.drawable.shape_white);
			page.getContentLayout().addView(nearbyPlacesLayout);
			
			JSONObject entityTypes = jsonContent.getJSONObject("entity_types");
			Iterator<String> entityTypeKeys = entityTypes.keys();
			
			//Check for empty case
			if (!entityTypeKeys.hasNext())
			{
				TextView entityTypeHeader = new TextView(page.getApplicationContext());
				entityTypeHeader.setText("Sorry, there is no information available. " +
						"You might want to check your current location");
				entityTypeHeader.setTypeface(Typeface.DEFAULT_BOLD);
				entityTypeHeader.setTextSize(22);
				entityTypeHeader.setBackgroundResource(R.drawable.shape_white);
				entityTypeHeader.setPadding(5, 10, 5, 10);
				entityTypeHeader.setTextColor(page.getResources().getColor(R.color.blue));
				
				nearbyPlacesLayout.addView(entityTypeHeader);
			}
			
			while (entityTypeKeys.hasNext())
			{
				String key = entityTypeKeys.next();
				JSONArray entityType = entityTypes.getJSONArray(key);
				if (entityType.length() > 0)
				{
					TextView entityTypeHeader = new TextView(page.getApplicationContext());
					entityTypeHeader.setText(key);
					entityTypeHeader.setTypeface(Typeface.DEFAULT_BOLD);
					entityTypeHeader.setTextSize(22);
					entityTypeHeader.setBackgroundResource(R.drawable.shape_white);
					entityTypeHeader.setPadding(5, 10, 5, 10);
					entityTypeHeader.setTextColor(page.getResources().getColor(R.color.blue));
					
					nearbyPlacesLayout.addView(entityTypeHeader);
					
					//start parsing each entities
					for (int i = 0; i < entityType.length(); i++)
					{
						JSONObject entity = entityType.getJSONObject(i);
						LinearLayout entityLayout = parsePlaceEntity(entity, page);
						nearbyPlacesLayout.addView(entityLayout);
					}
				}
			}
			((ContentPage) page).doneProcessingJSON();
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
	}

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		if (Page.manualRefresh)
		{
			return super.doInBackground();
		}
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ((ContentPage) page).getJSONContent();
	}

}
