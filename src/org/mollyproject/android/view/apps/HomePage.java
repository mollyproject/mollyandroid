package org.mollyproject.android.view.apps;
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
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import roboguice.inject.InjectView;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
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

public class HomePage extends Page {
	
	@InjectView (R.id.gridView) GridView gridview;
	@InjectView (R.id.searchField) EditText searchField;
	@InjectView (R.id.bottomLayout) LinearLayout bottomLayout;
	
	protected ImageAdapter gridIconsAdapter;
	protected ArrayList<Button> breadCrumbs;
	protected LinearLayout bcLayout;
	
	/** Called when the activity is first created. */
    @Override
    
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	setContentView(R.layout.grid_viewer);
    	
    	//bottomLayout.setMinimumHeight(getWindowManager().getDefaultDisplay().getHeight()/3);
    	//gridIconsAdapter = new ImageAdapter(this);
    }
    
    public Page getInstance()
    {
    	return this;
    }
	
    @Override
    public void onResume()
    {
    	super.onResume();
    	pDialog = ProgressDialog.show(this, "", "Loading...", true, false);
    	new NetworkPollingTask().execute();
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
    	
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				//Establish a connection
	    		String jsonText = router.onRequestSent(
						SelectionManager.getName(HomePage.this.getClass()),
						Router.OutputFormat.JSON,null);
	    		System.out.println("JSON Text " + jsonText);
				jsonContent = new JSONObject(jsonText);
				
				JSONArray availableApps = jsonContent.getJSONArray("applications");
				List<String> appsList = new ArrayList<String>(); 
				for (int i = 0; i < availableApps.length(); i++)
				{
					JSONObject app = availableApps.getJSONObject(i);
					if (app.getBoolean("display_to_user"))
					{
						appsList.add(app.getString("local_name"));
					}
				}
				gridIconsAdapter = new ImageAdapter(HomePage.this, appsList);
				
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
			finally
			{
				router.waitForRequests(); //return the router to the waiting state
			}
			return null;
		}
    	
		@Override
		public void updateView(Void outputs) {
			gridview.setAdapter(gridIconsAdapter);
			pDialog.dismiss();
		}
    }
    
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        protected List<String> apps = null;
        
        public ImageAdapter(Context c, List<String> apps) { mContext = c; this.apps = apps; }

        public int getCount() { return apps.size(); }
        
        public List<String> getAppsList() { return apps; }

        public Object getItem(int position) { return null; }

        public long getItemId(int position) { return 0; }
        
        public void addApp(String app) { apps.add(app);  }
        
        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
            } else {
                imageView = (ImageView) convertView;
            }
            
            imageView.setImageResource(SelectionManager.getImg(apps.get(position)+":index"));
            imageView.setMaxWidth(HomePage.this.getWindowManager().getDefaultDisplay().getWidth()/3);
            imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					myApp.setUnimplementedLocator(apps.get(position)+":index");
					Intent myIntent = new Intent(v.getContext(), myApp.test(apps.get(position)).getClass());
	                startActivityForResult(myIntent, 0);
				}
			});
            return imageView;
        }
    }
}





























