package org.mollyproject.android.view.pages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactResultsPage extends ContentPage {
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		String jsonOutput = myApp.getContactOutput();
		
		try {
			JSONObject output = new JSONObject(jsonOutput);
			JSONArray results = output.getJSONArray("results");
			LinearLayout resultsLayout = new LinearLayout(this);
			resultsLayout.setOrientation(LinearLayout.VERTICAL);
			
			TextView resultsNo = new TextView(this);
			resultsNo.setText("Search has returned "+results.length()+" result(s).");
			contentLayout.addView(resultsNo);
			if (results.length() > 0)
			{
				for (int i = 0; i < results.length(); i++)
				{
					JSONObject result = results.getJSONObject(i);
					LinearLayout thisResult = new LinearLayout(this);
					
					//name field, unique name
					TextView name = new TextView(this);
					name.setText(result.getString("cn"));
					
					//e-mail field, can have many, stored as a JSONArray in result
					LinearLayout mailLayout = getField(result,"mail");
					
					//ou field, i.e. department,college, etc - can have many, stored as
					//a JSONArray in result
					LinearLayout ouLayout = getField(result,"ou");
					
					thisResult.addView(ouLayout);
					thisResult.addView(mailLayout);
					
					contentLayout.addView(thisResult);
				}
			}
			
		} catch (JSONException e) {
			//problem here, json not received from server
			e.printStackTrace();
		}
			
	}
	
	private LinearLayout getField(JSONObject toBeExtracted, String locator) throws JSONException
	{
		LinearLayout layout = new LinearLayout(this);
		JSONArray fields = toBeExtracted.getJSONArray(locator);
		for (int j = 0; j < fields.length(); j++)
		{
			TextView field = new TextView(this);
			field.setText(fields.getString(j));
			layout.addView(field);
		}
		return layout;
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

}
