package org.mollyproject.android.view.apps.contact;

import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ContactPage extends ContentPage {
	public static String PHONE = "phone";
	public static String EMAIL = "email";
	public static String MEDIUM = "medium";
	protected LinearLayout contactSearchBar;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
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
	
	protected void searchContact(String query, String medium)
	{
		if (query.length() == 0)
		{
			popupErrorDialog("No query found", "Search cannot proceed. " +
					"Please enter a name into the search box.", this);
		}
		else
		{
			new ContactResultsTask(this,contactSearchBar,false,true).execute(query,medium);
		}
	}

	@Override
	public String getAdditionalParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page getInstance() {
		return this;
	}
}
