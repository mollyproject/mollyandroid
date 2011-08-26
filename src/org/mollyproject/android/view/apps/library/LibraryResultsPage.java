package org.mollyproject.android.view.apps.library;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class LibraryResultsPage extends AbstractLibraryPage {
	protected int curPageNum; //the current maximum page for a particular result displayed
	protected String query;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		bookArgs = MyApplication.libraryQuery;
		curPageNum = 1;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (!jsonProcessed|| manualRefresh)
		{
			manualRefresh = false;
			new LibraryFirstResultTask(this,false, true).execute();
		}
	}
	
	public void increaseCurPageNum()
	{
		curPageNum++;
	}
	
	public void decreaseCurPageNum()
	{
		curPageNum--;
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
		query = new String();
		for (String key : bookArgs.keySet())
		{
			if (bookArgs.get(key).length() > 0)
			{
				query = query + "&" + key + "=" + bookArgs.get(key);
			}
		}
		System.out.println("Lib query" + query);
		return query;
	}
}












