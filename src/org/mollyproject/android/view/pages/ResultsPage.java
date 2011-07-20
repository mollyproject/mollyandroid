package org.mollyproject.android.view.pages;

import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultsPage extends UnimplementedPage {
	public static final Page INSTANCE = new ResultsPage();
	protected Router router;
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//myApp.addBreadCrumb(SelectionManager.getName(INSTANCE));
		try {
			webView.loadUrl(Router.getFrom(setLocator(SelectionManager.RESULTS_PAGE)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		TextView testText = new TextView(getApplicationContext());
		router = myApp.getRouter();
		String jsonText = null;
		try {
			jsonText = router.onRequestSent("results:index");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (jsonText != null) testText.setText(jsonText);
		contentLayout.addView(testText,ViewGroup.LayoutParams.FILL_PARENT);
		
		setContentView(contentLayout);
	}
	

}
