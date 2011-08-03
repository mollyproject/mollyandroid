package org.mollyproject.android.view.apps;

import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import android.os.Bundle;
import android.webkit.WebView;

public class UnimplementedPage extends ContentPage {
	protected WebView webView;
	protected String locator;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		locator = myApp.getUnimplementedLocator();
		webView = new WebView(this);
		try {
			webView.loadUrl(getActualURL(SelectionManager
							.getName(getInstance().getClass())));
		} catch (Exception e) {
			e.printStackTrace();
			Page.popupErrorDialog("Loading Page failed", 
					"Page is not loaded. " +
					"Please try again later", this, true);
		}
		contentLayout.addView(webView);
		setContentView(contentLayout);
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
