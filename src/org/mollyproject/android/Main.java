package org.mollyproject.android;

import org.json.JSONObject;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.Renderer;
import android.app.Activity;
import android.os.Bundle;
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
				
        TextView tv = new TextView(this);
        tv.setText(o.toString());
        setContentView(tv);
    }
}