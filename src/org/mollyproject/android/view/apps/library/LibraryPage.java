package org.mollyproject.android.view.apps.library;

import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.UnimplementedPage;

import android.os.Bundle;

public class LibraryPage extends UnimplementedPage {
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public Page getInstance()
	{
		return this;
	}
}
