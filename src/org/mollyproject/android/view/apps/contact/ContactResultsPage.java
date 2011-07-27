package org.mollyproject.android.view.apps.contact;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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
			resultsNo.setTextSize(16);
			resultsNo.setTextColor(R.color.black);
			resultsNo.setPadding(10, 25, 0, 25);
			resultsNo.setBackgroundResource(R.drawable.bg_white);
			//notification
			String notification = "Your search returned "+results.length()+" result(s).";
			if (results.length() > 50)
			{
				notification = notification + " Try adding an initial to get more specific results.";
			}
			resultsNo.setText(notification);
			contentLayout.addView(resultsNo);
			
			if (results.length() > 0)
			{
				ScrollView scr = new ScrollView(this);
				
				for (int i = 0; i < results.length(); i++)
				{
					JSONObject result = results.getJSONObject(i);
					LinearLayout thisResult = new LinearLayout(this);
					thisResult.setOrientation(LinearLayout.VERTICAL);
					
					//name field
					TextView name = new TextView(this);
					name.setTextSize(18);
					final String finalName = result.getString("cn"); //declared as final for use in an onClickListener later
					name.setText((i+1)+". "+finalName);
					
					//ou field, i.e. department,college, etc - can have many, stored as
					//a JSONArray in result
					LinearLayout ouLayout = new LinearLayout(this);
					String ou = new String();
					ouLayout.setOrientation(LinearLayout.VERTICAL);
					JSONArray ouFields = result.getJSONArray("ou");
					for (int j = 0; j < ouFields.length(); j++)
					{
						TextView field = new TextView(this);
						field.setTextSize(18);
						field.setText(ouFields.getString(j));
						ou = ou + ouFields.getString(j) + '\n';
						ouLayout.addView(field);
					}
					
					//e-mail/phone field, can have many, stored as a JSONArray in result
					LinearLayout fieldLayout = new LinearLayout(this);
					fieldLayout.setOrientation(LinearLayout.VERTICAL);
					
					//check if the results are e-mails or phone numbers
					if(output.getString(ContactPage.MEDIUM).equals(ContactPage.EMAIL))
					{
						//case email
						JSONArray mailFields = result.getJSONArray("mail");
						String addresses = new String();
						for (int j = 0; j < mailFields.length(); j++)
						{
							//display each address field
							TextView field = new TextView(this);
							field.setTextSize(18);
							addresses = addresses + mailFields.get(j)+",";
							field.setText(mailFields.getString(j));
							fieldLayout.addView(field);
						}
						final String finalAdd = addresses;
						//go to email app when clicked on - easy: just add all the addresses
						//belonging to this person seen so far to the recipient's field
						thisResult.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View v) {
	                            final Intent emailIntent = new Intent(
	                            			android.content.Intent.ACTION_SEND);
	                            emailIntent.setType("plain/text");
	                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { finalAdd });
	                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
							}
						});
					}
					else if (output.getString(ContactPage.MEDIUM).equals(ContactPage.PHONE))
					{
						//case phone (a bit more complicated
						List<String> numbers = new ArrayList<String>();
						JSONArray phoneFields = result.getJSONArray("telephoneNumber");
						for (int j = 0; j < phoneFields.length(); j++)
						{
							TextView field = new TextView(this);
							field.setTextSize(18);
							numbers.add((String) phoneFields.get(j));
							field.setText(phoneFields.getString(j));
							fieldLayout.addView(field);
						}
						final List<String> finalNumbers = numbers;
						
						thisResult.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View v) {
								Dialog dialog = new Dialog(v.getContext());
				                dialog.setCancelable(true);
				                //go to phone app when clicked on - pops up a dialog to choose
				                //the number to dial
				                LinearLayout numLayout = new LinearLayout(v.getContext());
				                numLayout.setOrientation(LinearLayout.VERTICAL);
				                ScrollView scr = new ScrollView(v.getContext());
				                for (final String num : finalNumbers)
				                {
				                	//display each number
				                	TextView thisNum = new TextView(v.getContext());
				                	thisNum.setText(num);
				                	thisNum.setTextSize(18);
				                	thisNum.setPadding(10, 10, 0, 10);
				                	thisNum.setBackgroundResource(R.drawable.bg_white);
				                	thisNum.setOnClickListener(new OnClickListener(){
										@Override
										public void onClick(View v) {
											//call the number clicked
					                		final Intent phoneIntent = new Intent(Intent.ACTION_CALL);
				                            phoneIntent.setData(Uri.parse("tel:"+num));
				                            startActivity(Intent.createChooser(phoneIntent, "Calling number..."));
										}
				                	});
				                	//draw a small line underneath by leaving a gap
				                	thisNum.setLayoutParams(paramsWithLine);				                	
				                	numLayout.addView(thisNum);
				                }
				                dialog.setTitle("Call "+finalName+" by:");
				                scr.addView(numLayout);
				                dialog.setContentView(scr);
				                dialog.show();
							}
						});
					}
					
					thisResult.addView(name);
					thisResult.addView(ouLayout);
					thisResult.addView(fieldLayout);
					
					thisResult.setBackgroundResource(R.drawable.bg_blue);
					thisResult.setLayoutParams(paramsWithLine);
					thisResult.setPadding(10, 10, 0, 10);
					resultsLayout.addView(thisResult);
				}
				resultsLayout.setBackgroundResource(R.drawable.bg_white);
				scr.addView(resultsLayout);
				contentLayout.addView(scr);
				myApp.timeStop();
			}
			
		} catch (JSONException e) {
			//problem here, json not received from server
			e.printStackTrace();
		}
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

}
