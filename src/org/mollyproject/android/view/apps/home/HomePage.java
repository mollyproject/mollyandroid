package org.mollyproject.android.view.apps.home;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import roboguice.inject.InjectView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
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
	public static boolean firstHomeLoad = true;
	/** Called when the activity is first created. */
    @Override
    
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.grid_viewer);
    	RelativeLayout searchBar = (RelativeLayout) layoutInflater.inflate
    											(R.layout.search_bar,homeLayout, false);
    	homeLayout.addView(searchBar,0);
    	final EditText searchField = (EditText) findViewById(R.id.searchField);
    	searchField.setWidth(200);
    	searchField.invalidate();
    	setEnterKeySearch(searchField, this, null);
    }
    
    public LinearLayout getHomeLayout()
    {
    	return homeLayout;
    }
    
    public Page getInstance()
    {
    	return this;
    }
    @Override
    public void onResume()
    {
    	super.onResume();
    	if (firstHomeLoad || MyApplication.destroyed)
    	{
    		new NetworkPollingTask(this, false, true).execute();
    	}
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

	@Override
	public String getName() {
		return MollyModule.HOME_PAGE;
	}

	@Override
	public String getQuery() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAdditionalParams() {
		// TODO Auto-generated method stub
		return null;
	}
}





























