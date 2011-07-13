package org.mollyproject.android.selection;

import java.util.HashMap;
import java.util.Map;

import org.mollyproject.android.view.pages.*;

public class SelectionManager {
	//I don't want a simple list of Strings because that makes it more difficult
	//to query from, and Java won't take : as part of the name if that variable
	//is not a String, thus the complication introduced by the getStringLocator()
	
	public static enum ViewNames { places_index, home_index, result_index };
	
	//The following hash table allows for easier future change in implementation
	//of new pages
	protected static Map<String,Page> pages 
					= new HashMap<String,Page>();
	static {
		pages.put(getStringLocator(ViewNames.home_index), HomePage.INSTANCE);
		pages.put(getStringLocator(ViewNames.places_index), PlacesPage.INSTANCE);
		pages.put(getStringLocator(ViewNames.result_index), ResultPage.INSTANCE);		
	}
	protected ViewNames currentPage;
	
	public SelectionManager()
	{
		currentPage = ViewNames.home_index;
	}
	public void setPage(ViewNames newPage)
	{
		currentPage = newPage;
	}
	
	public Page getPage(String s)
	{
		return pages.get(s);
	}
		
	public static String getStringLocator(ViewNames name)
	{
		return name.toString().replace("_", ":");
	}

}
