package org.mollyproject.android.view.pages;

import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//search bar
		LinearLayout searchBar = new LinearLayout(this);
		searchBar.setOrientation(LinearLayout.VERTICAL);
		//where the search phrase is typed in
		final EditText searchField = new EditText(this);
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
		
		LinearLayout searchButtons = new LinearLayout(this);
		searchButtons.setOrientation(LinearLayout.HORIZONTAL);
		
		Button emailButton = new Button(this);
		emailButton.setText("E-mail");
		emailButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT, 1f));
		emailButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				searchContact(searchField.getText().toString(),EMAIL);
			}
		});
		
		Button phoneButton = new Button(this);
		phoneButton.setText("Phone");
		phoneButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT, 1f));
		phoneButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				searchContact(searchField.getText().toString(),PHONE);
			}
			
		});
		
		searchButtons.addView(emailButton,ViewGroup.LayoutParams.FILL_PARENT);
		searchButtons.addView(phoneButton,ViewGroup.LayoutParams.FILL_PARENT);
		
		searchBar.addView(searchField);
		searchBar.addView(searchButtons);
		
		contentLayout.addView(searchBar);
		//setContentView(R.layout.contacts);
	}
	
	private void searchContact(String query, String medium)
	{
		String jsonOutput = new String();
		try {
			jsonOutput = router.onRequestSent(SelectionManager.CONTACT_RESULTS_PAGE,
									Router.QUERY,query.replace(' ', '+')+"&medium="+medium+"&format=json");
			myApp.setContactOutput(jsonOutput);
			Intent myIntent = new Intent (this, ContactResultsPage.class);
			startActivityForResult(myIntent,0);
		} catch (Exception e) {
			myApp.setContactOutput("Error");
		}
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

}
