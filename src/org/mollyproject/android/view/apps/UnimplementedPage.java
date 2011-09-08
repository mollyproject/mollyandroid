package org.mollyproject.android.view.apps;

import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class UnimplementedPage extends ContentPage {
	//for pages that are not customised yet, simply load a WebView pointing to the url on the web site
	protected WebView webView;
	protected String actualURL;
	protected String locator;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		locator = MyApplication.locator; //myApp.getLocator();
		webView = new WebView(this);
		refresh();
		contentLayout.addView(webView);
	}
	
	@Override
	public void refresh() {
		try {
			if (actualURL == null)
			{
				actualURL = getActualURL(locator);
			}
			webView.loadUrl(actualURL);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Loading Page failed. " +
					"Please try again later", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public String getActualURL(String string) throws Exception
	{
		return MyApplication.router.getFrom((Router.mOX +"reverse/?name="+ string));
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getAdditionalParams() {
		return null;
	}

	@Override
	public String getName() {
		return locator;
	}

	@Override
	public String getQuery() {
		return null;
	}
}
