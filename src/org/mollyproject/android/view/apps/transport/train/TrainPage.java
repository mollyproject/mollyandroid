package org.mollyproject.android.view.apps.transport.train;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.transport.AutoRefreshPage;
import org.mollyproject.android.view.apps.transport.TransportPage;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TrainPage extends AutoRefreshPage {
	
	public static TrainPageRefreshTask trainPageRefreshTask;
	public final static String ARRIVALS = "arrivals";
	public final static String DEPARTURES = "departures";
	public static String board; //this will be changed only by the menu options
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		board = settings.getString("lastBoard", DEPARTURES);
	}
	
	@Override
	public void refresh() {
		if (TransportPage.transportTabHost.getCurrentTabTag().equals(TransportPage.RAIL))
		{
			System.out.println("Train Page resumed");
			//no need to check for board here because board is static, will always point to the last
			//one used in this instance of the page
			if (trainPageRefreshTask != null) 
			{
				trainPageRefreshTask.cancel(true);
			}

			Toast.makeText(this, "Please wait. This page might take a moment or two to refresh...", 
					Toast.LENGTH_SHORT).show();
			
			trainPageRefreshTask = new TrainPageRefreshTask(this, false, false);
			trainPageRefreshTask.execute();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (!TransportPage.transportTabHost.getCurrentTabTag().equals(TransportPage.RAIL) || TransportPage.transportPaused)
		{
			System.out.println("Train Page paused");
			
			editor.putString("lastBoard", board);
		    editor.commit();
			
			if (trainPageRefreshTask != null) 
			{
				trainPageRefreshTask.cancel(true);
			}
		}
	}
	
	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getQuery() throws UnsupportedEncodingException {
		return "&board=" + board;
	}

	@Override
	public String getAdditionalParams() {
		return "&arg=rail";
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.arrivals:
				board = ARRIVALS;
				break;
			case R.id.departures:
				board = DEPARTURES;
				break;
			default:
	        	return super.onOptionsItemSelected(item);
	    }
		onResume();
	    return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    menu.findItem(R.id.arrivals).setVisible(true);
	    menu.findItem(R.id.departures).setVisible(true);
	    return true;
	}

	@Override
	public void setContentLayout(LinearLayout contentLayout) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
