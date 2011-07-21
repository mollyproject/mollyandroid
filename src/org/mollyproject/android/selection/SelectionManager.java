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
	public static String CONTACT_PAGE = "contact:index";
	public static String FEATURE_VOTE = "feature_vote:index";
	public static String FEEDBACK_PAGE = "feedback:index";
	public static String LIBRARY_PAGE = "library:index";
	
	//The following hash table allows for easier future change in implementation
	//of new pages
	protected static HashBiMap<String,Class<? extends Page>> pages 
					= HashBiMap.create();
	static {
		pages.put(HOME_PAGE, HomePage.class);
		pages.put(RESULTS_PAGE, ResultsPage.class);
		pages.put(PLACES_PAGE, PlacesPage.class);
		pages.put(CONTACT_PAGE, ContactPage.class);
		pages.put(FEATURE_VOTE, FeatureVotePage.class);
		pages.put(FEEDBACK_PAGE, FeedbackPage.class);
		pages.put(LIBRARY_PAGE, LibraryPage.class);
	}
	
	protected static HashBiMap<String,Integer> bcImg 
					= HashBiMap.create();
	
	static {
		bcImg.put(HOME_PAGE, R.drawable.apple_touch_icon);
		bcImg.put(RESULTS_PAGE, R.drawable.results_bc);
		bcImg.put(PLACES_PAGE, R.drawable.places_bc);
		bcImg.put(CONTACT_PAGE, R.drawable.contact_bc);
		bcImg.put(FEATURE_VOTE, R.drawable.feature_vote_bc);
		bcImg.put(LIBRARY_PAGE, R.drawable.library_bc);
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
