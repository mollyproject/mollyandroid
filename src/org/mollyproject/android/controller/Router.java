package org.mollyproject.android.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.view.apps.Page;

import android.location.Location;

public class Router {
	protected CookieManager cookieMgr;
	//protected LocationThread currentLocThread;
	protected LocationTracker locTracker;
	protected String csrfToken;
	protected boolean firstReq;
	protected MyApplication myApp;
	protected HttpClient client;
	public final static String mOX =  "http://dev.m.ox.ac.uk/";

	public static enum OutputFormat { JSON, FRAGMENT, JS, YAML, XML, HTML };

	public Router (MyApplication myApp) throws IOException, JSONException, ParseException 
	{
		this.myApp = myApp;
		cookieMgr = new CookieManager(myApp);
		firstReq = true;
		locTracker = new LocationTracker(myApp);
		locTracker.startLocUpdate();
		client = new DefaultHttpClient();
	}
    
	public void setApp(MyApplication myApp)
    {
    	this.myApp = myApp;
    }
    
	//Take an URL String, convert to URL, open connection then process 
	//and return the response
	public String getFrom (String urlStr) throws MalformedURLException,
					IOException, UnknownHostException, SocketTimeoutException, JSONException, ParseException
	{
        String getURL = urlStr;
        System.out.println("Getting from: " + urlStr);
        HttpGet get = new HttpGet(getURL);
        HttpResponse responseGet = client.execute(get);  

        HttpEntity resEntityGet = responseGet.getEntity();  
        if (resEntityGet != null) {  
            //do something with the response
        	return EntityUtils.toString(resEntityGet);
        }
        
		return null;
		
	}
	
	public JSONObject exceptionHandledOnRequestSent(String locator, String arg, Page page, OutputFormat format, String query)
	{
		try{
			return onRequestSent(locator, arg, format, query);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			Page.popupErrorDialog("Malformed URL (Router)",
					"Please try restarting the app", page);
			
		}catch (JSONException e)
		{
			e.printStackTrace();
			Page.popupErrorDialog("JSON Exception (Router)", 
					"There might be a problem with JSON output " +
					"from server. Please try again later", page);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Page.popupErrorDialog("Unknown Host Exception (Router)", 
					"There might be a problem with network connection. " +
					"Please try later.", page);
		}  catch (IOException e)
		{
			e.printStackTrace();
			Page.popupErrorDialog("I/O Exception (Router)", 
					"There might be a problem with network connection. " +
					"Please try later.", page);
		} catch (ParseException e) {
			e.printStackTrace();
			Page.popupErrorDialog("Parse Exception (Router)", 
					"There might be a problem with network connection. " +
					"Please try later.", page);
		} 
		return null;
	}
	
	public String reverse(String locator, String arg) throws SocketTimeoutException, 
			MalformedURLException, UnknownHostException, IOException, JSONException, ParseException
	{
		//Geting the actual URL from the server using the locator (view name)
		//and the reverse API in Molly
		String reverseReq = new String();
		if (arg != null)
		{
			reverseReq = mOX + "reverse/?name="+locator + arg;
		}
		else
		{
			reverseReq = mOX + "reverse/?name="+locator;
		}
		return getFrom(reverseReq);
	}
	
