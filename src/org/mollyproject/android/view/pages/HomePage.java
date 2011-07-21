package org.mollyproject.android.view.pages;
import java.util.ArrayList;

import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.Renderer;
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
	
	protected Renderer ren;
	protected ArrayList<Button> breadCrumbs;
	protected LinearLayout bcLayout;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        ren = new Renderer();
        
    	myApp.addBreadCrumb(SelectionManager.getName(INSTANCE));
        System.out.println("Home added breadcrumb");
    	
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
		
		contentLayout.addView(resultsButton);
		setContentView(contentLayout);
    }
    
    //make the location thread terminate a bit cleaner
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	myApp.getRouter().getLocThread().stopThread();
    	myApp.getRouter().getLocThread().interrupt();
    }    
        
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            onDestroy();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}