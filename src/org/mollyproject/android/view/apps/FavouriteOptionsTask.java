package org.mollyproject.android.view.apps;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;

import android.widget.Toast;

public class FavouriteOptionsTask extends BackgroundTask<Void, Void, Void> {

	public FavouriteOptionsTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(Void outputs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
        	if(MyApplication.csrfToken != null)
        	{
        		
    			System.out.println("Fav pressed");
	        	//post the favourite on to the web server
	        	List<NameValuePair> argsPairs = new ArrayList<NameValuePair>();
	             
	        	argsPairs.add(new BasicNameValuePair("csrfmiddlewaretoken", MyApplication.csrfToken));
	        	argsPairs.add(new BasicNameValuePair("format", "json"));
	        	argsPairs.add(new BasicNameValuePair("language_code", "en"));
	            System.out.println("fav link " + MyApplication.favouriteURL);
	            argsPairs.add(new BasicNameValuePair("URL", MyApplication.favouriteURL));
	            if (!page.isFavourite)
	        	{
	            	argsPairs.add(new BasicNameValuePair("favourite", ""));
	        	}
	            else
	        	{
	            	argsPairs.add(new BasicNameValuePair("unfavourite", "Unfavourite"));
	        	}
	             
				List<String> output = MyApplication.router.post(argsPairs,
						MyApplication.router.reverse(MollyModule.FAVOURITES, null));
				page.setFav(new JSONObject(output.get(0)).getBoolean("is_favourite"));
			} 
    	}
    	catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
		
		return null;
	}
	
}
