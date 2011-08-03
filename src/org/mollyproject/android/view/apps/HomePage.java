package org.mollyproject.android.view.apps;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import roboguice.inject.InjectView;

import com.google.inject.Inject;
import com.google.inject.Injector;
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
	
	@Inject @Named("contact:index") Page contactPage;
	@InjectView (R.id.gridView) GridView gridview;
	@InjectView (R.id.search) EditText searchField;
	
	protected ArrayList<Button> breadCrumbs;
	protected LinearLayout bcLayout;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	System.out.println("Contact " + contactPage);
    	System.out.println("Grid " + gridview);
    	super.onCreate(savedInstanceState);
    	
    	Injector injector;
    	
    	this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    	
    	setContentView(R.layout.grid_viewer);
    	
    	//GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Intent myIntent = new Intent(v.getContext(), SelectionManager
						.getPageClass(SelectionManager.CONTACT_PAGE));
                startActivityForResult(myIntent, 0);
            }
        });
    	
    	searchField.setBackgroundResource(R.drawable.rounded_edittext);
    	
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
    	//home page still contributes to breadcrumb update, but doesn't need a bar on it
    	//so no need to call bcBar.reconstruct
		myApp.updateBreadCrumb(SelectionManager.getName(getInstance().getClass()));
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private class NetworkPollingTask extends AsyncTask<Void,Void,Void>
    {
    	//check for connection everytime the app is started and also spawn the location thread
    	//if necessary
    	protected boolean jsonException = false;
    	protected boolean networkException = false;
    	
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				System.out.println("Router "+router);
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
					
					//Establish a connection (mainly to get the csrftoken inside the cookieMgr
		    		String jsonText = router.onRequestSent(
							SelectionManager.getName(HomePage.this.getClass()),
							Router.OutputFormat.JSON,null);
		    		System.out.println("JSON Text " + jsonText);
					jsonContent = new JSONObject(jsonText);
					
					//connection succeeded, had csrftoken, spawn the location thread
					router.spawnNewLocThread();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				networkException = true;
			} catch (JSONException e) {
				e.printStackTrace();
				jsonException = true;
			} catch (NullPointerException e)
			{
				e.printStackTrace();
				networkException = true;
			} catch (UnknownHostException e) {
				e.printStackTrace();
				networkException = true;
			} catch (IOException e) {
				e.printStackTrace();
				networkException = true;
			}
			finally
			{
				router.waitForRequests(); //return the router to the waiting state
			}
			return null;
		}
    	
		protected void onPostExecute(Void result)
		{
			System.out.println(networkException +" "+ jsonException);
			pDialog.dismiss();
			
			if (networkException) 
			{
				popupErrorDialog("Cannot connect to m.ox.ac.uk", 
						"There might be a problem with internet connection. " +
						"Please try restarting the app", HomePage.this);
			}
			if (jsonException)
			{
				popupErrorDialog("JSON Exception", 
						"There might be a problem with JSON output " +
						"from server. Please try again later.", HomePage.this);
			}
			
		}
    }
    
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        private String[] pages = {
        		"contact:index", "library:index"
        };
        
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return pages.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(SelectionManager.getImg(pages[position]));
            imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent myIntent = new Intent(v.getContext(), myApp.test("contact:index").getClass());
	                startActivityForResult(myIntent, 0);
				}
			});
            return imageView;
        }

    }
}





























