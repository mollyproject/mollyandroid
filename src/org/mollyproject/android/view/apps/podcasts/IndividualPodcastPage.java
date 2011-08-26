package org.mollyproject.android.view.apps.podcasts;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

public class IndividualPodcastPage extends ContentPage {
	protected String slug; 
	@Override
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		slug = MyApplication.indPodcastSlug;
	};
	
	@Override
	public void onResume() {
		super.onResume();
		if (!jsonProcessed|| manualRefresh)
		{
			manualRefresh = false;
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
