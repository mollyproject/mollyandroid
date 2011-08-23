package org.mollyproject.android.view.apps.transport;

import org.mollyproject.android.view.apps.Page;

public abstract class AutoRefreshPage extends Page {
	protected boolean needsRefreshing = false; //for the first request, json data already downloaded, no need to refresh
	public boolean firstReq = true;
	public boolean needsRefreshing()
	{
		return needsRefreshing;
	}
	
	public void toBeRefreshed(boolean b)
	{
		needsRefreshing = b;
	}
	
}
