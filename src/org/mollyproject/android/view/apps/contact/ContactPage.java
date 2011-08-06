package org.mollyproject.android.view.apps.contact;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.app.ProgressDialog;
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
	public static String MEDIUM = "medium";
	
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
		new ContactSearchTask(this,false).execute(query,medium);
	}
	
	private class ContactSearchTask extends BackgroundTask<String, Void, Void>
	{

		public ContactSearchTask(Page page, boolean b) {
			super(page, b);
		}

		@Override
		public void updateView(Void outputs) {
		}

		@Override
		protected Void doInBackground(String... args) {
			//args = { query, medium }
			try {
				String searchQuery = "query="+URLEncoder.encode(args[0],"UTF-8")+"&medium="+args[1];
				myApp.setContactOutput(router.onRequestSent(MollyModule
						.getName(ContactResultsPage.class),
						Router.OutputFormat.JSON, searchQuery));
				Intent myIntent = new Intent (page, router.getDestination());
				myApp.timeStart();
				startActivityForResult(myIntent,0);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				unknownHostException = true;
			} catch (JSONException e) {
				e.printStackTrace();
				jsonException = true;
			} catch (IOException e) {
				e.printStackTrace();
				ioException = true;
			} finally {
				router.waitForRequests();
			}
			return null;
		}
		
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

}
