package org.mollyproject.android;

import org.json.JSONObject;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.Renderer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {
	private Router router;
	private Renderer ren;	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        
        ren = new Renderer();
		router = new Router(ren);
				
		JSONObject o = new JSONObject();
		
		//Trying out locator request:
		/*try {
			ren.setNewPage(PlacesPage.INSTANCE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//Code to try out the button, failed miserably
		final Button button = (Button) findViewById(R.id.button);
		
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	try {
					router.onRequestSent(router.getRenderer().getSelectionManager().getStringLocator(SelectionManager.ViewNames.places_index));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
}