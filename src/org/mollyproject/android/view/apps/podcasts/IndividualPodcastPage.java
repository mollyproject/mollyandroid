package org.mollyproject.android.view.apps.podcasts;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

public class IndividualPodcastPage extends ContentPage {
	
	@Override
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	};
	
	@Override
	public void onResume() {
		super.onResume();
		new IndividualPodcastTask(this, true, true).execute(myApp.getIndPodcastSlug());
	}
	
	@Override
	public String getAdditionalParams() {
		return myApp.getIndPodcastSlug();
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getName() {
		return MollyModule.INDIVIDUAL_PODCAST_PAGE;
	}

}
