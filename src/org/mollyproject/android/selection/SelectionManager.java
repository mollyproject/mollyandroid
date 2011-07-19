package org.mollyproject.android.selection;

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


	//The following hash table allows for easier future change in implementation
	//of new pages
	protected static HashBiMap<String,Page> pages 
					= HashBiMap.create();
	static {
		pages.put(HOME_PAGE, HomePage.INSTANCE);
		pages.put(RESULTS_PAGE, ResultsPage.INSTANCE);		
	}
	
	public SelectionManager()
	{
		currentPageName = HOME_PAGE;
	}
	
	public void setPage(String newPageName)
	{
		currentPageName = newPageName;
	}
	
	public static String getName(Page page)
	{
		return (pages.inverse().get(page));
	}
	
	public static Page getPage(String s)
	{
		return pages.get(s);
	}		
}
