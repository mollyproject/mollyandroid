package org.mollyproject.android.view.apps;

import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
			e.printStackTrace();
			AlertDialog dialog = Page.popupErrorDialog("Loading Page failed", 
					"Page is not loaded. " +
					"Please try again later", UnimplementedPage.this);
			dialog.setButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					UnimplementedPage.this.finish();
				}
			});
		}
		contentLayout.addView(webView);
		setContentView(contentLayout);
	}
	
	public String getActualURL(String string) throws Exception
	{
		return Router.getFrom((Router.mOX +"reverse/?name="+ string));
	}
}
