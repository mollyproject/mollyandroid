package org.mollyproject.android.view.apps.map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ComplexMapResultTask;
import org.mollyproject.android.view.apps.PageWithMap;

import android.widget.LinearLayout;

public class PlacesNearbyMapTask extends ComplexMapResultTask {

	public PlacesNearbyMapTask(PageWithMap page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}
	
	public LinearLayout parseNearbyEntity()
	{
		return null;
	}
	
	@Override
	public void updateView(JSONObject jsonContent) {
		super.updateView(jsonContent);
		try {
			if (!exceptionCaught)
			{
				JSONArray entities = jsonContent.getJSONArray("entities");
				for (int i = 0; i < entities.length(); i++)
				{
					JSONObject entity = entities.getJSONArray(i).getJSONObject(0);
					LinearLayout nearbyResultLayout = (LinearLayout) page.getLayoutInflater()
								.inflate(R.layout.plain_text_search_result, null);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
