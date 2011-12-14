package org.mollyproject.android.view.apps.weblearn.announcement;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class WebLearnAnnouncementPage extends ContentPage {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.WEBLEARN_ANNOUNCEMENT;
		additionalArgs = "&arg=" + MyApplication.weblearnAnnouncementSlug;
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
		new WebLearnAnnouncementTask(this, false, true).execute();
	}

}
