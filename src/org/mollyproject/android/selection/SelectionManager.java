package org.mollyproject.android.selection;

import org.mollyproject.android.R;
import org.mollyproject.android.view.pages.*;

import com.google.common.collect.HashBiMap;
public class SelectionManager {
	//I don't want a simple list of Strings because that makes it more difficult
	//to query from, and Java won't take : as part of the name if that variable
	//is not a String, thus the complication introduced by the getStringLocator()
	
	//public static enum ViewNames { places_index, home_index, result_index };
	
	protected String currentPageName;
	
	public static String TRAIL = "trail";
	
	public static String HOME_PAGE = "home:index";
	public static String RESULTS_PAGE = "results:index";
	public static String PLACES_PAGE = "places:index";

	//The following hash table allows for easier future change in implementation
	//of new pages
	protected static HashBiMap<String,Class<? extends Page>> pages 
					= HashBiMap.create();
	static {
		pages.put(HOME_PAGE, HomePage.class);
		pages.put(RESULTS_PAGE, ResultsPage.class);
		pages.put(PLACES_PAGE, PlacesPage.class);
	}
	
	protected static HashBiMap<String,Integer> bcImg 
					= HashBiMap.create();
	
	static {
		bcImg.put(HOME_PAGE, R.drawable.apple_touch_icon);
		bcImg.put(RESULTS_PAGE, R.drawable.results_bc);
		bcImg.put(PLACES_PAGE, R.drawable.places_bc);
	}
	
	public SelectionManager()
	{
		currentPageName = HOME_PAGE;
	}
	
	public void setPage(String newPageName)
	{
		currentPageName = newPageName;
	}
	
	public static String getName(Class <? extends Page> pageClass)
	{
		return (pages.inverse().get(pageClass));
	}
	
	public static int getBCImg(String viewName)
	{
		return bcImg.get(viewName);
	}
	
	public static Class<? extends Page> getPageClass(String s)
	{
		return pages.get(s);
	}		
}
