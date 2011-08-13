package org.mollyproject.android.view.apps.contact;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class ContactResultsPage extends AbstractContactPage {

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		new ContactResultsTask((AbstractContactPage) getInstance(),
				contactSearchBar,false,true).execute(myApp.getContactQuery());
	}
	
	@Override
	public String getAdditionalParams() {
		return null;
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getName() {
		return MollyModule.CONTACT_RESULTS_PAGE;
	}

}
