package org.mollyproject.android.view.apps.places.entity;

import org.json.JSONObject;
import org.mollyproject.android.view.apps.ComplexMapResultTask;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.PageWithMap;

public class PlacesEntityDirectionsTask extends ComplexMapResultTask {

	public PlacesEntityDirectionsTask(ContentPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void updateView(JSONObject jsonContent) {
		super.updateView(jsonContent);
		
		if (!exceptionCaught)
		{
			try
			{
				((PageWithMap) page).drawPath(jsonContent.getJSONObject("route").getJSONArray("path"));
			} catch (Exception e) {
				e.printStackTrace();
				otherException = true;
			}
			
		}
	}
}
