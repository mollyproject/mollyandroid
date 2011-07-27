package org.mollyproject.android.view.apps.library;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.UnimplementedPage;
import org.mollyproject.android.view.apps.contact.ContactResultsPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LibraryPage extends ContentPage {
	public static String TITLE = "title";
	public static String AUTHOR = "author";
	public static String ISBN = "isbn";
	
	protected Map<String,String> bookArgs = new HashMap<String,String>();
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LinearLayout searchForm = new LinearLayout(this);
		searchForm.setOrientation(LinearLayout.VERTICAL);
		
		//title field
		TextView title = new TextView(this);
		title.setText("Title");
		final EditText titleField = new EditText(this);
		bookArgs.put(TITLE, titleField.getText().toString());
		titleField.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                	
		                case KeyEvent.KEYCODE_ENTER:
		                	bookArgs.put(TITLE, titleField.getText().toString());
						try {
							searchBook();
						} catch (UnsupportedEncodingException e) {
							//Something is wrong with the query
							e.printStackTrace();
						}
		                    return true;
		                default:
		                    break;
		            }
		        }
				return false;
			}
		});
		
		TextView author = new TextView(this);
		author.setText("Author");
		final EditText authorField = new EditText(this);
		authorField.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                	
		                case KeyEvent.KEYCODE_ENTER:
		                	bookArgs.put(AUTHOR, authorField.getText().toString());
						try {
							searchBook();
						} catch (UnsupportedEncodingException e) {
							//Something is wrong with the query
							e.printStackTrace();
						}
		                    return true;
		                default:
		                    break;
		            }
		        }
				return false;
			}
		});
		
		TextView isbn = new TextView(this);
		isbn.setText("ISBN");
		final EditText isbnField = new EditText(this);
		isbnField.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                	
		                case KeyEvent.KEYCODE_ENTER:
		                	bookArgs.put(ISBN, isbnField.getText().toString());
							try {
								searchBook();
							} catch (UnsupportedEncodingException e) {
								//Something is wrong with the query
								e.printStackTrace();
							}
		                    return true;
		                default:
		                    break;
		            }
		        }
				return false;
			}
		});
		searchForm.addView(title);
		searchForm.addView(titleField);
		searchForm.addView(author);
		searchForm.addView(authorField);
		searchForm.addView(isbn);
		searchForm.addView(isbnField);
		
		contentLayout.addView(searchForm);
	}

	private void searchBook() throws UnsupportedEncodingException
	{
		String jsonOutput = new String();
		String query = new String();
		Set<String> keys = bookArgs.keySet();
		for (String key : keys)
		{
			if (bookArgs.get(key).length() > 0)
			{
				query = query+key+"="+URLEncoder.encode(bookArgs.get(key), "UTF-8")+"&";
			}
		}
		try {
			jsonOutput = router.onRequestSent(SelectionManager.LIBRARY_RESULTS_PAGE
					, Router.JSON, query);
			System.out.println(jsonOutput);
			myApp.setLibraryOutput(jsonOutput);
			
			Intent myIntent = new Intent (this, LibraryResultsPage.class);
			startActivityForResult(myIntent,0);
		} catch (Exception e) {
			myApp.setLibraryOutput("Error");
		}
		
	}
	
	@Override
	public Page getInstance()
	{
		return this;
	}
}
