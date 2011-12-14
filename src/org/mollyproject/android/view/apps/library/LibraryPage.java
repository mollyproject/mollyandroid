package org.mollyproject.android.view.apps.library;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class LibraryPage extends AbstractLibraryPage {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name =  MollyModule.LIBRARY_PAGE;
		dialogOnSetup = true;
	}
	
	@Override
	public void onResume() {
		currentSearchArgs.clear();
		bookArgs.clear();
		super.onResume();
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
