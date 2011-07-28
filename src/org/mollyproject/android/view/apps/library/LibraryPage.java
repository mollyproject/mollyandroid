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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
		
		LinearLayout buttonsLayout = new LinearLayout(this);
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
		Button searchButton = new Button(this);
		searchButton.setText("Search");
		searchButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT, 1f));
		searchButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
					searchBook();
				} catch (UnsupportedEncodingException e) {
					//Something is wrong with the query
					e.printStackTrace();
				}
			}
			
		});
		Button resetButton = new Button(this);
		resetButton.setText("Reset");
		resetButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT, 1f));
		resetButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				titleField.setText("");
				authorField.setText("");
				isbnField.setText("");
			}
			
		});
		buttonsLayout.addView(searchButton);
		buttonsLayout.addView(resetButton);
		
		searchForm.addView(title);
		searchForm.addView(titleField);
		searchForm.addView(author);
		searchForm.addView(authorField);
		searchForm.addView(isbn);
		searchForm.addView(isbnField);
		
		contentLayout.addView(searchForm);
		contentLayout.addView(buttonsLayout);
	}

	private void searchBook() throws UnsupportedEncodingException
	{
		String query = new String();
		Set<String> keys = bookArgs.keySet();
		boolean empty = true;
		for (String key : keys)
		{
			if (bookArgs.get(key).length() > 0)
			{
				query = query+key+"="+URLEncoder.encode(bookArgs.get(key), "UTF-8")+"&";
				empty = false;
			}
		}
		if (!empty)
		{
			myApp.setLibraryQuery(query);
			Intent myIntent = new Intent (this, LibraryResultsPage.class);
			startActivityForResult(myIntent,0);
		}
		else
		{
			/*Toast toast = Toast.makeText(this, "Please enter some search criteria",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();*/
			popupErrorDialog("Cannot perform search", "Please enter some search criteria"
					, this);
		}
	}
	
	@Override
	public Page getInstance()
	{
		return this;
	}
}
