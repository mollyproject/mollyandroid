package org.mollyproject.android;

import java.net.MalformedURLException;

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
		
		final Button resultButton = (Button) findViewById(R.id.resultButton);
		
        resultButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {  			
				try {
					router.onRequestSent(SelectionManager.
							getStringLocator(SelectionManager.ViewNames.result_index));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
        });
        
        setContentView(R.layout.main);
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