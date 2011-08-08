package org.mollyproject.android.view.apps.contact;

import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import roboguice.inject.InjectView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ContactPage extends ContentPage {
	public static String PHONE = "phone";
	public static String EMAIL = "email";
	public static String MEDIUM = "medium";
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		LayoutInflater layoutInflater = (LayoutInflater) 
					myApp.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout contactSearchBar = (LinearLayout) layoutInflater
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
				searchContact(searchField.getText().toString(),EMAIL);
			}
		});
		
		Button phoneButton = (Button) findViewById(R.id.phoneButton);
		phoneButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				searchContact(searchField.getText().toString(),PHONE);
			}
		});
	}
	
	private void searchContact(String query, String medium)
	{
		if (query.length() == 0)
		{
			popupErrorDialog("No query found", "Search cannot proceed. " +
					"Please enter a name into the search box.", this);
		}
		else
		{
			new ContactSearchTask(this,false).execute(query,medium);
		}
	}
	
	public void setContactOutput(JSONObject contactOutput)
	{
		myApp.setContactOutput(contactOutput);
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

}
