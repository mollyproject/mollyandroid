package org.mollyproject.android.view.apps.contact;

import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class ContactResultsPage extends AbstractContactPage {
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
        // Start a new thread that will download all the data
        new ContactResultsTask(this, true).execute(contentLayout);
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getAdditionalParams() {
		// TODO Auto-generated method stub
		return null;
	}

}
