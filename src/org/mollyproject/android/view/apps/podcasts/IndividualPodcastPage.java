package org.mollyproject.android.view.apps.podcasts;

import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.text.Html;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IndividualPodcastPage extends ContentPage {
	protected String slug; 
	@Override
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		slug = myApp.getIndPodcastSlug();
	};
	
	@Override
	public void onResume() {
		super.onResume();
		if (!jsonProcessed)
		{
			new IndividualPodcastTask(this, true, true).execute();
		}
	}
	
	@Override
	public String getAdditionalParams() {
		return ("&arg="+slug);
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getName() {
		return MollyModule.INDIVIDUAL_PODCAST_PAGE;
	}

	@Override
	public String getQuery() {
		return null;
	}
	
}
