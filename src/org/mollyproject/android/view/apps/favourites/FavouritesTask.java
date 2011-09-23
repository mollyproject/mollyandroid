package org.mollyproject.android.view.apps.favourites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FavouritesTask extends JSONProcessingTask {

	public FavouritesTask(ContentPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			page.getContentLayout().removeAllViews(); //clear the layout
			
			JSONArray favourites = jsonContent.getJSONArray("favourites");
			if (favourites.length() == 0)
			{
				//there is no favourite
				LinearLayout favouriteLayout = (LinearLayout) page.getLayoutInflater().inflate
						(R.layout.favourite_result, null);
				page.getContentLayout().addView(favouriteLayout);
				TextView favouriteText = (TextView) favouriteLayout.findViewById(R.id.favouriteText);
				favouriteText.setText("You haven't favourited anything yet!");
			}
			for (int i = 0; i < favourites.length(); i ++)
			{
				JSONObject favourite = favourites.getJSONObject(i);
				LinearLayout favouriteLayout = (LinearLayout) page.getLayoutInflater().inflate
						(R.layout.favourite_result, null);
				favouriteLayout.setLayoutParams(Page.paramsWithLine);
				page.getContentLayout().addView(favouriteLayout);
				page.registerForContextMenu(favouriteLayout);
				
				JSONObject metadata = favourite.getJSONObject("metadata");
				final JSONObject entity = metadata.getJSONObject("entity");
				//Assumption: this is a place
				final int id = i;
				favouriteLayout.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						//record the view last touched for checking for the right context menu content
						FavouritesPage.lastTouchedFav = id;
						return false;
					}
				});
				
				favouriteLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try
						{
							MyApplication.placesArgs[0] = entity.getString("identifier_scheme");
							MyApplication.placesArgs[1] = entity.getString("identifier_value");
							Intent myIntent = new Intent(page.getApplicationContext(), 
									MyApplication.getPageClass(MollyModule.PLACES_ENTITY));
							page.startActivityForResult(myIntent, 0);
						} catch (Exception e) {
							Toast.makeText(page, 
									"Sorry this page is not currently available", Toast.LENGTH_SHORT);
						}
					}
				});
				
				TextView favouriteText = (TextView) favouriteLayout.findViewById(R.id.favouriteText);
				favouriteText.setText(Html.fromHtml((i + 1) + ". " + metadata.getString("title") + "<br/>" 
							+ metadata.getString("additional")));
				
				ImageButton removeFav = (ImageButton) favouriteLayout.findViewById(R.id.removeFav);
				
			}
			
			
			((ContentPage) page).doneProcessingJSON();
		} catch (JSONException e) {
			jsonException = true;
			e.printStackTrace();
		}
	}

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		if (Page.manualRefresh)
		{
			return super.doInBackground();
		}
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
