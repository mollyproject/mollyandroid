package org.mollyproject.android.view.apps.unimplemented;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MollyWebClient extends WebViewClient {
	protected ProgressDialog pDialog;
	protected boolean pDialogLoaded = false;	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		if (!pDialogLoaded)
		{
			pDialogLoaded = true;
			pDialog = ProgressDialog.show(view.getContext(), null, "Loading");
		}
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		pDialogLoaded = false;
		pDialog.dismiss();
	}	
	@Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		System.out.println(url);
		if ((Uri.parse(url).getHost().equals("m.ox.ac.uk") || Uri.parse(url).getHost().equals("dev.m.ox.ac.uk")))
        {
        	//it is now very difficult to determine which exact page to go to dynamically
        	//(some pages have extra arguments and queries), so the short-term solution is to
        	//have each special case for each page. For now, there is only the weblearn page
        	
        	if (url.contains("weblearn"))
        	{
        		try {
					URL newURL = new URL(url);
					String query = newURL.getQuery();
					
					String[] params = query.split("&");  
				    Map<String, String> map = new HashMap<String, String>();  
				    for (String param : params)  
				    {  
				        String name = param.split("=")[0];  
				        String value = param.split("=")[1];  
				        map.put(name, value);  
				    }
				    
				    if (map.containsKey("oauth_token"))
				    {
				    	MyApplication.oauthToken = map.get("oauth_token");
				    }
				    
				    if (map.containsKey("oauth_verifier"))
				    {
				    	MyApplication.oauthVerifier = map.get("oath_verifier");
				    }
				    else
				    {
				    	MyApplication.oauthVerifier = null;
				    }
					
					Intent myIntent = new Intent(view.getContext(), MyApplication.getPageClass(MollyModule.WEBLEARN));
		        	view.getContext().startActivity(myIntent);
		        	
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
	        	//WebLearn stuff:
	        
	        	
	        	//int firs
	        	
	        	return true;
        	}
        	
        }
		/*else if (Uri.parse(url).getHost().equals("weblearn.ox.ac.uk")) {
            return false;
        }
        else if (Uri.parse(url).getHost().equals("webauth.ox.ac.uk")) {
        	return false;
        }*/
		return false;
    }
}
