package org.mollyproject.android.view.apps.unimplemented;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MollyWebClient extends WebViewClient {
	@Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		System.out.println(url);
        if (Uri.parse(url).getHost().equals("weblearn.ox.ac.uk")) {
            return false;
        }
        else if (Uri.parse(url).getHost().equals("webauth.ox.ac.uk")) {
        	return false;
        }
        else if (Uri.parse(url).getHost().equals("m.ox.ac.uk") || Uri.parse(url).getHost().equals("dev.m.ox.ac.uk"))
        {
        	//it is now very difficult to determine which exact page to go to dynamically
        	//(some pages have extra arguments and queries), so the short-term solution is to
        	//have each special case for each page. For now, there is only the weblearn page
        	
        	//WebLearn stuff:
        	Intent myIntent = new Intent(view.getContext(), MyApplication.getPageClass(MollyModule.WEBLEARN));
        	view.getContext().startActivity(myIntent);
        }
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        //startActivity(intent);
        return true;
    }
}
