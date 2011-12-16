package org.mollyproject.android.controller;

import org.json.JSONObject;
import org.mollyproject.android.view.apps.ContentPage;

/**
 * task used for processing JSON data fetched by a page and updating views on the page accordingly 
 * @author famanson
 *
 */
public abstract class JSONProcessingTask extends BackgroundTask<JSONObject, Void, JSONObject>{
	/**
	 * the constructor
	 * 
	 * @param page is specifically a ContentPage, i.e. this class should only be used for ContentPages
	 * @param toDestroyPageAfterFailure whether the page should be destroyed in case of failure
	 * @param dialogEnabled whether the dialog for the task is visible
	 * 
	 * */
	public JSONProcessingTask(ContentPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	/**
	 * the method that does the transition work, it takes the JSON data fetched by the page, passes it on to the
	 * methods responsible for updating the view or passes the null object on in case the data is invalid or there
	 * is no data at all
	 *  
	 * @param params an array of JSONObject, just dummy at this level, it might be used in a different way in subclasses
	 */
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		try {
			JSONObject newJSONContent = (MyApplication.router.requestJSON(page.getName(), page.getAdditionalParams(), page.getQuery()));
			((ContentPage) page).setJSONContent(newJSONContent);
			return newJSONContent;
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
		return null;
	}
}