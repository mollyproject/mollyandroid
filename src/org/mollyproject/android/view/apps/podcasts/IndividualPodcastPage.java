package org.mollyproject.android.view.apps.podcasts;

import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

public class IndividualPodcastPage extends ContentPage {

	@Override
	public String getAdditionalParams() {
		return myApp.getPodcastsSlug();
	}

	@Override
	public Page getInstance() {
		return this;
	}

}
