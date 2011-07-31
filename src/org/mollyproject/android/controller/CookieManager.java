package org.mollyproject.android.controller;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

import org.json.*;
import org.mollyproject.android.jsoncookie.Cookie;

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
	protected JSONObject cookies; //store the cookies in JSON format	
    
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String PATH = "Path";
    private static final String EXPIRES = "expires";
    private static final String DATE_FORMAT = "EEE, dd-MMM-yyyy hh:mm:ss z";
    private static final String SET_COOKIE_SEPARATOR="; ";
    private static final String COOKIE = "Cookie";
    
    private static final char NAME_VALUE_SEPARATOR = '=';
    private DateFormat dateFormat;

    public CookieManager(MyApplication myApp) throws IOException, JSONException {
    	//this.context = context;
    	setApp(myApp);
    	cookies = new JSONObject();
    	
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
	    		cookies = new JSONObject(recoveredJSONStr);
	    	}
    	}
    	//the else case is dealt with by doing a storeCookies() call in Router 
    	dateFormat = new SimpleDateFormat(DATE_FORMAT);
    }    
    
    public void setApp(MyApplication myApp)
    {
    	this.myApp = myApp;
    }
    
    /**
     * Retrieves and stores cookies returned by the host on the other side
     * of the the open java.net.URLConnection.
     *
     * The connection MUST have been opened using the connect()
     * method or a IOException will be thrown.
     *
     * @param conn a java.net.URLConnection - must be open, or IOException will be thrown
     * @throws java.io.IOException Thrown if conn is not open.
     * @throws JSONException 
     */
    public void storeCookies(URLConnection conn) throws JSONException, IOException
    {    	    		
    	System.out.println("cookies: "+cookies);
    	if (cookies.length() == 0)
    	{
			String headerName;
			String cookieField;
			for (int i=1; (headerName = conn.getHeaderFieldKey(i)) != null; i++) {
				System.out.println("header field "+conn.getHeaderFieldKey(i)+" "+conn.getHeaderField(i));
			    if (headerName.equalsIgnoreCase(SET_COOKIE)) {
			    	//Extract the cookie in the specified header field
			    	cookieField = conn.getHeaderField(i);
			    	System.out.println("Cookie Field: "+cookieField);
			    	
			    	JSONObject cookie = Cookie.toJSONObject(cookieField);		    	
			    	System.out.println("JSONCookie "+cookie);
			    	//put the cookie found in the JSON mappings
			    	cookies.put((String) cookie.get("name"), cookie);
			    }		   	    
			}
			System.out.println("CookieManager, Cookies "+cookies);
			writeCookiesToFile();
    	}    	
    	else 
    	{
    		System.out.println("Dont store anything, session saved.");
    	}
    }
    
    /**
     * Prior to opening a URLConnection, calling this method will set all
     * unexpired cookies that match the path or subpaths for the underlying URL
     *
     * The connection MUST NOT have been opened 
     * method or an IOException will be thrown.
     *
     * @param conn a java.net.URLConnection - must NOT be open, or IOException will be thrown
     * @throws java.io.IOException Thrown if conn has already been opened.
     * @throws JSONException 
     */
    public void setCookies(URLConnection conn) throws IllegalStateException, 
    												IOException, JSONException {
		URL url = conn.getURL();
		String path = url.getPath();
		
		StringBuffer cookieStringBuffer = new StringBuffer();
		
		Iterator<String> cookieNames = cookies.keys();
		while(cookieNames.hasNext()) {
		    String cookieName = cookieNames.next();
		    JSONObject cookie = (JSONObject) cookies.get(cookieName);
		    
		    // check cookie to ensure path matches  and cookie is not expired
		    // if all is cool, add cookie to header string 
		    if (comparePaths((String)cookie.get(PATH), path) && 
		    					isNotExpired((String)cookie.get(EXPIRES))) {
		    	System.out.println("Cookie Name, Set cookie "+cookie);
				cookieStringBuffer.append(cookieName);
				cookieStringBuffer.append(NAME_VALUE_SEPARATOR);
				cookieStringBuffer.append(cookie.getString("value"));
				if (cookieNames.hasNext()) cookieStringBuffer.append(SET_COOKIE_SEPARATOR);
		    }
		}
		System.out.println("Set Cookies: "+cookieStringBuffer.toString());
	    conn.setRequestProperty(COOKIE, cookieStringBuffer.toString());
    }

    private boolean isNotExpired(String cookieExpires) {
		if (cookieExpires == null) return true;
		Date now = new Date();
		try {
		    return (now.compareTo(dateFormat.parse(cookieExpires))) <= 0;
		} catch (java.text.ParseException pe) {
		    pe.printStackTrace();
		    return false;
		}
    }

    private boolean comparePaths(String cookiePath, String targetPath) {
		if (cookiePath == null) {
		    return true;
		} else if (cookiePath.equals("/")) {
		    return true;
		} else if (targetPath.regionMatches(0, cookiePath, 0, cookiePath.length())) {
		    return true;
		} else {
		    return false;
		}
    }
    
    public String getCSRFToken() throws JSONException
    {
    	return (String) ((JSONObject) cookies.get("csrftoken")).getString("value");
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
    	FileOutputStream fos = myApp.openFileOutput(COOKIESFILE, 
				Context.MODE_PRIVATE);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		osw.write(cookies.toString());		
		osw.flush();
		osw.close();
    }
}