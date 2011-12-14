package org.mollyproject.android.view.apps.contact;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class NewContactResultsTask extends JSONProcessingTask {

	public NewContactResultsTask(ContentPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			JSONArray results = jsonContent.getJSONArray("results");
			
			LinearLayout generalResultsLayout = (LinearLayout) page.getLayoutInflater().inflate
					(R.layout.general_search_results_page, null);
			LinearLayout resultsList = (LinearLayout) generalResultsLayout.findViewById(R.id.generalResultsList);
			
			String notification = new String();
			
			if (results.length() == 0)
			{
				notification = "Your search returned nothing. Please try another query.";
			}
			else
			{
				for (int i = 0; i < results.length(); i++)
				{
					JSONObject result = results.getJSONObject(i);
					LinearLayout contactLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.clickable_search_result, null);
					
					//name field
					final String name = result.getString("cn"); //declared as final for use in an onClickListener later
					
					//ou field, i.e. department,college, etc - can have many, stored as
					//a JSONArray in result
					String ou = new String();
					JSONArray ouFields = result.getJSONArray("ou");
					for (int j = 0; j < ouFields.length(); j++)
					{
						ou = ou + ouFields.getString(j) + '\n';
					}
					
					//e-mail/phone field, can have many, stored as a JSONArray in result
					//check if the results are e-mails or phone numbers
					if(jsonContent.getString(ContactPage.MEDIUM).equals(ContactPage.EMAIL))
					{
						//case email
						notification = "Your search for this e-mail query returned "+results.length()+" result(s).";
						
						JSONArray mailFields = result.getJSONArray("mail");
						String addresses = new String();
						for (int j = 0; j < mailFields.length(); j++)
						{
							//display each address field
							addresses = addresses + mailFields.get(j);
							if (j < mailFields.length()-1)
							{
								addresses = addresses +",";
							}
						}
						final String finalAdd = addresses;
						((TextView) contactLayout.findViewById(R.id.clickableResultText)).setText((i+1)+". "+ name + '\n' + ou + addresses);
						//go to email app when clicked on - easy: just add all the addresses
						//belonging to this person seen so far to the recipient's field
						page.setEmailClick(contactLayout,finalAdd);
						((ImageView) contactLayout.findViewById(R.id.arrow)).setImageResource(R.drawable.list_email);
					}
					else if (jsonContent.getString(ContactPage.MEDIUM).equals(ContactPage.PHONE))
					{
						//case phone (a bit more complicated)
						notification = "Your search for this phone query returned "+results.length()+" result(s).";
						
						List<String> numbers = new ArrayList<String>();
						JSONArray phoneFields = result.getJSONArray("telephoneNumber");
						String phoneNums = new String();
						for (int j = 0; j < phoneFields.length(); j++)
						{
							numbers.add((String) phoneFields.get(j));
							phoneNums = phoneFields.getString(j);
							if (j < phoneFields.length() - 1)
							{
								phoneNums = phoneNums + '\n';
							}
						}
						((TextView) contactLayout.findViewById(R.id.clickableResultText)).setText((i+1)+". "+ name + '\n' + ou + phoneNums);
						final List<String> finalNumbers = numbers;
						
						contactLayout.setOnClickListener(new OnClickListener(){
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
				                	thisNum.setTextSize(22);
				                	thisNum.setTextColor(R.color.black);
				                	thisNum.setPadding(5, 5, 5, 5);
				                	thisNum.setBackgroundResource(R.drawable.bg_white);
				                	page.setPhoneClick(thisNum, num);
				                	//draw a small line underneath by leaving a gap
				                	thisNum.setLayoutParams(Page.paramsWithLine);				                	
				                	numLayout.addView(thisNum);
				                }
				                dialog.setTitle("Call "+name+" by:");
				                scr.addView(numLayout);
				                dialog.setContentView(scr);
				                dialog.show();
							}
						});
						((ImageView) contactLayout.findViewById(R.id.arrow)).setImageResource(R.drawable.list_phone);
					}
					if (results.length() > 50)
					{
						notification = notification + 
						" Try adding an initial to get more specific results.";
					}
					contactLayout.setLayoutParams(Page.paramsWithLine);
					resultsList.addView(contactLayout);
				}
			}
			((TextView) generalResultsLayout.findViewById(R.id.searchResultsHeader)).setText(notification);
			LinearLayout previousResults = (LinearLayout) page.getContentLayout().findViewById(R.id.generalResultsLayout);
			if (previousResults != null)
			{
				page.getContentLayout().removeView(previousResults);
			}
			page.getContentLayout().addView(generalResultsLayout);
			((ContentPage) page).doneProcessingJSON();
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
	}
	
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		if(Page.manualRefresh)
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
