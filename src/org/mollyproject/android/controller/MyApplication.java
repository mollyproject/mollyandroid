package org.mollyproject.android.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.view.apps.Page;

import roboguice.application.RoboApplication;

import com.google.common.collect.ArrayListMultimap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
/**
 * this customised application state replaces the default one, its primary use is for caching (in case a page is resumed) and dependency injection
 * @author famanson
 *
 */
public class MyApplication extends RoboApplication {
	/**
	 * a reference to the Router
	 */
	public static Router router = null;
	/**
	 * cached value for a map query
	 */
	public static String mapQuery;
	/**
	 * cached value for a contact query
	 */
	public static String[] contactQuery = new String[2];
	/**
	 * cached value for a general search query
	 */
	public static String[] generalQuery = new String[2];
	/**
	 * cached value for the arguments for the places
	 */
	public static String[] placesArgs = new String[2];
	/**
	 * cached value for the boolean that identifies a property of a place
	 */
	public static boolean isNearbyEntity = false;
	/**
	 * cached value for the slug ("secondary identifier") of a nearby place page
	 */
	public static String placesNearbySlug = new String();
	/**
	 * cached value for the book control number
	 */
	public static String bookControlNumber;
	/**
	 * cached value for the podcasts output
	 */
	public static List<Map<String,String>> podcastsOutput;
	/**
	 * cached value for the library search query
	 */
	public static Map<String,String> libraryQuery = null;
	/**
	 * cached value for the locator (i.e. the view-name of the last opened page)
	 */
	public static String locator;
	/**
	 * cached value for the podcasts slug
	 */
	public static String podcastsSlug = new String();
	/**
	 * cached value for a single podcast slug
	 */
	public static String indPodcastSlug = new String();
	/**
	 * the cache for the library results
	 */
	public static ArrayListMultimap<String,JSONObject> libraryCache = ArrayListMultimap.create();
	/**
	 * boolean flag to indicate whether the application state is destroyed
	 */
	public static boolean destroyed = true;
	/**
	 * the injector, for injecting dependencies
	 */
	public static Injector injector;
	/**
	 * the last transport tab the user was on
	 */
	public static int lastTransportTab;
	/**
	 * cache for the podcast icons
	 */
	public static Map<String,Bitmap> podcastIconsCache = new HashMap<String,Bitmap>();
	/**
	 * cache for transport data
	 */
	public static JSONObject transportCache = null;
	/**
	 * the last tag of the transport tab
	 */
	public static String lastTransportTag = new String();
	/**
	 * the transportation mean chosen
	 */
	public static String transportation = new String();
	/**
	 * the current location as specified by the server under JSON format, the fields of interest are: String name, String accuracy and JSONArray history
	 */
	public static JSONObject currentLocation = null;
	/**
	 * the value of the CSRF token, should be set once per session in CookieManager.storeCookies()
	 */
	public static String csrfToken = null;
	/**
	 * all available apps for the current session
	 */
	public static JSONArray availableApps = null;
	/**
	 * name of the preferences file
	 */
	public static final String PREFS_NAME = "MyPrefsFile";
	/**
	 * indicates the current Molly app the user is using
	 */
	public static String currentApp;
	/**
	 * user's favourite place URL
	 */
	public static String favouriteURL;
	/**
	 * the map data
	 */
	public static String mapData;
	/**
	 * slug for news feed
	 */
	public static String feedSlug = new String();
	/**
	 * slug for webcam
	 */
	public static String webcamSlug = new String();
	/**
	 * cache for the webcam data
	 */
	public static JSONObject webcamCache = null;
	/**
	 * the current oAuth token
	 */
	public static String oauthToken = new String();
	/**
	 * the current oAuth identifier
	 */
	public static String oauthVerifier = null;
	/**
	 * the WebLearn state - whether the user is authorised in WebLearn
	 */
	public static int weblearnState;
	/**
	 * slug for WebLearn announcement
	 */
	public static String weblearnAnnouncementSlug = new String();
	/**
	 * slug for WebLearn sign-ups
	 */
	public static String weblearnSignupSlug = new String();
	/**
	 * WebLearn event ID
	 */
	public static String weblearnEventId = new String();
	/**
	 * the last app chosen in the new Search function
	 */
	public static int lastSearchApp;
	/**
	 * the date format: EEE, d MMM yyyy HH:mm:ss Z
	 */
	public static DateFormat defaultDateFormat = new SimpleDateFormat
			("EEE, d MMM yyyy HH:mm:ss Z");
	/**
	 * the date format: d MMM yyyy
	 */
	public static DateFormat myDateFormat = new SimpleDateFormat
			("d MMM yyyy");
	/**
	 * the date format: yyyy-MM-d HH:mm:ss
	 */
	public static DateFormat podcastDateFormat = new SimpleDateFormat
			("yyyy-MM-d HH:mm:ss");
	/**
	 * the date format: yyyy-MM-d HH:mm:ss Z
	 */
	public static DateFormat trainDateFormat = new SimpleDateFormat
			("yyyy-MM-d HH:mm:ss Z");
	/**
	 * yyyy-MM-d HH:mm:ss Z
	 */
	public static DateFormat updateDateFormat = new SimpleDateFormat
			("yyyy-MM-d HH:mm:ss Z");
	/**
	 * the hour format: HH:mm:ss
	 */
	public final static DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
	/**
	 * the hour format: HH:mm
	 */
	public final static DateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm");
	/**
	 * the date format: d MMM
	 */
	public static DateFormat dateMonthFormat = new SimpleDateFormat
			("d MMM");
	
