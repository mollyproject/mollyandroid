package org.mollyproject.android.view.apps.library;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class LibraryResultsPage extends AbstractLibraryPage {
	protected int curPageNum; //the current maximum page for a particular result displayed
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		curPageNum = 0;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		new LibraryFirstResultTask(this,false, true).execute();
	}
	
	public void increaseCurPageNum()
	{
		curPageNum++;
	}
	
	public int getCurPageNum()
	{
		return curPageNum;
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getAdditionalParams() {
		return null;
	}

	@Override
	public String getName() {
		return MollyModule.LIBRARY_RESULTS_PAGE;
	}

	@Override
	public String getQuery() {
		return null;
	}
}












