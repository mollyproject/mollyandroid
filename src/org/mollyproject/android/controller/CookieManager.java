package org.mollyproject.android.controller;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

import org.json.*;

import android.content.Context;

    /**
     * CookieManager is a simple utilty for handling cookies when working
     * with java.net.URL and java.net.URLConnection
     * objects.
     * 
     * 
     *     Cookiemanager cm = new CookieManager();
     *     URL url = new URL("http://www.hccp.org/test/cookieTest.jsp");
     *     
     *      . . . 
     *
     *     // getting cookies:
     *     URLConnection conn = url.openConnection();
     *     conn.connect();
     *
     *     // setting cookies
     *     cm.storeCookies(conn);
     *     cm.setCookies(url.openConnection());
     * 
     *     @author Ian Brown
     *      
     **/

public class CookieManager {
    
    private Map store;
    protected JSONObject jsonStore;
	private final static String COOKIESFILE = "cookiesFile.txt";
	protected Context context;
	
    
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String COOKIE_VALUE_DELIMITER = ";";
    private static final String PATH = "path";
    private static final String EXPIRES = "expires";
    private static final String DATE_FORMAT = "EEE, dd-MMM-yyyy hh:mm:ss z";
    private static final String SET_COOKIE_SEPARATOR="; ";
    private static final String COOKIE = "Cookie";
    
    private static final char NAME_VALUE_SEPARATOR = '=';
    private static final char DOT = '.';
    
    private DateFormat dateFormat;

    public CookieManager(Context context) throws JSONException, IOException  {
    	this.context = context;
    	
    	JSONObject jsonCookie = new JSONObject();
    	store = new HashMap();
    	jsonStore = new JSONObject();

    	dateFormat = new SimpleDateFormat(DATE_FORMAT);
    	 //JSON representation of store    	
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
    public void storeCookies(URLConnection conn) throws JSONException
    {
    	// let's determine the domain from where these cookies are being sent
    	if (store.isEmpty()) {
		String domain = getDomainFromHost(conn.getURL().getHost());
		
		Map domainStore; // this is where we will store cookies for this domain		
		
		// now let's check the store to see if we have an entry for this domain
		if (store.containsKey(domain)) {
		    // we do, so lets retrieve it from the store
		    domainStore = (Map)store.get(domain);
		} else {
		    // we don't, so let's create it and put it in the store
		    domainStore = new HashMap();
		    store.put(domain, domainStore);		  
		}
				
		// OK, now we are ready to get the cookies out of the URLConnection	
		String headerName=null;
		for (int i=1; (headerName = conn.getHeaderFieldKey(i)) != null; i++) {
		    if (headerName.equalsIgnoreCase(SET_COOKIE)) {
				Map cookie = new HashMap();
				StringTokenizer st = new StringTokenizer(conn.getHeaderField(i), COOKIE_VALUE_DELIMITER);
				
				// the specification dictates that the first name/value pair
				// in the string is the cookie name and value, so let's handle
				// them as a special case: 
				
				if (st.hasMoreTokens()) {
				    String token  = st.nextToken();
				    String name = token.substring(0, token.indexOf(NAME_VALUE_SEPARATOR));
				    String value = token.substring(token.indexOf(NAME_VALUE_SEPARATOR) + 1, token.length());
				    cookie.put(name, value);
				    domainStore.put(name, cookie);				    
				    System.out.println("CookieManager, "+name+" "+cookie);
				}
		    
				while (st.hasMoreTokens()) {
				    String token  = st.nextToken();
				    cookie.put(token.substring(0, token.indexOf(NAME_VALUE_SEPARATOR)).toLowerCase(),
				     token.substring(token.indexOf(NAME_VALUE_SEPARATOR) + 1, token.length()));
				}				
		    }
		   
	    	
		}
		
		jsonStore.put(domain,domainStore); //add the entry to the JSON Store
		
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
     */
    public void setCookies(URLConnection conn) throws IOException {
		
		// let's determine the domain and path to retrieve the appropriate cookies
		URL url = conn.getURL();
		String domain = getDomainFromHost(url.getHost());
		String path = url.getPath();
		
		Map domainStore = (Map)store.get(domain);
		if (domainStore == null) return;
		StringBuffer cookieStringBuffer = new StringBuffer();
		
		Iterator cookieNames = domainStore.keySet().iterator();
		while(cookieNames.hasNext()) {
		    String cookieName = (String)cookieNames.next();
		    Map cookie = (Map)domainStore.get(cookieName);
		    // check cookie to ensure path matches  and cookie is not expired
		    // if all is cool, add cookie to header string 
		    if (comparePaths((String)cookie.get(PATH), path) && 
		    					isNotExpired((String)cookie.get(EXPIRES))) {
			cookieStringBuffer.append(cookieName);
			cookieStringBuffer.append("=");
			cookieStringBuffer.append((String)cookie.get(cookieName));
			if (cookieNames.hasNext()) cookieStringBuffer.append(SET_COOKIE_SEPARATOR);
		    }
		}
		try {
		    conn.setRequestProperty(COOKIE, cookieStringBuffer.toString());
		    conn.connect();
		} catch (java.lang.IllegalStateException ise) {
		    IOException ioe = new IOException("Illegal State! Cookies cannot be " +
		    		"set on a URLConnection that is already connected. " 
		    + "Only call setCookies(java.net.URLConnection) AFTER calling " +
		    "		java.net.URLConnection.connect().");
		    throw ioe;
		}
    }

    private String getDomainFromHost(String host) {
		if (host.indexOf(DOT) != host.lastIndexOf(DOT)) {
		    return host.substring(host.indexOf(DOT) + 1);
		} else {
		    return host;
		}
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

    public Map getCookiesStore() {
    	return store;
    }
    
    public String getCSRFToken(URL url)
    {
    	return (String) ((Map)((Map) store.get(getDomainFromHost(url.getHost())))
    			.get("csrftoken")).get("csrftoken");
    }
    
    public JSONObject getJSONCookieStore()
    {
    	return jsonStore;
    }
    
    public String readCookiesFromFile() throws IOException, JSONException
    {
    	FileInputStream fIn = context.openFileInput(COOKIESFILE);
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
    	FileOutputStream fos = context.openFileOutput(COOKIESFILE, 
				Context.MODE_PRIVATE);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		osw.write(jsonStore.toString());
		System.out.println(jsonStore.toString());
		osw.flush();
		osw.close();
    }
}