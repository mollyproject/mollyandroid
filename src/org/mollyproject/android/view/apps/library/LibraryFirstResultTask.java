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

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LibraryFirstResultTask extends LibraryResultsTask<LinearLayout, Void, List<View>>
{
	public LibraryFirstResultTask(LibraryResultsPage libraryResultsPage, boolean b)
	{
		super(libraryResultsPage,b);
	}
	@Override
	protected List<View> doInBackground(LinearLayout... arg0) {
		try {
			return connectAndGenerate(page,((MyApplication) page.getApplication())
					.getLibraryQuery()+"&page="+((LibraryResultsPage) page).getCurPageNum());
		} catch (JSONException e) {
			jsonException = true;
		}
		return null;
	}
	
	@Override
	public void updateView(List<View> outputs) {
		page.populateViews(outputs,((LibraryResultsPage) page).getContentLayout());
	}
	
	private List<View> connectAndGenerate(Page page, String queryWithPage) throws JSONException
	{
		JSONObject results = page.getRouter().exceptionHandledOnRequestSent(MollyModule.getName(page.getClass()),
				page, Router.OutputFormat.JSON, queryWithPage);
		
		return generatePage((LibraryResultsPage) page, results);
	}
	
	
	private List<View> generatePage(final LibraryResultsPage page, JSONObject results) throws JSONException
	{
		//for use in the very first time the page is populated
		List<View> outputs = new ArrayList<View>();
		final JSONObject jsonPage = results.getJSONObject("page");
		
		final ScrollView scr = new ScrollView(page);
		
		final LinearLayout pageLayout = new LinearLayout(page);
		pageLayout.setOrientation(LinearLayout.VERTICAL);
		pageLayout.setBackgroundResource(R.drawable.bg_white);
		final LinearLayout resultsLayout = new LinearLayout(page);
		resultsLayout.setOrientation(LinearLayout.VERTICAL);

		final TextView resultsNo = new TextView(page);
		resultsNo.setTextSize(16);
		resultsNo.setTextColor(R.color.black);
		resultsNo.setPadding(10, 20, 0, 20);
		resultsNo.setBackgroundResource(R.drawable.bg_white);
		
		populateResults(page,getNextResultsPage(page), resultsLayout, resultsNo);
		
		pageLayout.addView(resultsLayout);
		scr.addView(pageLayout);
		
		outputs.add(resultsNo);
		outputs.add(scr);
		
		Button nextButton = new Button(page);
		nextButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT, 1f));
		nextButton.setText("View more results");
		nextButton.setEnabled(false);
		
		LinearLayout bottomButtonsLayout = new LinearLayout(page);
		bottomButtonsLayout.addView(nextButton);
		
		pageLayout.addView(bottomButtonsLayout);
		
		if (jsonPage.getBoolean("has_next"))
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
