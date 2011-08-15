package org.mollyproject.android.view.apps.contact;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class ContactPage extends AbstractContactPage {
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public String getAdditionalParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getName() {
		return MollyModule.CONTACT_PAGE;
	}
}
