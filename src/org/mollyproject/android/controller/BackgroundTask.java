package org.mollyproject.android.controller;

import org.mollyproject.android.view.apps.Page;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;

public abstract class BackgroundTask<A, B, C> extends AsyncTask<A, B, C> {
	protected boolean jsonException = false;
	protected boolean unknownHostException = false;
	protected boolean ioException = false;
	protected boolean otherException = false;
	protected boolean malformedURLException = false;
	protected boolean nullPointerException = false;
	protected boolean parseException = false; 
	protected boolean toDestroyPageAfterFailure;
	protected Page page;
	protected ProgressDialog pDialog;
	protected boolean dialogEnabled;
	protected LayoutInflater inflater;
	protected MyApplication myApp;
	
	public BackgroundTask(Page page, boolean toDestroyPageAfterFailure, boolean dialogEnabled)
	{
		super();
		this.page = page;
		this.toDestroyPageAfterFailure = toDestroyPageAfterFailure;
		this.dialogEnabled = dialogEnabled;
		inflater = page.getLayoutInflater();
		myApp = (MyApplication) page.getApplication();
	}
	
	@Override
	protected void onPreExecute()
	{
		if (dialogEnabled)
		{
			pDialog = ProgressDialog.show(page, "", "Loading...", true, true);
		}
	}
	
	@Override
	protected void onPostExecute(C outputs)
	{
		if (outputs != null)
		{
			updateView(outputs);
		}
		
		if (jsonException)
		{
			jsonException = false;
			Page.popupErrorDialog("JSON Exception", 
					"There might be a problem with JSON output " +
					"from server. Please try again.", page, toDestroyPageAfterFailure);
		}
		else if (nullPointerException)
		{
			nullPointerException = false;
			Page.popupErrorDialog("Null Pointer Exception. Cannot connect to server. ", 
					"Please try again later.", page, toDestroyPageAfterFailure);
		}
		else if (malformedURLException)
		{
			malformedURLException = false;
			Page.popupErrorDialog("Malformed URL Exception. Cannot connect to server. ", 
					"Please try again later.", page, toDestroyPageAfterFailure);
		}
		else if (unknownHostException)
		{
			unknownHostException = false;
			Page.popupErrorDialog("Unknown host Exception. Cannot connect to server. ", 
					"Please try again later.", page, toDestroyPageAfterFailure);
		} 
		else if (ioException)
		{
			ioException = false;
			Page.popupErrorDialog("I/O Exception. Cannot connect to server. ", 
					"Please try again later.", page, toDestroyPageAfterFailure);
		} 
		else if (parseException)
		{
			parseException = false;
			Page.popupErrorDialog("Parse Exception. Bad input from server. ", 
					"Please try again later.", page, toDestroyPageAfterFailure);
		} 
		else if (otherException)
		{
			otherException = false;
			Page.popupErrorDialog("Cannot connect to server. ", 
					"Please try again later.", page, toDestroyPageAfterFailure);
		} 
		System.out.println("Dialog enabled "+dialogEnabled);
		if (dialogEnabled)
		{
			pDialog.dismiss();
		}
	}
	public abstract void updateView(C outputs);
}
