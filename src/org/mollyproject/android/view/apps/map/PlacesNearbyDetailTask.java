package org.mollyproject.android.view.apps.map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ComplexMapResultTask;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlacesNearbyDetailTask extends ComplexMapResultTask {

	public PlacesNearbyDetailTask(PageWithMap page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}
	
	public LinearLayout parseNearbyEntity(int orderNumber, final JSONObject entity) throws JSONException
	{
		LinearLayout nearbyResultLayout = (LinearLayout) page.getLayoutInflater()
				.inflate(R.layout.clickable_search_result, null);
		String result = orderNumber + ". " + entity.getString("title");
		if (entity.has("distance"))
		{
			result = result  + '\n' + entity.getString("distance") + " " + entity.getString("bearing"); 
		}
		TextView plainResultText = (TextView) nearbyResultLayout.findViewById(R.id.clickableResultText);
		plainResultText.setText(result);
		
		nearbyResultLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					//set identifier_scheme and identifier_value
					String[] placesArgs = new String[2];
					placesArgs[0] = entity.getString("identifier_scheme");
					placesArgs[1] = entity.getString("identifier_value");
					MyApplication.placesArgs = placesArgs;
					Intent myIntent = new Intent (page.getApplicationContext(), 
							MyApplication.getPageClass(MollyModule.PLACES_ENTITY));
					page.startActivityForResult(myIntent, 0);
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(page.getApplicationContext(), "This page is not available", 
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		nearbyResultLayout.setLayoutParams(Page.paramsWithLine);
		return nearbyResultLayout;
	}
	
	@Override
	public void updateView(JSONObject jsonContent) {
		super.updateView(jsonContent);
		try {
			if (!exceptionCaught)
			{
				JSONArray entities = jsonContent.getJSONArray("entities");
				for (int i = 0; i < entities.length(); i++)
				{
					final JSONObject entity = entities.getJSONArray(i).getJSONObject(0);
					page.getContentLayout().addView(parseNearbyEntity(i+1, entity));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(page.getApplicationContext(), "There is an error with the data. " +
					"Information cannot be displayed", Toast.LENGTH_SHORT).show();
		} 
	}
}
