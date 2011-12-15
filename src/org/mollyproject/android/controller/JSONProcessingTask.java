package org.mollyproject.android.controller;

import org.json.JSONObject;
import org.mollyproject.android.view.apps.ContentPage;

public abstract class JSONProcessingTask extends BackgroundTask<JSONObject, Void, JSONObject>{
	//This class can only be used for ContentPages
	public JSONProcessingTask(ContentPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

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