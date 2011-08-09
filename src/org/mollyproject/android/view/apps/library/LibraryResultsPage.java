package org.mollyproject.android.view.apps.library;

import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class LibraryResultsPage extends AbstractLibraryPage {
	protected int curPageNum;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		curPageNum = 0;
		new LibraryFirstResultTask(this,false).execute();
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
		// TODO Auto-generated method stub
		return null;
	}
}












