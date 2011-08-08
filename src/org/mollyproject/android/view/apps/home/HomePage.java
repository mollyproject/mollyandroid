package org.mollyproject.android.view.apps.home;
import java.util.ArrayList;
import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.Page;

import roboguice.inject.InjectView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
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
    
    public void updateGrid(ImageAdapter newGridAdapter)
    {
    	gridview.setAdapter(newGridAdapter);
    }
}




























