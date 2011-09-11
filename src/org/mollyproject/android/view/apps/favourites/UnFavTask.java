package org.mollyproject.android.view.apps.favourites;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

public class UnFavTask extends BackgroundTask<Void, Void, Void>{

	public UnFavTask(Page page, boolean toDestroyPageAfterFailure,
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
		//post the favourite on to the web server
		try
		{
	    	List<NameValuePair> argsPairs = new ArrayList<NameValuePair>();
	         
	    	argsPairs.add(new BasicNameValuePair("csrfmiddlewaretoken", MyApplication.csrfToken));
	    	argsPairs.add(new BasicNameValuePair("format", "json"));
	    	argsPairs.add(new BasicNameValuePair("language_code", "en"));
	        System.out.println("fav link " + MyApplication.favouriteURL);
	        argsPairs.add(new BasicNameValuePair("URL", MyApplication.favouriteURL));
	        argsPairs.add(new BasicNameValuePair("unfavourite", ""));
	         
			List<String> output = MyApplication.router.post(argsPairs,
					MyApplication.router.reverse(MollyModule.FAVOURITES, null));
		} catch (Exception e) {
			operationException = true;
		}
		return null;
	}
	
}
