package org.mollyproject.android.view.apps.weblearn;

import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.unimplemented.UnimplementedPage;

import android.os.Bundle;
import android.view.View;

public class WebLearnLoginPage extends UnimplementedPage{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((View) contentScroll.getParent()).setBackgroundResource(R.drawable.shape_white);
	}
	@Override
	public void onResume() {
		loaded = true; //no PageSetupTask
		super.onResume();
	}
	
	@Override
	public void refresh() {
		webView.loadUrl(locator);
	}
}
