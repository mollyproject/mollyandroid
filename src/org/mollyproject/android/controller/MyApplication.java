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

public class MyApplication extends RoboApplication {
	//Intermediate stage for caching data in a session
	public static Router router = null; //make router null at first and check for it every time to avoid null pointers
	public static String mapQuery;
	public static String[] contactQuery = new String[2]; //arbitrary array of contact query, should expect the actual query and the required medium
	public static String[] generalQuery = new String[2];
	public static String[] placesArgs = new String[2];
	public static boolean isNearbyEntity = false;
	public static String placesNearbySlug = new String();
	public static String bookControlNumber;
	public static List<Map<String,String>> podcastsOutput;
	public static Map<String,String> libraryQuery = null;
	public static String locator;
	public static String podcastsSlug = new String();
	public static String indPodcastSlug = new String();
	public static ArrayListMultimap<String,JSONObject> libraryCache = ArrayListMultimap.create();
	public static boolean destroyed = true;
	public static Injector injector;
	public static int lastTransportTab;
	public static Map<String,Bitmap> podcastIconsCache = new HashMap<String,Bitmap>();
	public static JSONObject transportCache = null;
	public static String lastTransportTag = new String();
	public static String transportation = new String();
	public static JSONObject currentLocation = null; //the fields of interest are: String name, String accuracy and JSONArray history 
	public static String csrfToken = null; //should be set once per session in CookieManager.storeCookies()
	public static JSONArray availableApps = null; //all available apps for the current session
	public static final String PREFS_NAME = "MyPrefsFile";
	public static String currentApp;
	public static String favouriteURL;
	public static String mapData;
	public static String feedSlug = new String();
	public static String webcamSlug = new String();
	public static JSONObject webcamCache = null;
	public static String oauthToken = new String();
	public static String oauthVerifier = null;
	public static int weblearnState;
	public static String weblearnAnnouncementSlug = new String();
	public static String weblearnSignupSlug = new String();
	public static String weblearnEventId = new String();
	public static int lastSearchApp;
	
	public static DateFormat defaultDateFormat = new SimpleDateFormat
			("EEE, d MMM yyyy HH:mm:ss Z");
	public static DateFormat myDateFormat = new SimpleDateFormat
			("d MMM yyyy");
	public static DateFormat podcastDateFormat = new SimpleDateFormat
			("yyyy-MM-d HH:mm:ss");
	
	public static DateFormat trainDateFormat = new SimpleDateFormat
			("yyyy-MM-d HH:mm:ss Z");
	
	public static DateFormat updateDateFormat = new SimpleDateFormat
			("yyyy-MM-d HH:mm:ss Z");
	
	public final static DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
	
	public final static DateFormat hourMinuteFormat = new SimpleDateFormat("HH:mm");
	
	public static DateFormat dateMonthFormat = new SimpleDateFormat
			("d MMM");
	
	public boolean isOnline() {
		 return ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))
				 				.getActiveNetworkInfo().isConnectedOrConnecting();
	}
	
	private String getLogoFileName(String logoURL)
	{
		return logoURL.replace("/", "-");
	}
	
	public synchronized void updatePodcastIconsCache(String logoURL, Bitmap bitmap)
	{
		//bitmap file not stored
		try {
	    	FileOutputStream fos = openFileOutput(getLogoFileName(logoURL), 
					Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
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
	
	public static Class <? extends Page> getPageClass(String s)
	{
		return (injector.getInstance(Key.get(Page.class, Names.named(s))).getClass());
	}
	
	public static int getImgResourceId(String viewName)
	{
		return (injector.getInstance(Key.get(Integer.class, Names.named(viewName))));
	}
	
	@Override
    protected void addApplicationModules(List<Module> modules) {
        MollyModule module = new MollyModule();
		modules.add(module);
        injector = Guice.createInjector(module);
    }
}


