	/*public boolean isOnline() {
		 return ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))
				 				.getActiveNetworkInfo().isConnectedOrConnecting();
	}*/
	
	/**
	 * convert a (podcast) icon URL into valid file names 
	 * @param logoURL the URL to the icon
	 * @return a file name of the icon
	 */
	private String getLogoFileName(String logoURL)
	{
		return logoURL.replace("/", "-");
	}
	
	/**
	 * download the bitmap of an icon and store it, assuming the bitmpa is not already stored
	 * @param logoURL the URL to the icon
	 * @param bitmap a Bitmap object
	 */
	public synchronized void updatePodcastIconsCache(String logoURL, Bitmap bitmap)
	{
		try {
	    	FileOutputStream fos = openFileOutput(getLogoFileName(logoURL), 
					Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/**
	 * check if an icon has been downloaded and stored already, each icon is identified by its URL
	 * @param logoURL the URL to the icon
	 * @return whether the icon has been downloaded and stored
	 */
	public synchronized boolean hasPodcastIcon(String logoURL)
	{
		File file = getFileStreamPath(getLogoFileName(logoURL));
		if (file.exists())
		{
			Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
			podcastIconsCache.put(logoURL, bitmap);
		}
		return file.exists();
	}
	/**
	 * get the icon from the cache
	 * @param logoURL the URL of the original icon
	 * @return the Bitmap of the cached icon
	 */
	public synchronized Bitmap getIcon(String logoURL)
	{
		System.out.println("podcast image cache");
		try {
			FileInputStream fIn = openFileInput(getLogoFileName(logoURL));
			Bitmap bitmap = BitmapFactory.decodeStream(fIn);
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null; //should not reach here
	}
	
	/**
	 * given a string bound to a subclass of page, return that class 
	 * @param the bound string (most possibly the view-name of the page)
	 * @return the subclass bound to that string
	 */
	public static Class <? extends Page> getPageClass(String s)
	{
		return (injector.getInstance(Key.get(Page.class, Names.named(s))).getClass());
	}
	
	/**
	 * given a string bound to a constant value (integer), return the value itself, in this case the value identifies a piece of Android resource (e.g. an image) 
	 * @param viewName the bound string
	 * @return the integer that identifies a piece of resource
	 */
	public static int getImgResourceId(String viewName)
	{
		return (injector.getInstance(Key.get(Integer.class, Names.named(viewName))));
	}
	/**
	 * sets up the application module as part of the dependency injection scheme
	 */
	@Override
    protected void addApplicationModules(List<Module> modules) {
        MollyModule module = new MollyModule();
		modules.add(module);
        injector = Guice.createInjector(module);
    }
}


















