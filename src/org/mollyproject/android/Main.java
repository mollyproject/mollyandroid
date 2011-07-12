package org.mollyproject.android;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.Renderer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main extends Activity {
	protected Router router;
	protected Renderer ren;		
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

    	try {
			router.onRequestSent(ren.getSelectionManager().getStringLocator(SelectionManager.ViewNames.home_index));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		final Button button = (Button) findViewById(R.id.button);
		
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });        
    }
    
    //make the location thread terminate a bit cleaner
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	router.getLocThread().interrupt();
    }
}