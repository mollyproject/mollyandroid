package org.mollyproject.android.view.apps;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.Router;

import android.os.Bundle;
import android.webkit.WebView;

public class UnimplementedPage extends ContentPage {
	protected WebView webView;
	protected String locator;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		locator = myApp.getLocator();
		webView = new WebView(this);
		try {
			String temp = getActualURL(locator);
			System.out.println(temp);
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
		return Router.getFrom((Router.mOX +"reverse/?name="+ string));
	}

	@Override
	public Page getInstance() {
		return this;
	}
}
