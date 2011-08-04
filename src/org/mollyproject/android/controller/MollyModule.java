package org.mollyproject.android.controller;

import java.util.HashMap;
import java.util.Map;

import org.mollyproject.android.R;
import org.mollyproject.android.Splash;
import org.mollyproject.android.view.apps.*;
import org.mollyproject.android.view.apps.contact.ContactPage;
import org.mollyproject.android.view.apps.contact.ContactResultsPage;
import org.mollyproject.android.view.apps.features.FeatureVotePage;
import org.mollyproject.android.view.apps.feedback.FeedbackPage;
import org.mollyproject.android.view.apps.library.LibraryPage;
import org.mollyproject.android.view.apps.library.LibraryResultsPage;
import org.mollyproject.android.view.apps.results_release.ResultsReleasePage;

import com.google.common.collect.HashBiMap;
import com.google.inject.AbstractModule;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
public class MollyModule extends AbstractModule {
	//I don't want a simple list of Strings because that makes it more difficult
	//to query from, and Java won't take : as part of the name if that variable
	//is not a String, thus the complication introduced by the getStringLocator()
	
	//public static enum ViewNames { places_index, home_index, result_index };
	//ClassPathResource res = new ClassPathResource("spring-beans.xml");
	//BeanFactory factory = new XmlBeanFactory(res);
	public static String HOME_PAGE = "home:index";
	public static String RESULTS_PAGE = "results:index";
	public static String PLACES_PAGE = "places:index";
	public static String CONTACT_PAGE = "contact:index";
	public static String FEATURE_VOTE = "feature_vote:index";
	public static String FEEDBACK_PAGE = "feedback:index";
	public static String LIBRARY_PAGE = "library:index";
	public static String LIBRARY_RESULTS_PAGE = "library:search";
	public static String CONTACT_RESULTS_PAGE = "contact:result_list";
	
	//The following hash table allows for easier future change in implementation
	//of new pages
	protected static HashBiMap<String,Class<? extends Page>> pages 
					= HashBiMap.create();
	static {
		pages.put(HOME_PAGE, HomePage.class);
		pages.put(RESULTS_PAGE, ResultsReleasePage.class);
		pages.put(PLACES_PAGE, PlacesPage.class);
		pages.put(CONTACT_PAGE, ContactPage.class);
		pages.put(FEATURE_VOTE, FeatureVotePage.class);
		pages.put(FEEDBACK_PAGE, FeedbackPage.class);
		pages.put(LIBRARY_PAGE, LibraryPage.class);
		pages.put(LIBRARY_RESULTS_PAGE, LibraryResultsPage.class);
		pages.put(CONTACT_RESULTS_PAGE, ContactResultsPage.class);
	}
	
	protected static Map<String,Integer> bcImg 
					= new HashMap<String,Integer>();
	
	static {
		bcImg.put(HOME_PAGE, R.drawable.apple_touch_icon);
		bcImg.put(RESULTS_PAGE, R.drawable.results_bc);
		bcImg.put(PLACES_PAGE, R.drawable.places_bc);
		bcImg.put(CONTACT_PAGE, R.drawable.contact_bc);
		bcImg.put(FEATURE_VOTE, R.drawable.feature_vote_bc);
		bcImg.put(LIBRARY_PAGE, R.drawable.library_bc);
		bcImg.put(LIBRARY_RESULTS_PAGE, R.drawable.library_bc);
		bcImg.put(CONTACT_RESULTS_PAGE, R.drawable.contact_bc);
	}
	
	@Override
	protected void configure() {
		//views and drawables for contact page 
		bind(Page.class).annotatedWith(Names.named("contact:index")).to(ContactPage.class);
		bind(Integer.class).annotatedWith(Names.named("contact:index_img")).toInstance(R.drawable.contact);
		bind(Integer.class).annotatedWith(Names.named("contact:index_bc")).toInstance(R.drawable.contact_bc);
		
		//views and drawables for library page
		bind(Page.class).annotatedWith(Names.named("library:index")).to(LibraryPage.class);
		bind(Integer.class).annotatedWith(Names.named("library:index_img")).toInstance(R.drawable.library);
		bind(Integer.class).annotatedWith(Names.named("library:index_bc")).toInstance(R.drawable.library_bc);
		
		bind(Page.class).annotatedWith(Names.named("results:index")).to(ResultsReleasePage.class);
		bind(Page.class).annotatedWith(Names.named("splash")).to(Splash.class);
		bind(Page.class).annotatedWith(Names.named("home:index")).to(HomePage.class);
		
		//Unimplemented pages and default images
		bind(Page.class).annotatedWith(Named.class).to(UnimplementedPage.class);
		bind(Integer.class).annotatedWith(Named.class).toInstance(R.drawable.android_button);
	}

	
	protected static Map<String,Integer> img 
		= new HashMap<String,Integer>();
	static {
		img.put(CONTACT_PAGE, R.drawable.contact);
		img.put(LIBRARY_PAGE, R.drawable.library);
	}
	
	public static String getName(Class <? extends Page> pageClass)
	{
		return (pages.inverse().get(pageClass));
	}
	
	public static int getBCImg(String viewName)
	{
		if (bcImg.containsKey(viewName))
		{
			return bcImg.get(viewName);
		}
		else 
		{
			return R.drawable.android_button;
		}
	}
	
	public static int getImg(String viewName)
	{
		if (img.containsKey(viewName))
		{
			return img.get(viewName);
		}
		else 
		{
			return R.drawable.android_button;
		}
	}
	
	public static Class<? extends Page> getPageClass(String s)
	{
		return pages.get(s);
	}
}
