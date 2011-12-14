package org.mollyproject.android.view.apps.favourites;

import java.util.List;

import org.json.JSONObject;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.view.apps.FavouriteOptionsTask;
import org.mollyproject.android.view.apps.Page;

public class UnFavTask extends BackgroundTask<JSONObject, Void, JSONObject>{

	public UnFavTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject outputs) {
		//assuming page is FavouritesPage, manually refresh it
		//((ContentPage) page).setJSONContent(outputs);
		Page.manualRefresh = true;
		page.refresh();
	}

	@Override
	protected JSONObject doInBackground(JSONObject... entities) {
		//post the favourite on to the web server
		try
		{
			JSONObject entity = entities[0];
			List<String> output = FavouriteOptionsTask.unfavourite(entity.getString("_url"));
			return new JSONObject(output.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			operationException = true;
		}
		return null;
	}
	
}
