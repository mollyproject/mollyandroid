package org.mollyproject.android.view.pages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
			resultsNo.setTextSize(14);
			resultsNo.setText("Search has returned "+results.length()+" result(s).");
			contentLayout.addView(resultsNo);
			
			if (results.length() > 0)
			{
				ScrollView scr = new ScrollView(this);
				
				for (int i = 0; i < results.length(); i++)
				{
					JSONObject result = results.getJSONObject(i);
					LinearLayout thisResult = new LinearLayout(this);
					thisResult.setOrientation(LinearLayout.VERTICAL);
					
					//name field, unique name
					TextView name = new TextView(this);
					name.setTextSize(18);
					//name.setBackgroundResource(R.drawable.bg_white);
					name.setText((i+1)+". "+result.getString("cn"));
					
					//e-mail field, can have many, stored as a JSONArray in result
					LinearLayout mailLayout = new LinearLayout(this);
					mailLayout.setOrientation(LinearLayout.VERTICAL);
					JSONArray fields = result.getJSONArray("mail");
					String addresses = new String();
					for (int j = 0; j < fields.length(); j++)
					{
						TextView field = new TextView(this);
						field.setTextSize(18);
						addresses = addresses+","+fields.getString(j);
						field.setText(fields.getString(j));
						mailLayout.addView(field);
					}
					final String s = addresses;
					thisResult.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
                            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                            emailIntent.setType("plain/text");
                            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { s });
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
						}
					});
					
					//ou field, i.e. department,college, etc - can have many, stored as
					//a JSONArray in result
					LinearLayout ouLayout = getField(result,"ou");
					
					thisResult.addView(name);
					thisResult.addView(ouLayout);
					thisResult.addView(mailLayout);
					thisResult.setBackgroundResource(R.drawable.bg_blue);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
						(LinearLayout.LayoutParams.FILL_PARENT, 
						LinearLayout.LayoutParams.FILL_PARENT);
				    params.setMargins(0, 0, 0, 5); // (left, top, right, bottom)					
					thisResult.setLayoutParams(params);
					thisResult.setPadding(10, 10, 0, 10);
					
					resultsLayout.addView(thisResult);
				}
				resultsLayout.setBackgroundResource(R.drawable.bg_white);
				scr.addView(resultsLayout);
				contentLayout.addView(scr);
			}
			
		} catch (JSONException e) {
			//problem here, json not received from server
			e.printStackTrace();
		}
			
	}
	
	private LinearLayout getField(JSONObject toBeExtracted, String locator) throws JSONException
	{
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		JSONArray fields = toBeExtracted.getJSONArray(locator);
		for (int j = 0; j < fields.length(); j++)
		{
			TextView field = new TextView(this);
			field.setTextSize(18);
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
