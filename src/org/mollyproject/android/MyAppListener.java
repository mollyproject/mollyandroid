package org.mollyproject.android;

import org.mollyproject.android.view.pages.Page;

public interface MyAppListener {
	public void onBreadCrumbAdded(String breadcrumb);
	public void onBreadCrumbRemoved(String breadcrumb);
	public boolean canBeRemoved(String breadcrumb);
	public Class<? extends Page> getOwnerClass();
}
