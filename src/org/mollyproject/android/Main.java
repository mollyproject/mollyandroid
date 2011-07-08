package org.mollyproject.android;

import org.json.JSONObject;
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
	//protected LocationThread locThread;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        
        ren = new Renderer();
		router = new Router(ren);		
		//locThread = new LocationThread();
		
		JSONObject o = new JSONObject();
		setContentView(R.layout.main);
		//Trying out locator request:
		/*try {
			ren.setNewPage(PlacesPage.INSTANCE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

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
}