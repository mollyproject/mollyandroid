package org.mollyproject.android.view.pages;

import org.mollyproject.android.controller.Router;

import android.os.Bundle;
import android.webkit.WebView;

public abstract class UnimplementedPage extends ContentPage {
	protected WebView webView;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		webView = new WebView(this);
		contentLayout.addView(webView);
	}
	
	public String setLocator(String string)
	{
		return (Router.mOX +"reverse/?name="+ string);
	}
}
