package org.mollyproject.android.view.apps.map;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlacesNearbyTask extends BackgroundTask<JSONObject, Void, JSONObject>{

	public PlacesNearbyTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}
	
	public static LinearLayout parsePlaceEntity(JSONObject entity, Page page) throws JSONException
	{
		LinearLayout resultLayout = (LinearLayout) page.getLayoutInflater().inflate
				(R.layout.plain_text_search_result, null);
		TextView result = (TextView) resultLayout.findViewById(R.id.plainTextResultText);
		String resultText = new String();
		if (entity.getInt("entities_found") > 1)
		{
			resultText = resultText + entity.getString("verbose_name_plural");
		}
		else
		{
			resultText = resultText + entity.getString("verbose_name_singular");
		}
		
		resultText = resultText + " (" + entity.getString("entities_found") + " within " 
						+ entity.getString("max_distance") + ")";
		result.setText(resultText);
		result.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		return resultLayout;
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			JSONObject entityTypes = jsonContent.getJSONObject("entity_types");
			Iterator<String> entityTypeKeys = entityTypes.keys();
			
			while (entityTypeKeys.hasNext())
			{
				String key = entityTypeKeys.next();
				JSONArray entityType = entityTypes.getJSONArray(key);
				if (entityType.length() > 0)
				{
					//start parsing entities
					for (int i = 0; i < entityType.length(); i++)
					{
						JSONObject entity = entityType.getJSONObject(i);
						LinearLayout entityLayout = parsePlaceEntity(entity, page);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
	}

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ((ContentPage) page).getJSONContent();
	}

}
