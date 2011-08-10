package org.mollyproject.android.view.apps.contact;

import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class ContactResultsPage extends AbstractContactPage {

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		new ContactResultsTask((AbstractContactPage) getInstance(),
							contactSearchBar,false,true).execute(myApp.getContactQuery());
	}
	
	@Override
	public String getAdditionalParams() {
		return null;
	}

	@Override
	public Page getInstance() {
		// TODO Auto-generated method stub
		return this;
	}

}
