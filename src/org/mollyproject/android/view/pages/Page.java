package org.mollyproject.android.view.pages;

public abstract class Page {
	protected String locator;
	//each Page is a state of the display
	public Page()
	{			
		//each subclass should have a different locator, indicating the
		//location of the page itself, this locator is actually the view name		
	}
		
	public String getLocator()
	{
		return locator;
	}
	
	public abstract void refresh();
}
