package org.mollyproject.android.view.apps.library;

import java.util.ArrayList;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.Page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LibraryFirstResultTask extends LibraryResultsTask<LinearLayout, Void, JSONObject>
{
	public LibraryFirstResultTask(LibraryResultsPage libraryResultsPage, boolean b)
	{
		super(libraryResultsPage,b);
	}
	@Override
	protected JSONObject doInBackground(LinearLayout... arg0) {
		try {
			return getResults(page,((MyApplication) page.getApplication())
					.getLibraryQuery()+"&page="+((LibraryResultsPage) page).getCurPageNum());
		} catch (JSONException e) {
			jsonException = true;
		}
		return null;
	}
	
	@Override
	public void updateView(JSONObject results) {
		try {
			generatePage((LibraryResultsPage) page, results);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//page.populateViews(outputs,((LibraryResultsPage) page).getContentLayout());
	}
	
	private JSONObject getResults(Page page, String queryWithPage) throws JSONException
	{
		return page.getRouter().exceptionHandledOnRequestSent(MollyModule.getName(page.getClass()), null,
				page, Router.OutputFormat.JSON, queryWithPage);
	}
	
	
	private List<View> generatePage(final LibraryResultsPage page, JSONObject results) throws JSONException
	{
		//for use in the very first time the page is populated
		
		LayoutInflater layoutInflater = (LayoutInflater) 
			page.getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout libraryResultsTemplate = (LinearLayout) layoutInflater
						.inflate(R.layout.library_results_template,page.getContentLayout(), false);
		page.getContentLayout().addView(libraryResultsTemplate);
		
		List<View> outputs = new ArrayList<View>();
		final JSONObject jsonPage = results.getJSONObject("page");
		final LinearLayout resultsLayout = (LinearLayout) page.findViewById(R.id.resultsLayout); 
		final TextView resultsNo = (TextView) page.findViewById(R.id.libraryResultsNo);
		
		populateResults(page,getNextResultsPage(page), resultsLayout, resultsNo);
		Button nextButton = (Button) page.findViewById(R.id.moreButton);
		
		nextButton.setEnabled(false);
		if (jsonPage.getBoolean("has_next") & jsonPage.getInt("num_pages") > 1)
		{
			//generate the next page dynamically
			nextButton.setEnabled(true);
			nextButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					new LibraryNextPageTask(page, resultsLayout, resultsNo, false).execute();
				}
			});
		}
		return outputs;
		
	}
	
}