	public JSONObject onRequestSent(String locator, String arg, OutputFormat format, String query) 
							throws JSONException, UnknownHostException, IOException, ParseException {
		/*basic method for all requests for json response, it sets up a url to be sent
		  to the server as follow:
		  1. it looks up the url to the required page using the reverse api with either the
		  view_name only or both the view_name and the extra argument (arg)
		  2. then it returns the url, and open a connection to that one itself
		  3. get the response
		  For now it seems quite pointless in including the switch statement and the 
		  format parameters, but I will change this in the future if some other formats are needed,
		  for now JSON is the sole choice and the method only needs to return JSON
		  
		  this method is also to be included in AsyncTask subclasses where no UI is allowed
		  in the doInBackground method
		*/
		
		String urlStr = reverse(locator,arg);
		String outputStr = new String();
		switch(format){
		//Depending on the format wanted, get the output
		case JSON:
			urlStr = urlStr+"?format=json";
			break;
		}
		
		if (query != null)
		{
			urlStr = urlStr+query;
		}
		
		if (!firstReq)
		{
			((DefaultHttpClient)client).setCookieStore(cookieMgr.getCookieStore());
			System.out.println("Cookie set");
			updateCurrentLocation(locTracker.getCurrentLoc());
		}
		outputStr = getFrom(urlStr);
		
		//Check for cookies here, after the first "proper" request, not the reverse request
		if (firstReq || cookieMgr.getCookieStore().getCookies().size() <
				((DefaultHttpClient) client).getCookieStore().getCookies().size()) 
		{
			//If cookiestore is empty and first request then try storing cookies if this is the first request
			//or if the session id is added, in which case the size of the cookie store in the app is smaller
			//than that of the client
			cookieMgr.storeCookies(((DefaultHttpClient)client).getCookieStore());
			((DefaultHttpClient)client).setCookieStore(cookieMgr.getCookieStore());
			firstReq = false;
		}
		
		//updateLocationManually("Walton Street");
		
        return new JSONObject(outputStr);
	}
	
	public void updateCurrentLocation(Location loc) throws JSONException, ParseException, ClientProtocolException, IOException
	{
        HttpPost post = new HttpPost(reverse("geolocation:index",null));
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        
        System.out.println("Cookies before loc test" + ((DefaultHttpClient) client).getCookieStore().getCookies());
        
        params.add(new BasicNameValuePair("csrfmiddlewaretoken", cookieMgr.getCSRFToken()));
        params.add(new BasicNameValuePair("longitude", new Double(loc.getLongitude()).toString()));
        params.add(new BasicNameValuePair("latitude", new Double(loc.getLatitude()).toString()));
        params.add(new BasicNameValuePair("accuracy", new Double(loc.getAccuracy()).toString()));
        params.add(new BasicNameValuePair("method", "html5request"));
        params.add(new BasicNameValuePair("format", "json"));
        params.add(new BasicNameValuePair("force", "True"));
        
        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
        post.setEntity(ent);
        HttpResponse responsePOST = client.execute(post);
        //System.out.println("Location update cookies: " + ((DefaultHttpClient) client).getCookieStore().getCookies());
        
        BufferedReader rd = new BufferedReader
    			(new InputStreamReader(responsePOST.getEntity().getContent()));
	    String line;
	    System.out.println("Updating current location");
	    
	    while ((line = rd.readLine()) != null) {
	        System.out.println(line);
	    }
	    rd.close();
	    
	    //System.out.println("Loc Test: " + getFrom("http://dev.m.ox.ac.uk/geolocation/"));
	    System.out.println("Cookies after loc test" + ((DefaultHttpClient) client).getCookieStore().getCookies());
	}
	
	public void updateLocationManually(String locationName) throws JSONException, 
				ClientProtocolException, IOException, ParseException
	{
        HttpPost post = new HttpPost(reverse("geolocation:index",null));
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        
        params.add(new BasicNameValuePair("csrfmiddlewaretoken", cookieMgr.getCSRFToken()));
        params.add(new BasicNameValuePair("format", "json"));
        params.add(new BasicNameValuePair("method", "geocoded"));
        params.add(new BasicNameValuePair("name", locationName));
        
        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
        post.setEntity(ent);
        HttpResponse responsePOST = client.execute(post);
        
        BufferedReader rd = new BufferedReader
    			(new InputStreamReader(responsePOST.getEntity().getContent()));
	    String line;
	    System.out.println("Testing manual Loc update");
	    
	    while ((line = rd.readLine()) != null) {
	        System.out.println(line);
	    }
	    rd.close();
	}
}
