package org.mollyproject.android.view.apps.library;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.view.apps.Page;

import android.widget.LinearLayout;
import android.widget.TextView;

public class LibraryNextPageTask extends LibraryResultsTask<Void, Void, JSONObject>
{
	
	//A hack:
	protected LinearLayout resultsLayout;
	protected TextView resultsNo;
	public LibraryNextPageTask(LibraryResultsPage libraryResultsPage, LinearLayout resultsLayout, 
			TextView resultsNo, boolean toDestroy, boolean dialog)
	{
		super(libraryResultsPage,toDestroy, dialog);
		this.resultsLayout = resultsLayout;
		this.resultsNo = resultsNo;
	}
	
	@Override
	public void updateView(JSONObject nextJSONPage) {
		try {
			//Once a next page request is made, the query must have been fixed
			populateResults(page, nextJSONPage, resultsLayout, resultsNo);
		} catch (JSONException e) {
			//A special case of the updateView method
			e.printStackTrace();
			Page.popupErrorDialog("JSON Exception", 
					"There might be a problem with JSON output " +
					"from server. Please try again.", page, toDestroyPageAfterFailure);
		}
	}

	@Override
	protected JSONObject doInBackground(Void... arg0) {
		try {
			return getNextResultsPage(page);
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
		return null;
	}
	
	
	
}

