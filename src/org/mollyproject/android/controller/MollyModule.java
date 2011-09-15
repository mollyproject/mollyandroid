package org.mollyproject.android.controller;

import org.mollyproject.android.R;
import org.mollyproject.android.Splash;
import org.mollyproject.android.view.apps.*;
import org.mollyproject.android.view.apps.contact.ContactPage;
import org.mollyproject.android.view.apps.contact.ContactResultsPage;
import org.mollyproject.android.view.apps.favourites.FavouritesPage;
import org.mollyproject.android.view.apps.home.HomePage;
import org.mollyproject.android.view.apps.library.LibraryBookResultPage;
import org.mollyproject.android.view.apps.library.LibraryPage;
import org.mollyproject.android.view.apps.library.LibraryResultsPage;
import org.mollyproject.android.view.apps.places.PlacesNearbyDetailPage;
import org.mollyproject.android.view.apps.places.PlacesNearbyPage;
import org.mollyproject.android.view.apps.places.PlacesPage;
import org.mollyproject.android.view.apps.places.entity.PlacesEntityDirectionsPage;
import org.mollyproject.android.view.apps.places.entity.PlacesEntityNearbyDetailPage;
import org.mollyproject.android.view.apps.places.entity.PlacesEntityNearbyList;
import org.mollyproject.android.view.apps.places.entity.PlacesResultsPage;
import org.mollyproject.android.view.apps.podcasts.IndividualPodcastPage;
import org.mollyproject.android.view.apps.podcasts.PodcastsCategoryPage;
import org.mollyproject.android.view.apps.podcasts.PodcastsPage;
import org.mollyproject.android.view.apps.results_release.ResultsReleasePage;
import org.mollyproject.android.view.apps.search.SearchPage;
import org.mollyproject.android.view.apps.status.ServiceStatusPage;
import org.mollyproject.android.view.apps.transport.TransportPage;
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
	public static String PLACES_NEARBY_DETAIL = "places:nearby-detail";
	public static String PLACES_ENTITY = "places:entity";
	public static String PLACES_ENTITY_NEARBY_LIST = "places:entity-nearby-list";
	public static String PLACES_ENTITY_NEARBY_DETAIL = "places:entity-nearby-detail";
	public static String PLACES_ENTITY_DIRECTIONS = "places:entity-directions";
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
	public static String TRANSPORT_PAGE = "transport:index";
	public static String STATUS_PAGE = "service-status:index";
	public static String PUBLIC_TRANSPORT = "transport:public-transport";
	public static String FAVOURITES = "favourites:index";
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
		bind(Page.class).annotatedWith(Names.named(PLACES_NEARBY)).to(PlacesNearbyPage.class);
		bind(Page.class).annotatedWith(Names.named(PLACES_NEARBY_DETAIL)).to(PlacesNearbyDetailPage.class);
		bind(Page.class).annotatedWith(Names.named(PLACES_ENTITY)).to(PlacesResultsPage.class);
		bind(Page.class).annotatedWith(Names.named(PLACES_ENTITY_NEARBY_LIST)).to(PlacesEntityNearbyList.class);
		bind(Page.class).annotatedWith(Names.named(PLACES_ENTITY_NEARBY_DETAIL)).to(PlacesEntityNearbyDetailPage.class);
		bind(Page.class).annotatedWith(Names.named(PLACES_ENTITY_DIRECTIONS)).to(PlacesEntityDirectionsPage.class);
		
		//views and drawables for weather page
		bind(Page.class).annotatedWith(Names.named(WEATHER_PAGE)).to(WeatherPage.class);
		bind(Integer.class).annotatedWith(Names.named(WEATHER_PAGE+"_img")).toInstance(R.drawable.weather);
		bind(Integer.class).annotatedWith(Names.named(WEATHER_PAGE+"_bc")).toInstance(R.drawable.weather_bc);
		
		//search page
		bind(Page.class).annotatedWith(Names.named(SEARCH_PAGE)).to(SearchPage.class);
		
		//podcasts pages
		bind(Page.class).annotatedWith(Names.named(PODCAST_PAGE)).to(PodcastsPage.class);
		bind(Integer.class).annotatedWith(Names.named(PODCAST_PAGE+"_img"))
															.toInstance(R.drawable.podcasts);
		bind(Integer.class).annotatedWith(Names.named(PODCAST_PAGE+"_bc"))
															.toInstance(R.drawable.podcasts_bc);
		
		bind(Page.class).annotatedWith(Names.named(PODCAST_CATEGORY_PAGE)).to(PodcastsCategoryPage.class);
		
		bind(Page.class).annotatedWith(Names.named(INDIVIDUAL_PODCAST_PAGE)).to(IndividualPodcastPage.class);
		
		//transport pages
		bind(Page.class).annotatedWith(Names.named(TRANSPORT_PAGE)).to(TransportPage.class);
		bind(Integer.class).annotatedWith(Names.named(TRANSPORT_PAGE+"_img")).toInstance(R.drawable.transport);
		
		//results release page
		bind(Page.class).annotatedWith(Names.named(RESULTS_PAGE)).to(ResultsReleasePage.class);
		bind(Integer.class).annotatedWith(Names.named(RESULTS_PAGE+"_img")).toInstance(R.drawable.results);
		bind(Integer.class).annotatedWith(Names.named(RESULTS_PAGE+"_bc")).toInstance(R.drawable.results_bc);
		
		//service status page and images
		bind(Page.class).annotatedWith(Names.named(STATUS_PAGE)).to(ServiceStatusPage.class);
		bind(Integer.class).annotatedWith(Names.named(STATUS_PAGE+"_img")).toInstance(R.drawable.service_status);
		bind(Integer.class).annotatedWith(Names.named(STATUS_PAGE+"_bc")).toInstance(R.drawable.service_status_bc);
		//temporary patch to display breadcrumbs:
		bind(Integer.class).annotatedWith(Names.named("service_status:index"+"_bc")).toInstance(R.drawable.service_status_bc);
		
		bind(Integer.class).annotatedWith(Names.named("up")).toInstance(R.drawable.service_up);
		bind(Integer.class).annotatedWith(Names.named("partial")).toInstance(R.drawable.service_partial);
		bind(Integer.class).annotatedWith(Names.named("unknown")).toInstance(R.drawable.service_unknown);
		bind(Integer.class).annotatedWith(Names.named("down")).toInstance(R.drawable.service_down);
		
		//favourites management
		bind(Page.class).annotatedWith(Names.named(FAVOURITES)).to(FavouritesPage.class);
		
		//Other non-content pages
		bind(Page.class).annotatedWith(Names.named("splash")).to(Splash.class);
		bind(Page.class).annotatedWith(Names.named(HOME_PAGE)).to(HomePage.class);

		//Unimplemented pages and default images
		bind(Page.class).annotatedWith(Named.class).to(UnimplementedPage.class);
		
		bind(Integer.class).annotatedWith(Names.named("audio")).toInstance(R.drawable.list_audio);
		bind(Integer.class).annotatedWith(Names.named("video")).toInstance(R.drawable.list_video);
		
		bind(Integer.class).annotatedWith(Names.named("default_white")).toInstance(R.drawable.unavail_w_1);
		bind(Integer.class).annotatedWith(Names.named("default_blue")).toInstance(R.drawable.unavail_b_7);
		bind(Integer.class).annotatedWith(Named.class).toInstance(R.drawable.unavail_b_7);
		
		//Lookups for weather icons. Warning: long
		bind(Integer.class).annotatedWith(Names.named("cloudy1")).toInstance(R.drawable.cloudy1);
		bind(Integer.class).annotatedWith(Names.named("cloudy1_night")).toInstance(R.drawable.cloudy1_night);
		bind(Integer.class).annotatedWith(Names.named("cloudy1_small")).toInstance(R.drawable.cloudy1_small);
		bind(Integer.class).annotatedWith(Names.named("cloudy1_night_small")).toInstance(R.drawable.cloudy1_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("cloudy2")).toInstance(R.drawable.cloudy2);
		bind(Integer.class).annotatedWith(Names.named("cloudy2_night")).toInstance(R.drawable.cloudy2_night);
		bind(Integer.class).annotatedWith(Names.named("cloudy2_small")).toInstance(R.drawable.cloudy2_small);
		bind(Integer.class).annotatedWith(Names.named("cloudy2_night_small")).toInstance(R.drawable.cloudy2_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("cloudy3")).toInstance(R.drawable.cloudy3);
		bind(Integer.class).annotatedWith(Names.named("cloudy3_night")).toInstance(R.drawable.cloudy3_night);
		bind(Integer.class).annotatedWith(Names.named("cloudy3_small")).toInstance(R.drawable.cloudy3_small);
		bind(Integer.class).annotatedWith(Names.named("cloudy3_night_small")).toInstance(R.drawable.cloudy3_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("cloudy4")).toInstance(R.drawable.cloudy4);
		bind(Integer.class).annotatedWith(Names.named("cloudy4_night")).toInstance(R.drawable.cloudy4_night);
		bind(Integer.class).annotatedWith(Names.named("cloudy4_small")).toInstance(R.drawable.cloudy4_small);
		bind(Integer.class).annotatedWith(Names.named("cloudy4_night_small")).toInstance(R.drawable.cloudy4_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("cloudy5")).toInstance(R.drawable.cloudy5);
		bind(Integer.class).annotatedWith(Names.named("cloudy5_small")).toInstance(R.drawable.cloudy5_small);
		
		bind(Integer.class).annotatedWith(Names.named("fog")).toInstance(R.drawable.fog);
		bind(Integer.class).annotatedWith(Names.named("fog_night")).toInstance(R.drawable.fog_night);
		bind(Integer.class).annotatedWith(Names.named("fog_small")).toInstance(R.drawable.fog_small);
		bind(Integer.class).annotatedWith(Names.named("fog_night_small")).toInstance(R.drawable.fog_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("hail")).toInstance(R.drawable.hail);
		bind(Integer.class).annotatedWith(Names.named("hail_small")).toInstance(R.drawable.hail_small);
		
		bind(Integer.class).annotatedWith(Names.named("light_rain")).toInstance(R.drawable.light_rain);
		bind(Integer.class).annotatedWith(Names.named("light_rain_small")).toInstance(R.drawable.light_rain_small);
		
		bind(Integer.class).annotatedWith(Names.named("mist")).toInstance(R.drawable.mist);
		bind(Integer.class).annotatedWith(Names.named("mist_night")).toInstance(R.drawable.mist_night);
		bind(Integer.class).annotatedWith(Names.named("mist_small")).toInstance(R.drawable.mist_small);
		bind(Integer.class).annotatedWith(Names.named("mist_night_small")).toInstance(R.drawable.mist_night_small);
	
		bind(Integer.class).annotatedWith(Names.named("overcast")).toInstance(R.drawable.overcast);
		bind(Integer.class).annotatedWith(Names.named("overcast_small")).toInstance(R.drawable.overcast_small);
		
		bind(Integer.class).annotatedWith(Names.named("shower1")).toInstance(R.drawable.shower1);
		bind(Integer.class).annotatedWith(Names.named("shower1_night")).toInstance(R.drawable.shower1_night);
		bind(Integer.class).annotatedWith(Names.named("shower1_small")).toInstance(R.drawable.shower1_small);
		bind(Integer.class).annotatedWith(Names.named("shower1_night_small")).toInstance(R.drawable.shower1_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("shower2")).toInstance(R.drawable.shower2);
		bind(Integer.class).annotatedWith(Names.named("shower2_night")).toInstance(R.drawable.shower2_night);
		bind(Integer.class).annotatedWith(Names.named("shower2_small")).toInstance(R.drawable.shower2_small);
		bind(Integer.class).annotatedWith(Names.named("shower2_night_small")).toInstance(R.drawable.shower2_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("shower3")).toInstance(R.drawable.shower3);
		bind(Integer.class).annotatedWith(Names.named("shower3_small")).toInstance(R.drawable.shower3_small);
		
		bind(Integer.class).annotatedWith(Names.named("sleet")).toInstance(R.drawable.sleet);
		bind(Integer.class).annotatedWith(Names.named("sleet_small")).toInstance(R.drawable.sleet_small);
		
		bind(Integer.class).annotatedWith(Names.named("snow1")).toInstance(R.drawable.snow1);
		bind(Integer.class).annotatedWith(Names.named("snow1_night")).toInstance(R.drawable.snow1_night);
		bind(Integer.class).annotatedWith(Names.named("snow1_small")).toInstance(R.drawable.snow1_small);
		bind(Integer.class).annotatedWith(Names.named("snow1_night_small")).toInstance(R.drawable.snow1_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("snow2")).toInstance(R.drawable.snow2);
		bind(Integer.class).annotatedWith(Names.named("snow2_night")).toInstance(R.drawable.snow2_night);
		bind(Integer.class).annotatedWith(Names.named("snow2_small")).toInstance(R.drawable.snow2_small);
		bind(Integer.class).annotatedWith(Names.named("snow2_night_small")).toInstance(R.drawable.snow2_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("snow3")).toInstance(R.drawable.snow3);
		bind(Integer.class).annotatedWith(Names.named("snow3_night")).toInstance(R.drawable.snow3_night);
		bind(Integer.class).annotatedWith(Names.named("snow3_small")).toInstance(R.drawable.snow3_small);
		bind(Integer.class).annotatedWith(Names.named("snow3_night_small")).toInstance(R.drawable.snow3_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("snow4")).toInstance(R.drawable.snow4);
		bind(Integer.class).annotatedWith(Names.named("snow4_small")).toInstance(R.drawable.snow4_small);
		
		bind(Integer.class).annotatedWith(Names.named("snow5")).toInstance(R.drawable.snow5);
		bind(Integer.class).annotatedWith(Names.named("snow5_small")).toInstance(R.drawable.snow5_small);
		
		bind(Integer.class).annotatedWith(Names.named("sunny")).toInstance(R.drawable.sunny);
		bind(Integer.class).annotatedWith(Names.named("sunny_night")).toInstance(R.drawable.sunny_night);
		bind(Integer.class).annotatedWith(Names.named("sunny_small")).toInstance(R.drawable.sunny_small);
		bind(Integer.class).annotatedWith(Names.named("sunny_night_small")).toInstance(R.drawable.sunny_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("tstorm1")).toInstance(R.drawable.tstorm1);
		bind(Integer.class).annotatedWith(Names.named("tstorm1_night")).toInstance(R.drawable.tstorm1_night);
		bind(Integer.class).annotatedWith(Names.named("tstorm1_small")).toInstance(R.drawable.tstorm1_small);
		bind(Integer.class).annotatedWith(Names.named("tstorm1_night_small")).toInstance(R.drawable.tstorm1_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("tstorm2")).toInstance(R.drawable.tstorm2);
		bind(Integer.class).annotatedWith(Names.named("tstorm2_night")).toInstance(R.drawable.tstorm2_night);
		bind(Integer.class).annotatedWith(Names.named("tstorm2_small")).toInstance(R.drawable.tstorm2_small);
		bind(Integer.class).annotatedWith(Names.named("tstorm2_night_small")).toInstance(R.drawable.tstorm2_night_small);
		
		bind(Integer.class).annotatedWith(Names.named("tstorm3")).toInstance(R.drawable.tstorm3);
		bind(Integer.class).annotatedWith(Names.named("tstorm3_small")).toInstance(R.drawable.tstorm3_small);
		
	}
}
