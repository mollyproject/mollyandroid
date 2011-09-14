package org.mollyproject.android.view.apps.contact;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class ContactPage extends AbstractContactPage {
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		dialogOnSetup = true;
		name = MollyModule.CONTACT_PAGE;
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getQuery() {
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
	}
}
