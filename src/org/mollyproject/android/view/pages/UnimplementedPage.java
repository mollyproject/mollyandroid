package org.mollyproject.android.view.pages;

import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import android.os.Bundle;
import android.webkit.WebView;

public abstract class UnimplementedPage extends ContentPage {
	protected WebView webView;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		webView = new WebView(this);
		try {
			webView.loadUrl(getActualURL(SelectionManager
							.getName(getInstance().getClass())));
		} catch (Exception e) {
		}
		contentLayout.addView(webView);
		setContentView(contentLayout);
	}
	
	public String getActualURL(String string) throws Exception
	{
		return Router.getFrom((Router.mOX +"reverse/?name="+ string));
	}
}
