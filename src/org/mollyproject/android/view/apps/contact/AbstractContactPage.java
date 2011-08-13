package org.mollyproject.android.view.apps.contact;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public abstract class AbstractContactPage extends ContentPage {
	//to use a common search bar for both the contact:index page and contact:results_list page
	public static String PHONE = "phone";
	public static String EMAIL = "email";
	public static String MEDIUM = "medium";
	protected LinearLayout contactSearchBar;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//inflate the search bar and put it right on top
		contactSearchBar = (LinearLayout) layoutInflater
					.inflate(R.layout.contact_search_bar,contentLayout, false);
		contentLayout.addView(contactSearchBar);
		
		//search bar
		final EditText searchField = (EditText) findViewById(R.id.contactSearchField);
		searchField.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		            //search for contact with medium = email by default if enter key is pressed
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                case KeyEvent.KEYCODE_ENTER:
		                	searchContact(searchField.getText().toString(),EMAIL);
		                    return true;
		                default:
		                    break;
		            }
		        }
				return false;
			}
		});
		
		Button emailButton = (Button) findViewById(R.id.emailButton);
		emailButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//search by email if button clicked
				searchContact(searchField.getText().toString(),EMAIL);
			}
		});
		
		Button phoneButton = (Button) findViewById(R.id.phoneButton);
		phoneButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//search by phone if button clicked
				searchContact(searchField.getText().toString(),PHONE);
			}
		});
	}
	
	protected void searchContact(String query, String medium)
	{
		if (query.length() == 0)
		{
			popupErrorDialog("No query found", "Search cannot proceed. " +
					"Please enter a name into the search box.", this);
		}
		else
		{
			//send query to myApp
			myApp.setContactOutput(query,medium);
			Intent myIntent = new Intent(this,myApp.getPageClass(MollyModule.CONTACT_RESULTS_PAGE));
			startActivityForResult(myIntent, 0);
		}
	}
}
