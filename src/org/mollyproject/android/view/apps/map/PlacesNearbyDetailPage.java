package org.mollyproject.android.view.apps.map;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;

import android.os.Bundle;
import android.widget.LinearLayout;

public class PlacesNearbyDetailPage extends PageWithMap{
	protected String slug;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.PLACES_NEARBY_DETAIL;
		slug = MyApplication.placesNearbySlug;
		additionalArgs = "&arg=" + slug;
		contentLayout.setBackgroundResource(R.drawable.shape_white);
		mapView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
        		getWindowManager().getDefaultDisplay().getHeight()/3));
	}
	
	@Override
	public void refresh() {
		new PlacesNearbyDetailTask(this, false, true).execute();
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

}
