package org.mollyproject.android.view.pages;

public abstract class Page {
	protected String urlStr;
	protected String locator;
	//each Page is a state of the display
	public Page()
	{	
		urlStr = "http://m.ox.ac.uk/";
		//each subclass should have a different locator, indicating the
		//location of the page itself
		locator = "";
	}
		
	public String getURLStr()
	{
		return (urlStr+locator);
	}
	
	public abstract void refresh();
}
