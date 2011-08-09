package org.mollyproject.android.view.apps.contact;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import android.content.Intent;

public class ContactSearchTask extends BackgroundTask<String, Void, Void>
{

	public ContactSearchTask(ContactPage contactPage, boolean toDestroy, boolean dialog) {
		super(contactPage, toDestroy, dialog);
	}

	@Override
	public void updateView(Void outputs) {
	}

	@Override
	protected Void doInBackground(String... args) {
		//args = { query, medium }
		
		return null;
	}
}
