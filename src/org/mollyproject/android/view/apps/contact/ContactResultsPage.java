package org.mollyproject.android.view.apps.contact;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.ResultsDisplayPage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ContactResultsPage extends ResultsDisplayPage {
	
	protected ProgressDialog pDialog = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Show the ProgressDialog on this thread
        pDialog = ProgressDialog.show(this, "", "Loading...", true, false);
        /*pDialog.setButton(ProgressDialog.BUTTON_POSITIVE, "Cancel", 
        		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});*/
        
        // Start a new thread that will download all the data
        query = myApp.getContactQuery();
        new ContactResultsTask().execute(contentLayout);
	}
	
	private class ContactResultsTask extends  AsyncTask<LinearLayout, Void, List<View>>
	{
		protected boolean jsonException = false;
		protected boolean otherException = false;
		@Override
		protected List<View> doInBackground(LinearLayout... args) {
			try {
				System.out.println("ASYNC");
				List<View> outputs = new ArrayList<View>();
				String jsonOutput = router.onRequestSent(SelectionManager
						.getName(ContactResultsPage.this.getClass()),
						Router.OutputFormat.JSON, query);
				System.out.println(jsonOutput);
				JSONObject output = new JSONObject(jsonOutput);
				JSONArray results = output.getJSONArray("results");
				
				LinearLayout resultsLayout = new LinearLayout(ContactResultsPage.this);
				resultsLayout.setOrientation(LinearLayout.VERTICAL);
				
				TextView resultsNo = new TextView(ContactResultsPage.this);
				resultsNo.setTextSize(16);
				resultsNo.setTextColor(R.color.black);
				resultsNo.setPadding(10, 20, 0, 20);
				resultsNo.setBackgroundResource(R.drawable.bg_white);
				//notification
				String notification = "Your search returned "+results.length()+" result(s).";
				if (results.length() > 50)
				{
					notification = notification + " Try adding an initial to get more specific results.";
				}
				resultsNo.setText(notification);
				outputs.add(resultsNo);
				
				if (results.length() > 0)
				{
					ScrollView scr = new ScrollView(ContactResultsPage.this);
					
					for (int i = 0; i < results.length(); i++)
					{
						JSONObject result = results.getJSONObject(i);
						LinearLayout thisResult = new LinearLayout(ContactResultsPage.this);
						thisResult.setOrientation(LinearLayout.VERTICAL);
						
						//name field
						TextView name = new TextView(ContactResultsPage.this);
						name.setTextSize(18);
						final String finalName = result.getString("cn"); //declared as final for use in an onClickListener later
						name.setText((i+1)+". "+finalName);
						
						//ou field, i.e. department,college, etc - can have many, stored as
						//a JSONArray in result
						LinearLayout ouLayout = new LinearLayout(ContactResultsPage.this);
						String ou = new String();
						ouLayout.setOrientation(LinearLayout.VERTICAL);
						JSONArray ouFields = result.getJSONArray("ou");
						for (int j = 0; j < ouFields.length(); j++)
						{
							TextView field = new TextView(ContactResultsPage.this);
							field.setTextSize(18);
							field.setText(ouFields.getString(j));
							ou = ou + ouFields.getString(j) + '\n';
							ouLayout.addView(field);
						}
						
						//e-mail/phone field, can have many, stored as a JSONArray in result
						LinearLayout fieldLayout = new LinearLayout(ContactResultsPage.this);
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
								TextView field = new TextView(ContactResultsPage.this);
								field.setTextSize(18);
								addresses = addresses + mailFields.get(j)+",";
								field.setText(mailFields.getString(j));
								fieldLayout.addView(field);
							}
							final String finalAdd = addresses;
							//go to email app when clicked on - easy: just add all the addresses
							//belonging to this person seen so far to the recipient's field
							setEmailClick(thisResult,finalAdd);
						}
						else if (output.getString(ContactPage.MEDIUM).equals(ContactPage.PHONE))
						{
							//case phone (a bit more complicated)
							List<String> numbers = new ArrayList<String>();
							JSONArray phoneFields = result.getJSONArray("telephoneNumber");
							for (int j = 0; j < phoneFields.length(); j++)
							{
								TextView field = new TextView(ContactResultsPage.this);
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
					                	setPhoneClick(thisNum, num);
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
					outputs.add(scr);
					System.out.println("Search completed, returned "+results.length()
										+" results"+" and page rendered in:");

					myApp.timeStop();
					
					return outputs;
				}
				
			} 
			catch (JSONException e) {
				//problem here, json not received from server
				jsonException = true;
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				otherException = true;
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				otherException = true;
				e.printStackTrace();
			} 
			catch (Exception e) {
				//Anything else is assumed to be caused by a network failure
				otherException = true;
			}
			finally
			{
				router.waitForRequests(); //return the router to the waiting state
			}
			return null;
		}
		protected void onPostExecute(List<View> outputs)
		{
			if (jsonException)
			{
				jsonException = false;
				popupErrorDialog("JSON Exception", 
						"There might be a problem with JSON output " +
						"from server. Please try again.", ContactResultsPage.this, true);
			}
			else if (otherException)
			{
				otherException = false;
				popupErrorDialog("Cannot connect to server. ", 
						"Please try again later.", ContactResultsPage.this, true);
			}
			else 
			{
				for (int i = 0; i < outputs.size(); i++)
				{
					contentLayout.addView(outputs.get(i));
				}
				pDialog.dismiss();
			}
		}
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

}
