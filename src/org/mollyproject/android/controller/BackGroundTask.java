package org.mollyproject.android.controller;

import org.mollyproject.android.view.apps.Page;

import android.os.AsyncTask;

public abstract class BackGroundTask<A, B, C> extends AsyncTask<A, B, C> {
	protected boolean jsonException = false;
	protected boolean unknownHostException = false;
	protected boolean ioException = false;
	protected boolean otherException = false;
	protected Page page;
	
	public BackGroundTask(Page page)
	{
		super();
		this.page = page;
	}
	
	public BackGroundTask()
	{
		super();
	}
	
	@Override
	protected void onPostExecute(C outputs)
	{
		if (jsonException)
		{
			jsonException = false;
			Page.popupErrorDialog("JSON Exception", 
					"There might be a problem with JSON output " +
					"from server. Please try again.", page, true);
		}
		
		else if (unknownHostException)
		{
			unknownHostException = false;
			Page.popupErrorDialog("Unknown host Exception. Cannot connect to server. ", 
					"Please try again later.", page, true);
		} 
		
		else if (ioException)
		{
			ioException = false;
			Page.popupErrorDialog("I/O Exception. Cannot connect to server. ", 
					"Please try again later.", page, true);
		} 
		
		else if (otherException)
		{
			otherException = false;
			Page.popupErrorDialog("Cannot connect to server. ", 
					"Please try again later.", page, true);
		} 
		else
		{
			updateView(outputs);
		}
	}
	public abstract void updateView(C outputs);
}
