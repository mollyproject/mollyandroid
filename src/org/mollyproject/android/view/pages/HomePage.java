package org.mollyproject.android.view.pages;
import java.util.ArrayList;

import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.Renderer;
import org.mollyproject.android.view.breadcrumbs.BreadCrumbBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomePage extends Page {
	public static final Page INSTANCE = new HomePage();
	
	protected Router router;
	protected Renderer ren;
	protected ArrayList<Button> breadCrumbs;
	protected LinearLayout bcLayout;
	protected BreadCrumbBar bcBar;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        
        ren = new Renderer();
        
		bcBar = new BreadCrumbBar(getApplicationContext());
		
		LinearLayout contentLayout = new LinearLayout(this);
		contentLayout.setOrientation(LinearLayout.VERTICAL);
		contentLayout.addView(bcBar.getBar(), new ViewGroup.LayoutParams
				(getWindowManager().getDefaultDisplay().getWidth(), 
				getWindowManager().getDefaultDisplay().getHeight()/10));
		
		
		Button resultsButton = new Button(this);
		resultsButton.setText("Go to Results");
		resultsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				ArrayList<String> nameTrail = new ArrayList<String>();
				nameTrail.add(SelectionManager.HOME_PAGE);
				
				Intent myIntent = new Intent(view.getContext(), ResultsPage.class);
                startActivityForResult(myIntent, 0);
			}
		});
		
		contentLayout.addView(resultsButton, new ViewGroup.LayoutParams
				(getWindowManager().getDefaultDisplay().getWidth(), 
				getWindowManager().getDefaultDisplay().getHeight()/10));
		setContentView(contentLayout);
		/*
		Button addButton = (Button) findViewById(R.id.addButton);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Main.this.bcBar.addBreadCrumb(new BreadCrumbFragment(new HomePage()));
			}
		});
		
		Button removeButton = (Button) findViewById(R.id.removeButton);
		removeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Main.this.bcBar.removeBreadCrumb();
			}
		});
		
		//Button resultsButton = ;
		//resultsButton
		((Button) findViewById(R.id.resultsButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), ResultsPage.class);
                startActivityForResult(myIntent, 0);
			}
		});*/
    }
    
    public BreadCrumbBar getBCBar()
    {
    	return bcBar;
    }
    
    //make the location thread terminate a bit cleaner
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	router.getLocThread().stopThread();
    	router.getLocThread().interrupt();
    }    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}