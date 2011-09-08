package org.mollyproject.android.view.apps.library;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class AbstractLibraryPage extends ContentPage {
	//make use of the search bar for both lib page and lib results page
	public static String TITLE = "title";
	public static String AUTHOR = "author";
	public static String ISBN = "isbn";
	
	protected EditText isbnField;
	protected EditText authorField;
	protected EditText titleField;
	
	protected Map<String,String> bookArgs = new HashMap<String,String>(); //this is used to store the input in each text field
	protected Map<String,String> currentSearchArgs = new HashMap<String,String>();
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		bookArgs.put(TITLE, "");
		bookArgs.put(AUTHOR, "");
		bookArgs.put(ISBN, "");
		
		//inflate the searchbar
		LinearLayout librarySearchBar = (LinearLayout) getLayoutInflater()
					.inflate(R.layout.library_search_bar,null);
		
		if (MyApplication.libraryQuery != null)
		{
			//put the most recent search term into current search args
			Set<String> cachedArgs = MyApplication.libraryQuery.keySet();
			for (String key: cachedArgs)
			{
				currentSearchArgs.put(key, MyApplication.libraryQuery.get(key));
			}
		}
		
		//The next 3 lines are the special case for the page needing to change the text of these fields later
		titleField = (EditText) librarySearchBar.findViewById(R.id.titleField);
		authorField = (EditText) librarySearchBar.findViewById(R.id.authorField);
		isbnField = (EditText) librarySearchBar.findViewById(R.id.isbnField);
		
		contentLayout.addView(librarySearchBar);
		
		setUpLibrarySearchBar(this, bookArgs, librarySearchBar);
		
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.reload).setEnabled(false);
		return super.onPrepareOptionsMenu(menu);
	}
	
	public static void setUpLibrarySearchBar(final Page page, final Map<String,String> bookArgs, 
			final LinearLayout librarySearchBar)
	{
		
		bookArgs.put(TITLE, "");
		bookArgs.put(AUTHOR, "");
		bookArgs.put(ISBN, "");
		
		final EditText titleField = (EditText) librarySearchBar.findViewById(R.id.titleField);
		searchLibraryOnEnterKey(page, bookArgs, librarySearchBar, titleField, TITLE);
		
		final EditText authorField = (EditText) librarySearchBar.findViewById(R.id.authorField);
		searchLibraryOnEnterKey(page, bookArgs, librarySearchBar, authorField, AUTHOR);
		
		final EditText isbnField = (EditText) librarySearchBar.findViewById(R.id.isbnField);
		searchLibraryOnEnterKey(page, bookArgs, librarySearchBar, isbnField, ISBN);
		
		Button searchButton = (Button) librarySearchBar.findViewById(R.id.searchLibraryButton);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AbstractLibraryPage.searchLibraryOnClick(page, bookArgs, librarySearchBar);
			}
		});
		
		Button resetButton = (Button) librarySearchBar.findViewById(R.id.resetLibraryButton);
		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//clear out every field
				titleField.setText("");
				authorField.setText("");
				isbnField.setText("");
				bookArgs.clear();
			}
		});
	}
	
	@Override
	public void onResume() {
		System.out.println("current search args: " + currentSearchArgs);
		if (!currentSearchArgs.isEmpty())
		{
			titleField.setText(currentSearchArgs.get(TITLE));
			isbnField.setText(currentSearchArgs.get(ISBN));
			authorField.setText(currentSearchArgs.get(AUTHOR));
		}
		else
		{
			titleField.setText("");
			isbnField.setText("");
			authorField.setText("");
		}
		super.onResume();
	}
	
	public static void searchLibraryOnClick(final Page page, final Map<String,String> bookArgs, 
			final LinearLayout librarySearchBar)
	{
		try {
			//record all the search queries available and start the search
			searchBook(page, bookArgs, librarySearchBar);
		} catch (UnsupportedEncodingException e) {
			//Something is wrong with the query
			e.printStackTrace();
			
			Toast.makeText(page.getApplicationContext(), "Unsupported Encoding" +
					"There might be a problem with the search terms. " +
					"Please try again later.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public static void searchLibraryOnEnterKey(final Page page, final Map<String,String> bookArgs, 
			final LinearLayout librarySearchBar, final EditText inputField, final String argID)
	{
		//because the library results page will be displayed by a series of pages,
		//it makes it far easier to do the actual searching on the LibraryResultPage:
		//Each search request would only return one page so it is easier to get each
		//result page on demand then concatenate the page found at the end of the
		//current result rather than find a thousand pages in one go in the main page
		//and load them all on to the result page
		inputField.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                	
		                case KeyEvent.KEYCODE_ENTER:
		                	//.trim() won't make a difference in searching but does in caching
		                	//it should reduce the amount of unecessary cache
		                	System.out.println("text field content: " + inputField.getText());
		                	bookArgs.put(argID, inputField.getText().toString().trim());
						try {
							searchBook(page, bookArgs, librarySearchBar);
						} catch (UnsupportedEncodingException e) {
							//Something is wrong with the query
							e.printStackTrace();
							Toast.makeText(page.getApplicationContext(), "Unsupported Encoding" +
									"There might be a problem with the search terms. " +
									"Please try again later.", Toast.LENGTH_SHORT).show();
						}
		                    return true;
		                default:
		                    break;
		            }
		        }
				return false;
			}
		});
	}

	public static void searchBook(Page page, Map<String,String> bookArgs, LinearLayout librarySearchBar) throws UnsupportedEncodingException
	{
		bookArgs.put(TITLE, ((TextView) librarySearchBar.findViewById(R.id.titleField)).getText().toString().trim());
		bookArgs.put(AUTHOR, ((TextView) librarySearchBar.findViewById(R.id.authorField)).getText().toString().trim());
		bookArgs.put(ISBN, ((TextView) librarySearchBar.findViewById(R.id.isbnField)).getText().toString().trim());
		
		Set<String> keys = bookArgs.keySet();
		boolean empty = true;
		for (String key : keys)
		{
			System.out.println(key+ ": " + bookArgs.get(key) + " " + bookArgs.get(key).length());
			if (bookArgs.get(key).length() > 0)
			{
				bookArgs.put(key, URLEncoder.encode(bookArgs.get(key), "UTF-8"));
				empty = false;
			}
		}
		if (!empty)
		{
			MyApplication.libraryQuery = bookArgs;
			Intent myIntent = new Intent (page.getApplicationContext(), MyApplication.getPageClass(MollyModule.LIBRARY_RESULTS_PAGE));
			page.startActivityForResult(myIntent,0);
		}
		else
		{
			Toast.makeText(page.getApplicationContext(), "Cannot perform search. "
					+"Please enter some search criteria", Toast.LENGTH_SHORT).show();
		}
	}
}
