package org.mollyproject.android.view.breadcrumbs;

import java.util.List;

import android.widget.Button;

public interface BreadCrumbsListener {
	public void onBreadCrumbAdded(BreadCrumbFragment bcFrag);
	public void onBreadCrumbRemoved() ;
}
