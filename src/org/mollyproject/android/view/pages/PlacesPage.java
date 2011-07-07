package org.mollyproject.android.view.pages;

public class PlacesPage extends Page {
	public static final Page INSTANCE = new PlacesPage();
	public PlacesPage()
	{
		locator = "places:index";
	}
	public void refresh() {
	}
}
