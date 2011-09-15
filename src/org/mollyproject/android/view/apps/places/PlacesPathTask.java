package org.mollyproject.android.view.apps.places;

import org.json.JSONObject;
import org.mollyproject.android.view.apps.ComplexMapResultTask;
import org.mollyproject.android.view.apps.ContentPage;


public class PlacesPathTask extends ComplexMapResultTask {

	public PlacesPathTask(ContentPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		super.updateView(jsonContent);
		
	}
}
