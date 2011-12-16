package org.mollyproject.android.controller;

import org.mollyproject.android.view.apps.Page;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;
/**
 * the class that is behind the background tasks used in Molly Android,
 * on this (abstract) level it handles all the exceptions and dialogs
 * 
 * @author famanson
 *
 * @param <A> the input for doInBackGround()
 * @param <B> the input for onProgressUpdate()
 * @param <C> the input for onPostExecute() and the output of doInBackground()
 * 
 */
public abstract class BackgroundTask<A, B, C> extends AsyncTask<A, B, C> {
	/**
	 * boolean flag set to true when a jsonException is found
	 */
	protected boolean jsonException = false;
	/**
	 * boolean flag set to true when a unknownHostException is found
	 */
	protected boolean unknownHostException = false;
	/**
	 * boolean flag set to true when a ioException is found
	 */
	protected boolean ioException = false;
	/**
	 * boolean flag set to true when a operationException is found
	 */
	protected boolean operationException = false;
	/**
	 * boolean flag set to true when a otherException (i.e. unexpected exception) is found
	 */
	protected boolean otherException = false;
	/**
	 * boolean flag set to true when a malformedURLException is found
	 */
	protected boolean malformedURLException = false;
	/**
	 * boolean flag set to true when a nullPointerException is found
	 */
	protected boolean nullPointerException = false;
	/**
	 * boolean flag set to true when a parseException is found
	 */
	protected boolean parseException = false;
	/**
	 * boolean flag set to true when the Page given is to be destroyed after failure, must be initialised
	 */
	protected boolean toDestroyPageAfterFailure;
	/**
	 * boolean flag set to true when a Page needs to be destroyed at runtime 
	 */
	protected boolean destroyPlease;
	/**
	 * the Page that the task is executed on
	 */
	protected Page page;
	/**
	 * the progress dialog seen when the task is running
	 */
	protected ProgressDialog pDialog;
	/**
	 * boolean flag indicates whether the dialog should be visible or not
	 */
	protected boolean dialogEnabled;
	/**
	 * a reference to the global application state
	 */
	protected MyApplication myApp;
	
	/**
	 * the constructor of a background task
	 * 
	 * @param page the Page that the task is executed on  
	 * @param toDestroyPageAfterFailure whether the page should be destroyed in case of failure
	 * @param dialogEnabled whether the dialog for the task is visible 
	 */
	public BackgroundTask(Page page, boolean toDestroyPageAfterFailure, boolean dialogEnabled)
	{
		super();
		this.page = page;
		this.toDestroyPageAfterFailure = toDestroyPageAfterFailure;
		destroyPlease = false;
		this.dialogEnabled = dialogEnabled;
		myApp = (MyApplication) page.getApplication();
	}
	
	/**
	 * called before doInBackground(), spawns the dialog if needed 
	 */
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
	
	/**
	 * called instead of onPostExecute() when the task is cancelled, in this case also when 
	 * the user cancels the dialog (by pressing the Back key) 
	 */
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
	
	/**
	 * called after the execution is done, this is where all the exceptions are handled,
	 * where the page is properly destroyed if needed and where the progress dialog is dismissed
	 */
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
	/**
	 * an abstract method for updating the layout of the page, for use in concrete subclasses,
	 * it is called in the onPostExecute() method if the outputs of doInBackground is not null
	 * @param outputs the outputs of doInBackground()
	 */
	public abstract void updateView(C outputs);
}
