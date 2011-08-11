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
import org.mollyproject.android.view.apps.home.HomePage;
import org.mollyproject.android.view.apps.library.LibraryPage;
import org.mollyproject.android.view.apps.library.LibraryResultsPage;
import org.mollyproject.android.view.apps.map.PlacesPage;
import org.mollyproject.android.view.apps.podcasts.IndividualPodcastPage;
import org.mollyproject.android.view.apps.podcasts.PodcastsCategoryPage;
import org.mollyproject.android.view.apps.podcasts.PodcastsPage;
import org.mollyproject.android.view.apps.results_release.ResultsReleasePage;
import org.mollyproject.android.view.apps.search.SearchPage;
import org.mollyproject.android.view.apps.weather.WeatherPage;

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
	public static String SEARCH_PAGE = "search:index";
	public static String WEATHER_PAGE = "weather:index";
	public static String PODCAST_PAGE = "podcasts:index";
	public static String PODCAST_CATEGORY_PAGE = "podcasts:category";
	public static String INDIVIDUAL_PODCAST_PAGE = "podcasts:podcast";
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
		pages.put(SEARCH_PAGE, SearchPage.class);
		pages.put(LIBRARY_RESULTS_PAGE, LibraryResultsPage.class);
		pages.put(CONTACT_RESULTS_PAGE, ContactResultsPage.class);
		pages.put(WEATHER_PAGE, WeatherPage.class);
		pages.put(PODCAST_PAGE, PodcastsPage.class);
		pages.put(PODCAST_CATEGORY_PAGE, PodcastsCategoryPage.class);
		pages.put(INDIVIDUAL_PODCAST_PAGE, IndividualPodcastPage.class);
	}
	
	protected static Map<String,Integer> bcImg 
					= new HashMap<String,Integer>();
	
	@Override
	protected void configure() {
		//views and drawables for contact page 
		bind(Page.class).annotatedWith(Names.named(CONTACT_PAGE)).to(ContactPage.class);
		bind(Integer.class).annotatedWith(Names.named(CONTACT_PAGE+"_img")).toInstance(R.drawable.contact);
		bind(Integer.class).annotatedWith(Names.named(CONTACT_PAGE+"_bc")).toInstance(R.drawable.contact_bc);
		bind(Page.class).annotatedWith(Names.named(CONTACT_RESULTS_PAGE)).to(ContactResultsPage.class);
		//views and drawables for library page
		bind(Page.class).annotatedWith(Names.named(LIBRARY_PAGE)).to(LibraryPage.class);
		bind(Integer.class).annotatedWith(Names.named(LIBRARY_PAGE+"_img")).toInstance(R.drawable.library);
		bind(Integer.class).annotatedWith(Names.named(LIBRARY_PAGE+"_bc")).toInstance(R.drawable.library_bc);
		bind(Page.class).annotatedWith(Names.named(LIBRARY_RESULTS_PAGE)).to(LibraryResultsPage.class);
		
		bind(Page.class).annotatedWith(Names.named(PLACES_PAGE)).to(PlacesPage.class);
		bind(Integer.class).annotatedWith(Names.named(PLACES_PAGE+"_img")).toInstance(R.drawable.places);
		bind(Integer.class).annotatedWith(Names.named(PLACES_PAGE+"_bc")).toInstance(R.drawable.places_bc);

		//views and drawables for weather page
		bind(Page.class).annotatedWith(Names.named(WEATHER_PAGE)).to(WeatherPage.class);
		bind(Integer.class).annotatedWith(Names.named(WEATHER_PAGE+"_img")).toInstance(R.drawable.weather);
		bind(Integer.class).annotatedWith(Names.named(WEATHER_PAGE+"_bc")).toInstance(R.drawable.weather_bc);
		
		//search page
		bind(Page.class).annotatedWith(Names.named(SEARCH_PAGE)).to(SearchPage.class);
		
		//podcasts page
		bind(Page.class).annotatedWith(Names.named(PODCAST_PAGE)).to(PodcastsPage.class);
		bind(Integer.class).annotatedWith(Names.named(PODCAST_PAGE+"_img"))
															.toInstance(R.drawable.podcasts);
		bind(Integer.class).annotatedWith(Names.named(PODCAST_PAGE+"_bc"))
															.toInstance(R.drawable.podcasts_bc);
		
		bind(Page.class).annotatedWith(Names.named(PODCAST_CATEGORY_PAGE)).to(PodcastsCategoryPage.class);
		
		bind(Page.class).annotatedWith(Names.named(INDIVIDUAL_PODCAST_PAGE)).to(IndividualPodcastPage.class);
		
		bind(Page.class).annotatedWith(Names.named(RESULTS_PAGE)).to(ResultsReleasePage.class);
		bind(Page.class).annotatedWith(Names.named("splash")).to(Splash.class);
		bind(Page.class).annotatedWith(Names.named(HOME_PAGE)).to(HomePage.class);
		
		//Unimplemented pages and default images
		bind(Page.class).annotatedWith(Named.class).to(UnimplementedPage.class);
		
		bind(Integer.class).annotatedWith(Names.named("audio")).toInstance(R.drawable.list_audio);
		bind(Integer.class).annotatedWith(Names.named("video")).toInstance(R.drawable.list_video);
		
		bind(Integer.class).annotatedWith(Names.named("default_white")).toInstance(R.drawable.unavail_w_1);
		bind(Integer.class).annotatedWith(Names.named("default_blue")).toInstance(R.drawable.unavail_b_2);
		bind(Integer.class).annotatedWith(Named.class).toInstance(R.drawable.unavail_b_2);
	}
	
	public static String getName(Class <? extends Page> pageClass)
	{
		return (pages.inverse().get(pageClass));
	}
	
	public static Class<? extends Page> getPageClass(String s)
	{
		return pages.get(s);
	}
}
