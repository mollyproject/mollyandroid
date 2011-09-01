package org.mollyproject.android.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

    /**
     * CookieManager is a simple utilty for handling cookies when working
     * with java.net.URL and java.net.URLConnection
     * objects.
     * 
     * 
     *     @author Ian Brown
     *     
     *     Original code by Ian Brown - I customised it so that everything
     *     is in JSON format and stored nicely in a text file
     **/

public class CookieManager {
    
	private final static String COOKIESFILE = "cookiesFile.txt";
	protected File cookiesFile;
	//protected Context context;
	protected MyApplication myApp;
	protected JSONObject jsonCookies; //store the cookies in JSON format
	protected BasicCookieStore basicCookies;
	
    private static final String PATH = "Path";
    private static final String DOMAIN = "domain";
    private static final String VALUE = "value";
    private static final String NAME = "name";
    private static final String NULL = "/";
    private static final String EXPIRES = "expires";
    private static final String DATE_FORMAT_WITHOUT_TIMEZONE = "EEE MMM dd hh:mm:ss yyyy";
    private DateFormat dateFormatWithoutTimeZone;

    public CookieManager(MyApplication myApp) throws IOException, JSONException, ParseException {
    	this.myApp = myApp;
    	dateFormatWithoutTimeZone = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
    	jsonCookies = new JSONObject();
    	basicCookies = new BasicCookieStore();
    	cookiesFile = myApp.getFileStreamPath(COOKIESFILE);
    	//check if file exists
    	if (cookiesFile.exists())
    	{
	    	//check whether the cookies already exist in the private storage
	    	String recoveredJSONStr = readCookiesFromFile();
	    	System.out.println("Read from file "+recoveredJSONStr);
	    	
	    	if (recoveredJSONStr != null)
	    	{
	    		//there is something in the file, if the JSON text is not in the 
	    		//right format an exception will be thrown
	    		jsonCookies = new JSONObject(recoveredJSONStr);
	    		
	    		//have json data, now construct the cookie store from it
	    		basicCookies = getNewCookieStore();
	    	}
    	}
    	//the else case is dealt with by doing a storeCookies() call in Router 
    	
    }    
    
    public void storeCookies(CookieStore cookieStore) throws JSONException, IOException
    {   
    	//this should only be called when:
    	//1. this is the first request made
    	//2. the session id is added to the cookies. In this case, all other fields are unchanged
    	
    	System.out.println("CookieMgr store: " + basicCookies.getCookies());
    	System.out.println("Client store: "+ cookieStore.getCookies());
    	
    	//clear all expired cookies
    	basicCookies.clearExpired(new Date());
    	
    	//if the cookies stored in the app has expired (cleared after the function call above)
    	//or needs to update, then assign to the new cookie store
    	if (basicCookies.getCookies().size() < cookieStore.getCookies().size())
    	{
	    	basicCookies = (BasicCookieStore) cookieStore;
	    	jsonCookies = new JSONObject();
			List<Cookie> cookieList = cookieStore.getCookies();
			for (int i = 0; i < cookieList.size(); i++) {
				Cookie cookie = cookieList.get(i);
		    	JSONObject jsonCookie = new JSONObject();    	
		    	//put the cookie found in the JSON mappings
		    	jsonCookie.put(EXPIRES, dateFormatWithoutTimeZone.format(cookie.getExpiryDate()));
		    	jsonCookie.put(PATH,NULL);
		    	jsonCookie.put(DOMAIN, Router.mOX);
		    	jsonCookie.put(VALUE,cookie.getValue());
		    	jsonCookie.put(NAME,cookie.getName());
		    	jsonCookies.put(cookie.getName(), jsonCookie);
			}
			System.out.println("Stored Cookies from client: "+jsonCookies);
			writeCookiesToFile();
    	}
    	MyApplication.csrfToken = getCSRFToken();
    }
    
    public BasicCookieStore getCookieStore()
    {
    	return basicCookies;
    }
    
    public BasicCookieStore getNewCookieStore() throws JSONException, ParseException
    {
    	//reconstruct a totally new basic cookie store from the json data
    	BasicCookieStore basicCookieStore = new BasicCookieStore();
    	
		//append the cookies part with Set-cookies as the header
		Iterator<String> cookieNames = jsonCookies.keys();
		while(cookieNames.hasNext()) {
			//process the cookies one by one
		    String cookieName = cookieNames.next();
		    
		    //pick a cookie
		    JSONObject cookie = (JSONObject) jsonCookies.get(cookieName);
	    	System.out.println("Cookie Name, Set cookie "+cookie);
	    	
	    	//construct a BasicClientCookie from the json data 
			BasicClientCookie basicCookie = new BasicClientCookie(cookieName,cookie.getString(VALUE));
			System.out.println(cookie.getString(EXPIRES));
			//System.out.println(dateFormat.parse(cookie.getString(EXPIRES)));
			basicCookie.setExpiryDate(dateFormatWithoutTimeZone.parse(cookie.getString(EXPIRES)));
			basicCookie.setDomain("dev.m.ox.ac.uk");
			basicCookie.setPath("/");
			
			//add to the (basic client) cookie store
			basicCookieStore.addCookie(basicCookie);
		}
		return basicCookieStore;
    }
    
    public String getCSRFToken() throws JSONException
    {
    	System.out.println((String) ((JSONObject) jsonCookies.get("csrftoken")).getString("value"));
    	return (String) ((JSONObject) jsonCookies.get("csrftoken")).getString("value");
    }
    
    public String readCookiesFromFile() throws IOException, JSONException
    {
    	//Assuming the file exists
    	FileInputStream fIn = myApp.openFileInput(COOKIESFILE);
        InputStreamReader isr = new InputStreamReader(fIn);
        char[] inputBuffer = new char[1024];
        isr.read(inputBuffer);
        String readString = new String(inputBuffer);
        System.out.println("CookieManager, read from file: "+readString);
        if (readString.length() > 0)
        {   
        	return readString;
        }
    	return null;
    }
    
    public void writeCookiesToFile() throws IOException
    {
    	File dir = myApp.getFilesDir();
    	File file = new File(dir, COOKIESFILE);
    	System.out.println("File deleted "+file.delete());
    	FileOutputStream fos = myApp.openFileOutput(COOKIESFILE, 
				Context.MODE_PRIVATE);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		osw.write(jsonCookies.toString());		
		osw.flush();
		osw.close();
    }
}