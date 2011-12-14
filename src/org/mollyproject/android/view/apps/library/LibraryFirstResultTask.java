package org.mollyproject.android.view.apps.library;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ContentPage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LibraryFirstResultTask extends AbstractLibraryResultsTask<LinearLayout, Void, JSONObject>
{
	public LibraryFirstResultTask(LibraryResultsPage libraryResultsPage, boolean toDestroy, boolean dialog)
	{
		super(libraryResultsPage,toDestroy, dialog);
	}
	@Override
	protected JSONObject doInBackground(LinearLayout... arg0) {
		/*try {
			//just download the results and pass the json response on
			return getResults(page,query+"&page="+((LibraryResultsPage) page).getCurPageNum());
		} catch (JSONException e) {
			jsonException = true;
		}
		return null;*/
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ((ContentPage) page).getJSONContent();
	}
	
	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			//generate the page with the given json response
			generatePage((LibraryResultsPage) page, jsonContent);
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			unknownHostException = true;
		} catch (IOException e) {
			e.printStackTrace();
			ioException = true;
		}
	}
	
	private List<View> generatePage(final LibraryResultsPage page, JSONObject jsonContent) throws JSONException, UnknownHostException, IOException
	{
		//for use in the very first time the page is populated
		
		LayoutInflater layoutInflater = page.getLayoutInflater();
		LinearLayout libraryResultsTemplate = (LinearLayout) layoutInflater
						.inflate(R.layout.library_results_template,null);
		page.getContentLayout().addView(libraryResultsTemplate);
		
		List<View> outputs = new ArrayList<View>();
		final JSONObject jsonPage = jsonContent.getJSONObject("page");
		final LinearLayout resultsLayout = (LinearLayout) libraryResultsTemplate.findViewById(R.id.resultsLayout); 
		final TextView resultsNo = (TextView) libraryResultsTemplate.findViewById(R.id.libraryResultsNo);
		
		populateResults(page, jsonPage, resultsLayout, resultsNo);
		Button nextButton = (Button) libraryResultsTemplate.findViewById(R.id.moreButton);
		
		nextButton.setEnabled(false);
		if (jsonPage.getBoolean("has_next") & jsonPage.getInt("num_pages") > 1)
		{
			//generate the next page dynamically
			nextButton.setEnabled(true);
			nextButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					new LibraryNextPageTask(page, resultsLayout, resultsNo, false, true).execute();
				}
			});
		}
		return outputs;
		
	}
	
}
