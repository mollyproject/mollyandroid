package org.mollyproject.android.view.apps.favourites;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.app.Dialog;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.LinearLayout;

public class FavouritesPage extends ContentPage {
	protected LinearLayout selectedFav;
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
	public String getAdditionalParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return MollyModule.FAVOURITES;
	}

	@Override
	public void refresh() {
		new FavouritesTask(this, true, true).execute();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.favourite_context_menu, menu);
		selectedFav = (LinearLayout) v;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		switch (item.getItemId())
		{
			case R.id.linkToFav:
				//Go to the favourite
				break;
			case R.id.setLocation:
				//Set the favourite as current location
				break;
			case R.id.removeFav:
				//Remove the selected favourite
				break;
			
		}
		
		return super.onContextItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.manage_favourites).setVisible(false);
		return super.onPrepareOptionsMenu(menu);
	}
}
