package org.mollyproject.android.view.apps;

import java.util.List;

import org.mollyproject.android.controller.Router;

import android.os.Bundle;
import android.webkit.WebView;

public class UnimplementedPage extends ContentPage {
	//for pages that are not customised yet, simply load a WebView pointing to the url on the web site
	protected WebView webView;
	protected String locator;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		locator = myApp.getLocator();
		webView = new WebView(this);
		try {
			String temp = getActualURL(locator);
			webView.loadUrl(temp);
		} catch (Exception e) {
			e.printStackTrace();
			Page.popupErrorDialog("Loading Page failed", 
					"Page is not loaded. " +
					"Please try again later", this, true);
		}
		contentLayout.addView(webView);
	}
	
	public String getActualURL(String string) throws Exception
	{
		return router.getFrom((Router.mOX +"reverse/?name="+ string));
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
