package org.mollyproject.android.controller;

import org.mollyproject.android.R;
import org.mollyproject.android.Splash;
import org.mollyproject.android.view.apps.*;
import org.mollyproject.android.view.apps.contact.ContactPage;
import org.mollyproject.android.view.apps.contact.ContactResultsPage;
import org.mollyproject.android.view.apps.home.HomePage;
import org.mollyproject.android.view.apps.library.LibraryBookResultPage;
import org.mollyproject.android.view.apps.library.LibraryPage;
import org.mollyproject.android.view.apps.library.LibraryResultsPage;
import org.mollyproject.android.view.apps.map.PlacesNearbyPage;
import org.mollyproject.android.view.apps.map.PlacesPage;
import org.mollyproject.android.view.apps.map.PlacesResultsPage;
import org.mollyproject.android.view.apps.podcasts.IndividualPodcastPage;
import org.mollyproject.android.view.apps.podcasts.PodcastsCategoryPage;
import org.mollyproject.android.view.apps.podcasts.PodcastsPage;
import org.mollyproject.android.view.apps.results_release.ResultsReleasePage;
import org.mollyproject.android.view.apps.search.SearchPage;
import org.mollyproject.android.view.apps.weather.WeatherPage;

import com.google.inject.AbstractModule;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
public class MollyModule extends AbstractModule {
	//Config class, holds constant strings and bindings, etc for use in MyApplication and various other places
	
	public static String HOME_PAGE = "home:index";
	public static String RESULTS_PAGE = "results:index";
	public static String PLACES_PAGE = "places:index";
	public static String PLACES_NEARBY = "places:nearby-list";
	public static String PLACES_ENTITY = "places:entity";
	public static String CONTACT_PAGE = "contact:index";
	public static String FEATURE_VOTE = "feature_vote:index";
	public static String FEEDBACK_PAGE = "feedback:index";
	public static String LIBRARY_PAGE = "library:index";
	public static String LIBRARY_RESULTS_PAGE = "library:search";
	public static String LIBRARY_BOOK_RESULT_PAGE = "library:item-detail";
	public static String CONTACT_RESULTS_PAGE = "contact:result_list";
	public static String SEARCH_PAGE = "search:index";
	public static String WEATHER_PAGE = "weather:index";
	public static String PODCAST_PAGE = "podcasts:index";
	public static String PODCAST_CATEGORY_PAGE = "podcasts:category";
	public static String INDIVIDUAL_PODCAST_PAGE = "podcasts:podcast";
	public static String SPLASH = "splash";
	
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
		bind(Page.class).annotatedWith(Names.named(LIBRARY_BOOK_RESULT_PAGE)).to(LibraryBookResultPage.class);
		
		bind(Page.class).annotatedWith(Names.named(PLACES_PAGE)).to(PlacesPage.class);
		bind(Integer.class).annotatedWith(Names.named(PLACES_PAGE+"_img")).toInstance(R.drawable.places);
		bind(Integer.class).annotatedWith(Names.named(PLACES_PAGE+"_bc")).toInstance(R.drawable.places_bc);

		bind(Page.class).annotatedWith(Names.named(PLACES_ENTITY)).to(PlacesResultsPage.class);
		
		bind(Page.class).annotatedWith(Names.named(PLACES_NEARBY)).to(PlacesNearbyPage.class);
		
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
		bind(Integer.class).annotatedWith(Names.named(RESULTS_PAGE+"_img")).toInstance(R.drawable.results);
		bind(Integer.class).annotatedWith(Names.named(RESULTS_PAGE+"_bc")).toInstance(R.drawable.results_bc);
		
		bind(Page.class).annotatedWith(Names.named("splash")).to(Splash.class);
		bind(Page.class).annotatedWith(Names.named(HOME_PAGE)).to(HomePage.class);

		//Unimplemented pages and default images
		bind(Page.class).annotatedWith(Named.class).to(UnimplementedPage.class);
		
		bind(Integer.class).annotatedWith(Names.named("audio")).toInstance(R.drawable.list_audio);
		bind(Integer.class).annotatedWith(Names.named("video")).toInstance(R.drawable.list_video);
		
		bind(Integer.class).annotatedWith(Names.named("default_white")).toInstance(R.drawable.unavail_w_1);
		bind(Integer.class).annotatedWith(Names.named("default_blue")).toInstance(R.drawable.unavail_b_7);
		bind(Integer.class).annotatedWith(Named.class).toInstance(R.drawable.unavail_b_7);
	}
}
