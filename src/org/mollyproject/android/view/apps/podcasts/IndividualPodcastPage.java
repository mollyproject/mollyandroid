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
		name =  MollyModule.INDIVIDUAL_PODCAST_PAGE;
		slug = MyApplication.indPodcastSlug;
		additionalArgs = "&arg="+slug;
		System.out.println("TEST: " + additionalArgs);
	};
	
	@Override
	public void refresh() {
		slug = "TEST";
		System.out.println(additionalArgs);
		new IndividualPodcastTask(this, false, true).execute();
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getQuery() {
		return null;
	}
	
}
