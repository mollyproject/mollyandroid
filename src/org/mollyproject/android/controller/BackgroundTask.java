package org.mollyproject.android.controller;

import org.mollyproject.android.view.apps.Page;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

public abstract class BackgroundTask<A, B, C> extends AsyncTask<A, B, C> {
	protected boolean jsonException = false;
	protected boolean unknownHostException = false;
	protected boolean ioException = false;
	protected boolean operationException = false;
	protected boolean otherException = false;
	protected boolean malformedURLException = false;
	protected boolean nullPointerException = false;
	protected boolean parseException = false; 
	protected boolean toDestroyPageAfterFailure;
	protected boolean destroyPlease;
	protected Page page;
	protected ProgressDialog pDialog;
	protected boolean dialogEnabled;
	protected MyApplication myApp;
	
	public BackgroundTask(Page page, boolean toDestroyPageAfterFailure, boolean dialogEnabled)
	{
		super();
		this.page = page;
		this.toDestroyPageAfterFailure = toDestroyPageAfterFailure;
		destroyPlease = false;
		this.dialogEnabled = dialogEnabled;
		myApp = (MyApplication) page.getApplication();
	}
	
	@Override
	protected void onPreExecute()
	{
		if (dialogEnabled)
		{
			pDialog = ProgressDialog.show(page, "", "Loading...", true, true);
			pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					cancel(true);
				}
			});
		}
	}
	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (toDestroyPageAfterFailure)
		{
			page.finish();
		}
		MyApplication.router.releaseConnection();
		try {
			MyApplication.router = new Router((MyApplication) page.getApplication());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Page.manualRefresh = false;
		page = null;
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
			destroyPlease = true;
			Toast.makeText(page.getApplicationContext(), "There might be a problem with JSON output " +
					"from server. Please try reloading the page later.", Toast.LENGTH_SHORT).show();
		}
		else if (nullPointerException)
		{
			nullPointerException = false;
			destroyPlease = true;
			Toast.makeText(page.getApplicationContext(), "Null Pointer Exception. Cannot connect to server. " + 
							"Please try reloading the page later.", Toast.LENGTH_SHORT).show();
		}
		else if (malformedURLException)
		{
			malformedURLException = false;
			destroyPlease = true;
			Toast.makeText(page.getApplicationContext(), "Null Pointer Exception. Cannot connect to server. " + 
					"Please try reloading the page later.", Toast.LENGTH_SHORT).show();
		}
		else if (unknownHostException)
		{
			unknownHostException = false;
			destroyPlease = true;
			Toast.makeText(page.getApplicationContext(), "Unknown host Exception. Cannot connect to server. " + 
					"Please try reloading the page later.", Toast.LENGTH_SHORT).show();
		} 
		else if (ioException)
		{
			ioException = false;
			destroyPlease = true;
			Toast.makeText(page.getApplicationContext(), "I/O Exception. Cannot connect to server. " +
					"Please try reloading the page later.", Toast.LENGTH_SHORT).show();
		} 
		else if (parseException)
		{
			parseException = false;
			destroyPlease = true;
			Toast.makeText(page.getApplicationContext(), "Parse Exception. Bad input from server. " + 
					"Please try reloading the page later.", Toast.LENGTH_SHORT).show();
		} else if (operationException)
		{
			operationException = false;
			destroyPlease = true;
			Toast.makeText(page.getApplicationContext(), "Operation cannot be complete. " +
					"Please check your connection and try again later.", Toast.LENGTH_SHORT).show();
		} else if (otherException)
		{
			otherException = false;
			destroyPlease = true;
			Toast.makeText(page.getApplicationContext(), "Error loading page. " +
					"Please try reloading the page later.", Toast.LENGTH_SHORT).show();
		}
		if (dialogEnabled)
		{
			pDialog.dismiss();
		}
		
		if (toDestroyPageAfterFailure & destroyPlease)
		{
			page.finish();
		}
		Page.manualRefresh = false;
	}
	public abstract void updateView(C outputs);
}
