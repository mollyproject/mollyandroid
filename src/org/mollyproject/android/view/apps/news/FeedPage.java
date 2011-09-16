package org.mollyproject.android.view.apps.news;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class FeedPage extends ContentPage {
	protected String slug;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		slug = MyApplication.feedSlug;
		additionalArgs = "&arg=" + slug;
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
		// TODO Auto-generated method stub
		
	}

}
