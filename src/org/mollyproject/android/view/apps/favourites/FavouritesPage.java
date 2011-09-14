package org.mollyproject.android.view.apps.favourites;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FavouritesPage extends ContentPage {
	protected JSONObject metadata;
	public static int lastTouchedFav;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.FAVOURITES;
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getQuery() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		new FavouritesTask(this, false, true).execute();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		try {
			super.onCreateContextMenu(menu, v, menuInfo);
			MenuInflater menuInflater = getMenuInflater();
			menuInflater.inflate(R.menu.favourite_context_menu, menu);
			//try loading the metadata again every time the context menu is called
			metadata = jsonContent.getJSONArray("favourites").getJSONObject(lastTouchedFav).getJSONObject("metadata");
			menu.setHeaderTitle(metadata.getString("title"));
			//selectedFav = (LinearLayout) v;
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), 
					"Sorry this operation is not currently available", Toast.LENGTH_SHORT);
		};
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		try
		{
			//it is safe to use jsonContent because favourites cannot be added in this page, they can only be removed
			//also, the jsonContent will be re-updated every time a change is made to the favrouites list
			
			//this method will only be called once the context menu has been brought up, i.e if the metadata fails it fails
			//in the last step
			final JSONObject entity = metadata.getJSONObject("entity");
			switch (item.getItemId())
			{
				case R.id.linkToFav:
					//Go to the favourite
					MyApplication.placesArgs[0] = entity.getString("identifier_scheme");
					MyApplication.placesArgs[1] = entity.getString("identifier_value");
					Intent myIntent = new Intent(getApplicationContext(), 
							MyApplication.getPageClass(MollyModule.PLACES_ENTITY));
					startActivityForResult(myIntent, 0);
					break;
				case R.id.setLocation:
					//Set the favourite as current location
					new FavouriteLocationTask(this, false, true).execute(entity);
					//MyApplication.router.updateLocationManually(entity.getString("title"), entity.getJSONArray("location").getDouble(0), 
					//		entity.getJSONArray("location").getDouble(1), entity.getDouble("accuracy"));
					break;
				case R.id.removeFav:
					//Remove the selected favourite, it should receive the entity to be unfavourited
					new UnFavTask(this, false, true).execute(entity);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), 
					"Sorry this operation is not currently available", Toast.LENGTH_SHORT);
		}
		
		return super.onContextItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.manage_favourites).setVisible(false);
		return super.onPrepareOptionsMenu(menu);
	}
}
