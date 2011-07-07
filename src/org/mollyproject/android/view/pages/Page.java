package org.mollyproject.android.view.pages;

public abstract class Page {
	//protected String locator;
	//protected Renderer ren;
	//each Page is a state of the display
	public Page()
	{			
		//this.ren = ren;
		//ren.addPage(this);
		//each subclass should have a different locator, indicating the
		//location of the page itself, this locator is actually the view name		
	}			
	
	public abstract void refresh();
}
