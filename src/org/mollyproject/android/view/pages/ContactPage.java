package org.mollyproject.android.view.pages;

import android.os.Bundle;

public class ContactPage extends UnimplementedPage {

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.contacts);
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

}
