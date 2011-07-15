package org.mollyproject.android;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.Renderer;
import org.mollyproject.android.view.breadcrumbs.BreadCrumbBar;
import org.mollyproject.android.view.breadcrumbs.BreadCrumbFragment;
import org.mollyproject.android.view.breadcrumbs.BreadCrumbRenderer;
import org.mollyproject.android.view.pages.HomePage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Main extends Activity {
	protected Router router;
	protected Renderer ren;
	protected ArrayList<Button> breadCrumbs;
	protected LinearLayout linearLayout1;

	BreadCrumbBar bcBar;
	BreadCrumbRenderer bcRen;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        
        ren = new Renderer();
		try {
			router = new Router(ren,getApplicationContext());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setContentView(R.layout.main);
		
		linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		bcBar = new BreadCrumbBar(getApplicationContext());
		bcRen = new BreadCrumbRenderer(bcBar, linearLayout1);
		
		Button button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Main.this.bcBar.addBreadCrumb(new BreadCrumbFragment(new HomePage()));
			}
		});
		
		Button button6 = (Button) findViewById(R.id.button6);
		button6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Main.this.bcBar.removeBreadCrumb();
			}
		});
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
}