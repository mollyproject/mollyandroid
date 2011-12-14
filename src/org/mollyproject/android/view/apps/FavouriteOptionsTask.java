package org.mollyproject.android.view.apps;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;

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
    			List<String> output;
	        	//post the favourite on to the web server
    			if (!page.isFavourite)
    	    	{
    				output = favourite(MyApplication.favouriteURL);
    	    	}
    	        else
    	    	{
    	        	output = unfavourite(MyApplication.favouriteURL);
    	    	}
				page.setFav(new JSONObject(output.get(0)).getBoolean("is_favourite"));
			} 
    	}
    	catch (Exception e) {
			e.printStackTrace();
			operationException = true;
		}
		
		return null;
	}
	
	public static List<String> unfavourite(String url) throws Exception
	{
		List<NameValuePair> argsPairs = new ArrayList<NameValuePair>();
        
    	argsPairs.add(new BasicNameValuePair("csrfmiddlewaretoken", MyApplication.csrfToken));
    	argsPairs.add(new BasicNameValuePair("format", "json"));
    	argsPairs.add(new BasicNameValuePair("language_code", "en"));
        argsPairs.add(new BasicNameValuePair("URL", url));//MyApplication.favouriteURL));
        argsPairs.add(new BasicNameValuePair("unfavourite", ""));
         
		return MyApplication.router.post(argsPairs,
				MyApplication.router.reverse(MollyModule.FAVOURITES, null));
	}
	
	public static List<String> favourite(String url) throws Exception
	{
		List<NameValuePair> argsPairs = new ArrayList<NameValuePair>();
        
    	argsPairs.add(new BasicNameValuePair("csrfmiddlewaretoken", MyApplication.csrfToken));
    	argsPairs.add(new BasicNameValuePair("format", "json"));
    	argsPairs.add(new BasicNameValuePair("language_code", "en"));
        argsPairs.add(new BasicNameValuePair("URL", url));//MyApplication.favouriteURL));
        argsPairs.add(new BasicNameValuePair("favourite", ""));
         
		return MyApplication.router.post(argsPairs,
				MyApplication.router.reverse(MollyModule.FAVOURITES, null));
	}
}
