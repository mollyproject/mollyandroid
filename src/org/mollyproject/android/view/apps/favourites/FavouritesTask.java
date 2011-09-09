package org.mollyproject.android.view.apps.favourites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FavouritesTask extends BackgroundTask<JSONObject, Void, JSONObject>{

	public FavouritesTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			JSONArray favourites = jsonContent.getJSONArray("favourites");
			
			if (favourites.length() == 0)
			{
				
			}
			
			for (int i = 0; i < favourites.length(); i ++)
			{
				JSONObject favourite = favourites.getJSONObject(i);
				LinearLayout favouriteLayout = (LinearLayout) page.getLayoutInflater().inflate
						(R.layout.favourite_result, null);
				page.getContentLayout().addView(favouriteLayout);
				JSONObject metadata = favourite.getJSONObject("metadata");
				final JSONObject entity = metadata.getJSONObject("entity");
				//Assumption: this is a place
				
				TextView favouriteText = (TextView) favouriteLayout.findViewById(R.id.favouriteText);
				favouriteText.setText(Html.fromHtml(metadata.getString("title") + "<br/>" 
							+ metadata.getString("additional")));
				
				favouriteText.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try
						{
							MyApplication.placesArgs[0] = entity.getString("identity_scheme");
							MyApplication.placesArgs[1] = entity.getString("identity_value");
							Intent myIntent = new Intent(page.getApplicationContext(), 
									MyApplication.getPageClass(MollyModule.PLACES_PAGE));
							page.startActivityForResult(myIntent, 0);
						} catch (Exception e) {
							Toast.makeText(page.getApplicationContext(), 
									"Sorry this page is not currently available", Toast.LENGTH_SHORT);
						}
					}
				});
				
				TextView removeFav = (TextView) favouriteLayout.findViewById(R.id.removeFav);
				
			}
			
			
			((ContentPage) page).doneProcessingJSON();
		} catch (JSONException e) {
			jsonException = true;
			e.printStackTrace();
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
