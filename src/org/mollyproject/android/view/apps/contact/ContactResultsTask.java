package org.mollyproject.android.view.apps.contact;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ContactResultsTask extends BackgroundTask<String, Void, LinearLayout>
{
	protected LinearLayout contentLayout;
	protected LinearLayout contactSearchBar;
	public ContactResultsTask(ContactPage contactPage, LinearLayout contactSearchBar,
			boolean toDestroy, boolean dialog) {
		super(contactPage,toDestroy,dialog);
		contentLayout = ((ContactPage) page).getContentLayout();
		this.contactSearchBar = contactSearchBar;
	}

	@Override
	protected LinearLayout doInBackground(String... args) {
		try {
			String searchQuery = "query="+URLEncoder.encode(args[0],"UTF-8")+"&medium="+args[1];
			//((ContactPage) page).setContactOutput();

			List<View> outputs = new ArrayList<View>();
			JSONObject searchOutput = page.getRouter().onRequestSent("contact:result_list",
					null, Router.OutputFormat.JSON, searchQuery);
			JSONArray results = searchOutput.getJSONArray("results");
			
			LinearLayout resultsLayout = new LinearLayout(page);
			resultsLayout.setOrientation(LinearLayout.VERTICAL);
			
			TextView resultsNo = new TextView(page);
			resultsNo.setTextSize(16);
			resultsNo.setTextColor(R.color.blue);
			resultsNo.setPadding(10, 20, 0, 20);
			resultsNo.setBackgroundResource(R.drawable.bg_white);
			//notification
			String notification = "Your search returned "+results.length()+" result(s).";
			if (results.length() > 50)
			{
				notification = notification + 
				" Try adding an initial to get more specific results.";
			}
			resultsNo.setText(notification);
			resultsLayout.addView(resultsNo);
			//outputs.add(resultsNo);
			
			if (results.length() > 0)
			{
				//ScrollView scr = new ScrollView(page);
				
				for (int i = 0; i < results.length(); i++)
				{
					JSONObject result = results.getJSONObject(i);
					LinearLayout thisResult = new LinearLayout(page);
					thisResult.setOrientation(LinearLayout.VERTICAL);
					
					//name field
					TextView name = new TextView(page);
					name.setTextSize(18);
					final String finalName = result.getString("cn"); //declared as final for use in an onClickListener later
					name.setText((i+1)+". "+finalName);
					
					//ou field, i.e. department,college, etc - can have many, stored as
					//a JSONArray in result
					LinearLayout ouLayout = new LinearLayout(page);
					String ou = new String();
					ouLayout.setOrientation(LinearLayout.VERTICAL);
					JSONArray ouFields = result.getJSONArray("ou");
					for (int j = 0; j < ouFields.length(); j++)
					{
						TextView field = new TextView(page);
						field.setTextSize(18);
						field.setText(ouFields.getString(j));
						ou = ou + ouFields.getString(j) + '\n';
						ouLayout.addView(field);
					}
					
					//e-mail/phone field, can have many, stored as a JSONArray in result
					LinearLayout fieldLayout = new LinearLayout(page);
					fieldLayout.setOrientation(LinearLayout.VERTICAL);
					
					//check if the results are e-mails or phone numbers
					if(searchOutput.getString(ContactPage.MEDIUM).equals(ContactPage.EMAIL))
					{
						//case email
						JSONArray mailFields = result.getJSONArray("mail");
						String addresses = new String();
						for (int j = 0; j < mailFields.length(); j++)
						{
							//display each address field
							TextView field = new TextView(page);
							field.setTextSize(18);
							addresses = addresses + mailFields.get(j)+",";
							field.setText(mailFields.getString(j));
							fieldLayout.addView(field);
						}
						final String finalAdd = addresses;
						//go to email app when clicked on - easy: just add all the addresses
						//belonging to this person seen so far to the recipient's field
						page.setEmailClick(thisResult,finalAdd);
					}
					else if (searchOutput.getString(ContactPage.MEDIUM).equals(ContactPage.PHONE))
					{
						//case phone (a bit more complicated)
						List<String> numbers = new ArrayList<String>();
						JSONArray phoneFields = result.getJSONArray("telephoneNumber");
						for (int j = 0; j < phoneFields.length(); j++)
						{
							TextView field = new TextView(page);
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
				                	thisNum.setBackgroundResource(R.drawable.bg_white);
				                	page.setPhoneClick(thisNum, num);
				                	//draw a small line underneath by leaving a gap
				                	thisNum.setLayoutParams(Page.paramsWithLine);				                	
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
					thisResult.setLayoutParams(Page.paramsWithLine);
					thisResult.setPadding(10, 10, 0, 10);
					resultsLayout.addView(thisResult);
				}
				resultsLayout.setBackgroundResource(R.drawable.bg_white);
				//scr.addView(resultsLayout);
				outputs.add(resultsLayout);
				System.out.println("Search completed, returned "+results.length()
									+" results");
			}
			return resultsLayout;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			unknownHostException = true;
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (IOException e) {
			e.printStackTrace();
			ioException = true;
		}
		return null;
	}
	
	@Override
	public void updateView(LinearLayout resultsLayout) {
		((ViewGroup) contactSearchBar.getParent()).removeAllViews();
		contentLayout.removeAllViews();
		
		resultsLayout.addView(contactSearchBar, 0);
		ScrollView scroll = new ScrollView(page);
		scroll.addView(resultsLayout);
		contentLayout.addView(scroll);
		//Page.populateViews(outputs, scroll);
		page.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}
