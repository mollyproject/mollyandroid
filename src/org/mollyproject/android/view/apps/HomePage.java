package org.mollyproject.android.view.apps;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

public class HomePage extends Page {
	
	protected ArrayList<Button> breadCrumbs;
	protected LinearLayout bcLayout;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private ViewFlipper viewFlipper;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        
    	/*myApp.updateBreadCrumb(SelectionManager.getName(HomePage.class));
        System.out.println("Home added breadcrumb");
    	
		LinearLayout contentLayout = new LinearLayout(this);
		contentLayout.setOrientation(LinearLayout.VERTICAL);
		contentLayout.addView(bcBar.getBar(), new ViewGroup.LayoutParams
				(getWindowManager().getDefaultDisplay().getWidth(), 
				getWindowManager().getDefaultDisplay().getHeight()/10));
		
		ScrollView scr = new ScrollView(this); 
		
		Button resultsButton = new Button(this);
		resultsButton.setText("Go to Results");
		resultsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), SelectionManager
						.getPageClass(SelectionManager.RESULTS_PAGE));
                startActivityForResult(myIntent, 0);
			}
		});
		
		Button contactsButton = new Button(this);
		contactsButton.setText("Go to Contacts");
		contactsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), SelectionManager
						.getPageClass(SelectionManager.CONTACT_PAGE));
                startActivityForResult(myIntent, 0);
			}
		});
		
		Button featureButton = new Button(this);
		featureButton.setText("Suggest new features");
		featureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), SelectionManager
						.getPageClass(SelectionManager.FEATURE_VOTE));
                startActivityForResult(myIntent, 0);
			}
		});
		
		Button libraryButton = new Button(this);
		libraryButton.setText("Go to Library Search");
		libraryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), SelectionManager
						.getPageClass(SelectionManager.LIBRARY_PAGE));
                startActivityForResult(myIntent, 0);
			}
		});
		
		Button placesButton = new Button(this);
		placesButton.setText("Go to Places");
		placesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), SelectionManager
						.getPageClass(SelectionManager.PLACES_PAGE));
                startActivityForResult(myIntent, 0);
			}
		});
		
		contentLayout.addView(contactsButton);
		contentLayout.addView(resultsButton);
		contentLayout.addView(libraryButton);
		contentLayout.addView(placesButton);
		contentLayout.addView(featureButton);
		
		scr.addView(contentLayout);
		setContentView(scr);*/
    	
    	setContentView(R.layout.view_flipper);
    	
    	viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper1);
    	gestureDetector = new GestureDetector(new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };
        
        RelativeLayout contactOuterLayout = (RelativeLayout) findViewById(R.id.contactOuterLayout);
    	
    	RelativeLayout libraryOuterLayout = (RelativeLayout) findViewById(R.id.libraryOuterLayout);
        
        Button prevButton = (Button) findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewFlipper.setInAnimation
        			(AnimationUtils.loadAnimation(HomePage.this, R.anim.slide_right_in));
				viewFlipper.setOutAnimation
            		(AnimationUtils.loadAnimation(HomePage.this, R.anim.slide_right_out));
				viewFlipper.showPrevious();
			}
		});
        
        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewFlipper.setInAnimation
        			(AnimationUtils.loadAnimation(HomePage.this, R.anim.slide_left_in));
				viewFlipper.setOutAnimation
            		(AnimationUtils.loadAnimation(HomePage.this, R.anim.slide_left_out));
				viewFlipper.showNext();
			}
		});
    	/*EditText searchField = (EditText) findViewById(R.id.search);
    	searchField.setBackgroundResource(R.drawable.rounded_edittext);
    	
    	RelativeLayout contactSearch = (RelativeLayout) findViewById(R.id.contactButtonLayout);
    	contactSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), SelectionManager
						.getPageClass(SelectionManager.CONTACT_PAGE));
                startActivityForResult(myIntent, 0);				
			}
    	});*/
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
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
	        return true;
	    else
	    	return false;
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
						"Please try restarting the app", HomePage.this, true);
			}
			if (jsonException)
			{
				popupErrorDialog("JSON Exception", 
						"There might be a problem with JSON output " +
						"from server. Please try again later.", HomePage.this, true);
			}
			
		}
    }
    
    class MyGestureDetector extends SimpleOnGestureListener {

    	private static final int SWIPE_MIN_DISTANCE = 120;
    	private static final int SWIPE_MAX_OFF_PATH = 250;
    	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    	
    	@Override
    	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    		System.out.println("FLING");
    		
    		try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	viewFlipper.setInAnimation
                		(AnimationUtils.loadAnimation(HomePage.this, R.anim.slide_left_in));
                    viewFlipper.setOutAnimation
                    	(AnimationUtils.loadAnimation(HomePage.this, R.anim.slide_left_out));
                	viewFlipper.showNext();
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	viewFlipper.setInAnimation
                		(AnimationUtils.loadAnimation(HomePage.this, R.anim.slide_right_in));
                    viewFlipper.setOutAnimation
                    	(AnimationUtils.loadAnimation(HomePage.this, R.anim.slide_right_out));
                	viewFlipper.showPrevious();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
    	}
    	
    	@Override
    	public boolean onSingleTapConfirmed (MotionEvent e)
    	{
    		Intent myIntent = new Intent(HomePage.this, SelectionManager
					.getPageClass(viewFlipper.getCurrentView().getId()));
            startActivityForResult(myIntent, 0);
    		return false;
    	}
    }
}





























