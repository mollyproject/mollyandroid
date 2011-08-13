package org.mollyproject.android.view.apps.contact;

import org.mollyproject.android.controller.BackgroundTask;

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
