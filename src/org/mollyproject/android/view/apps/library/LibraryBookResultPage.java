package org.mollyproject.android.view.apps.library;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.PageWithMap;

import android.os.Bundle;

public class LibraryBookResultPage extends PageWithMap{
	protected String controlNumber;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controlNumber = MyApplication.bookControlNumber;
	}
	
	@Override
	public void refresh() {
		new LibraryBookResultTask(this, true, true).execute();
	}
	
	@Override
	public String getQuery() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAdditionalParams() {
		return "&arg="+controlNumber;
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getName() {
		return MollyModule.LIBRARY_BOOK_RESULT_PAGE;
	}

}
