package org.mollyproject.android.view.apps.library;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.view.apps.Page;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LibraryNextPageTask extends AbstractLibraryResultsTask<Void, Void, JSONObject>
{
	
	//A hack, because resultsNo and resultsLayout cannot be passed along with JSONObject in the
	//same argument, just inject them right here in the constructor:
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
			jsonException = true;
			Toast.makeText(page.getApplicationContext(), "JSON Exception. " +
					"There might be a problem with JSON output " +
					"from server. Please try again later.", Toast.LENGTH_SHORT).show();
			
			/*Page.popupErrorDialog("JSON Exception", 
					"There might be a problem with JSON output " +
					"from server. Please try again.", page, toDestroyPageAfterFailure);*/
		}
	}

	@Override
	protected JSONObject doInBackground(Void... arg0) {
		try {
			return getNextResultsPage(page);
		} catch (JSONException e) {
			e.printStackTrace();
			((LibraryResultsPage) page).decreaseCurPageNum();
			jsonException = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			((LibraryResultsPage) page).decreaseCurPageNum();
			unknownHostException = true;
		} catch (IOException e) {
			e.printStackTrace();
			((LibraryResultsPage) page).decreaseCurPageNum();
			ioException = true;
		} catch (ParseException e) {
			e.printStackTrace();
			parseException = true;
		}
		return null;
	}
	
	
	
}

