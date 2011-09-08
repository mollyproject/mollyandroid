package org.mollyproject.android.controller;

import org.json.JSONObject;
import org.mollyproject.android.view.apps.Page;

public abstract class JSONProcessingTask extends BackgroundTask<JSONObject, Void, JSONObject>{
	
	public JSONProcessingTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		try {
			return (MyApplication.router.onRequestSent(page.getName(), page.getAdditionalParams(), Router.OutputFormat.JSON, page.getQuery()));
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
		return null;
	}
}