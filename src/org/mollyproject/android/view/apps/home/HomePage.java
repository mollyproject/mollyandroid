package org.mollyproject.android.view.apps.home;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.Page;
import roboguice.inject.InjectView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HomePage extends Page {
	
	@InjectView (R.id.listView) ListView listview;
	//@InjectView (R.id.bottomLayout) LinearLayout bottomLayout;
	@InjectView (R.id.homeLayout) LinearLayout homeLayout;
	protected ArrayList<Button> breadCrumbs;
	protected LinearLayout bcLayout;
	public static boolean firstHomeLoad = true;
	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	name = MollyModule.HOME_PAGE;
    	setContentView(R.layout.home_page_layout);
    	RelativeLayout searchBar = (RelativeLayout) getLayoutInflater().inflate
    			(R.layout.search_bar_without_margin,null);
    	homeLayout.addView(searchBar,0);
    	EditText searchField = (EditText) findViewById(R.id.searchField);
    	searchField.setWidth(200);
    	searchField.invalidate();
    	setEnterKeySearch(searchField, this, null);
    	
    	Button searchButton = (Button) findViewById(R.id.searchButton);
    	setClickSearch(searchButton, searchField, this, null);
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
    	if (firstHomeLoad || MyApplication.destroyed || MyApplication.availableApps == null)
    	{
    		refresh();
    	}
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	//Special case for the back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        else
        {
        	return super.onKeyDown(keyCode, event);
        }
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	MenuItem switchDomain = menu.findItem(R.id.switchDomain);
    	switchDomain.setVisible(true);
    	if (Router.mOX.equals("http://m.ox.ac.uk/"))
    	{
    		switchDomain.setTitle("Switch to dev.m");
    	}
    	else
    	{
    		switchDomain.setTitle("Switch to m.ox");
    	}
    	return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId())
    	{
    		case R.id.switchDomain:
    			if (Router.mOX.equals("http://m.ox.ac.uk/"))
    	    	{
    				Router.mOX = "http://dev.m.ox.ac.uk/";
    	    	}
    	    	else
    	    	{
    	    		Router.mOX = "http://m.ox.ac.uk/";
    	    	}
    			Toast.makeText(getApplicationContext(), "Now using " + Router.mOX, Toast.LENGTH_SHORT).show();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    	
    }
    
    public void updateList(ImageAdapter newListAdapter)
    {
    	listview.setAdapter(newListAdapter);
    }

	@Override
	public String getQuery() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinearLayout getContentLayout() {
		return homeLayout;
	}
	@Override
	public void setContentLayout(LinearLayout contentLayout) {
		// TODO Auto-generated method stub
	}
	@Override
	public void refresh() {
		new NetworkPollingTask(this, false, true).execute();
	}
}





























