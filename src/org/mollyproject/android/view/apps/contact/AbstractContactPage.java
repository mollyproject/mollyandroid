package org.mollyproject.android.view.apps.contact;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public abstract class AbstractContactPage extends ContentPage {
	//to use a common search bar for both the contact:index page and contact:results_list page
	public static String PHONE = "phone";
	public static String EMAIL = "email";
	public static String MEDIUM = "medium";
	protected LinearLayout contactSearchBar;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		contactSearchBar = (LinearLayout) getLayoutInflater()
				.inflate(R.layout.contact_search_bar,null);
		contentLayout.addView(contactSearchBar);
		setUpContactSearchBar(this, contactSearchBar);
	}
	
	public static void setUpContactSearchBar(final Page page, LinearLayout contactSearchBar)
	{
		//search bar
		final EditText searchField = (EditText) contactSearchBar.findViewById(R.id.contactSearchField);
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
		                	AbstractContactPage.searchContact(page, 
		                			searchField.getText().toString(),AbstractContactPage.EMAIL);
		                    return true;
		                default:
		                    break;
		            }
		        }
				return false;
			}
		});
		
		Button emailButton = (Button) contactSearchBar.findViewById(R.id.emailButton);
		emailButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//search by email if button clicked
				AbstractContactPage.searchContact(page, 
						searchField.getText().toString(),AbstractContactPage.EMAIL);
			}
		});
		
		Button phoneButton = (Button) contactSearchBar.findViewById(R.id.phoneButton);
		phoneButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//search by phone if button clicked
				AbstractContactPage.searchContact(page, 
						searchField.getText().toString(),AbstractContactPage.PHONE);
			}
		});
	}
	
	public static void searchContact(Page page, String query, String medium)
	{
		if (query.length() == 0)
		{
			Toast.makeText(page, "No query found. Search cannot proceed. " +
					"Please enter a name into the search box.", Toast.LENGTH_SHORT).show();
		}
		else
		{
			//send query to myApp
			MyApplication.contactQuery[0] = query;
			MyApplication.contactQuery[1] = medium;
			Intent myIntent = new Intent(page ,MyApplication.getPageClass(MollyModule.CONTACT_RESULTS_PAGE));
			page.startActivityForResult(myIntent, 0);
		}
	}
}
