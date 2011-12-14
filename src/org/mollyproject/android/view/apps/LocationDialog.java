package org.mollyproject.android.view.apps;

import android.app.Dialog;

public class LocationDialog extends Dialog {
	protected Page page;
	public LocationDialog(Page page) {
		super(page);
		this.page = page;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		page.onResume();//refresh the page if needed (must be specified in this onResume() method in the page itself)
	}

}
