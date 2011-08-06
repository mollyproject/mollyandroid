package org.mollyproject.android.view.apps.home;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.Page;

import roboguice.inject.InjectView;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class HomePage extends Page {
	
	@InjectView (R.id.gridView) GridView gridview;
	@InjectView (R.id.bottomLayout) LinearLayout bottomLayout;
	@InjectView (R.id.homeLayout) LinearLayout homeLayout;
	protected ImageAdapter gridIconsAdapter;
	protected ArrayList<Button> breadCrumbs;
	protected LinearLayout bcLayout;
	protected boolean loaded = false;
	/** Called when the activity is first created. */
    @Override
    
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	setContentView(R.layout.grid_viewer);
    	RelativeLayout searchBar = (RelativeLayout) layoutInflater.inflate(R.layout.search_bar,homeLayout, false);
    	homeLayout.addView(searchBar,0);
    }
    
    public Page getInstance()
    {
    	return this;
    }
	
    @Override
    public void onResume()
    {
    	super.onResume();
		new NetworkPollingTask(this, false).execute();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private class NetworkPollingTask extends BackgroundTask<Void,Void,Void>
    {
    	//check for connection everytime the app is started and also spawn the location thread
    	//if necessary
    	
		public NetworkPollingTask(Page page, boolean b) {
			super(page,b);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				//Establish a connection
				if (!loaded)
				{
					System.out.println("Router called");
		    		JSONObject output = router.onRequestSent(
							MollyModule.getName(HomePage.this.getClass()),
							Router.OutputFormat.JSON,null);
					
					JSONArray availableApps = output.getJSONArray("applications");
					List<String> appsList = new ArrayList<String>(); 
					for (int i = 0; i < availableApps.length(); i++)
					{
						JSONObject app = availableApps.getJSONObject(i);
						if (app.getBoolean("display_to_user"))
						{
							appsList.add(app.getString("local_name")+":index");
						}
					}
					gridIconsAdapter = new ImageAdapter(page, appsList);
				}
				if (router.getLocThread() != null)
		    	{
			    	if (router.getLocThread().isInterrupted())
			    	{
			    		System.out.println("LocThread needs to restart");
						router.spawnNewLocThread();
			    	}
	    		}
				else
				{
					//LocThread is actually null, it is not there
					//this happens when either no connection has been made before
					//or the LocThread has been made null and checked explicitly 
					//to prevent the NullPointerException
					
					router.spawnNewLocThread();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				malformedURLException = true;
			} catch (JSONException e) {
				e.printStackTrace();
				jsonException = true;
			} catch (NullPointerException e)
			{
				e.printStackTrace();
				nullPointerException = true;
			} catch (UnknownHostException e) {
				e.printStackTrace();
				unknownHostException = true;
			} catch (IOException e) {
				e.printStackTrace();
				ioException = true;
			}
			return null;
		}
    	
		@Override
		public void updateView(Void outputs) {
			gridview.setAdapter(gridIconsAdapter);
			loaded = true;
		}
    }
}





























