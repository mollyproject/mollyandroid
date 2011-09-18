package org.mollyproject.android.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;


import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpException;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.protocol.HttpContext;

public class Router {
	protected CookieManager cookieMgr;
	protected LocationTracker locTracker;
	protected boolean firstReq;
	protected MyApplication myApp;
	protected HttpClient client;
	protected HttpGet get;
	protected HttpPost post;
	public final static String mOX =  "http://m.ox.ac.uk/";

	public static enum OutputFormat { JSON, FRAGMENT, JS, YAML, XML, HTML };

	public Router (MyApplication myApp) throws SocketException, IOException, JSONException, ParseException 
	{
		this.myApp = myApp;
		cookieMgr = new CookieManager(myApp);
		firstReq = true;
		locTracker = new LocationTracker(myApp);
		locTracker.startLocUpdate();
		client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);
		
		((DefaultHttpClient) client).addRequestInterceptor(new HttpRequestInterceptor() {

            public void process(
                    final HttpRequest request,
                    final HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip");
                }
                //TODO: add the location updates header here when it is implemented on the Molly server
                //e.g: X-Current-Location: -1.6, 51, 100
            }
        });

        ((DefaultHttpClient) client).addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(
                    final HttpResponse response,
                    final HttpContext context) throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
                Header ceheader = entity.getContentEncoding();
                if (ceheader != null) {
                    HeaderElement[] codecs = ceheader.getElements();
                    for (int i = 0; i < codecs.length; i++) {
                        if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                            response.setEntity(
                                    new GzipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }

        });
	}
	
	static class GzipDecompressingEntity extends HttpEntityWrapper {

        public GzipDecompressingEntity(final HttpEntity entity) {
            super(entity);
        }

        @Override
        public InputStream getContent()
            throws IOException, IllegalStateException {

            // the wrapped entity's getContent() decides about repeatability
            InputStream wrappedin = wrappedEntity.getContent();

            return new GZIPInputStream(wrappedin);
        }

        @Override
        public long getContentLength() {
            // length of ungzipped content is not known
            return -1;
        }

    }
    
	public void setApp(MyApplication myApp)
    {
    	this.myApp = myApp;
    }
    
	//Take an URL String, convert to URL, open connection then process 
	//and return the response
	public synchronized String getFrom (String urlStr) throws MalformedURLException,
					IOException, UnknownHostException, SocketException, JSONException, ParseException
	{
        String getURL = urlStr;
        System.out.println("Getting from: " + urlStr);
        get = new HttpGet(getURL);
        HttpResponse responseGet = client.execute(get);  
        HttpEntity resEntityGet = responseGet.getEntity();  
        if (resEntityGet != null) {  
            //do something with the response
        	return EntityUtils.toString(resEntityGet);
        }
		return null;
	}
	
	public String reverse(String locator, String arg) throws SocketException, 
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
	
	public synchronized JSONObject onRequestSent(String locator, String arg, OutputFormat format, String query) 
				throws JSONException, ParseException, IllegalArgumentException, MalformedURLException, SocketException, IOException 
	{
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
		if (!firstReq)
		{
			((DefaultHttpClient)client).setCookieStore(cookieMgr.getCookieStore());
			System.out.println("Cookie set");
			if (LocationTracker.autoLoc) 
			{ 
				updateCurrentLocation(); 
			}
			else if (MyApplication.currentLocation != null) 
			{ 
				updateLocationManually(MyApplication.currentLocation.getString("name"), MyApplication.currentLocation.getDouble("latitude"), 
						MyApplication.currentLocation.getDouble("longitude"), 10.0); 
			}
		}
		
		System.out.println("GET Request");
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
		
        return new JSONObject(outputStr);
	}
	
	public List<String> post(List<NameValuePair> arguments, String url) throws ClientProtocolException, IOException
	{
		//take in arguments as a list of name-value pairs and a target url, encode all the arguments,
		//then do a POST request to the target url, return the output as a list of strings
		
		post = new HttpPost(url);
		UrlEncodedFormEntity ent = new UrlEncodedFormEntity(arguments,HTTP.UTF_8);
		post.setEntity(ent);
		HttpResponse responsePOST = client.execute(post);
		//System.out.println("Location update cookies: " + ((DefaultHttpClient) client).getCookieStore().getCookies());
        BufferedReader rd = new BufferedReader
    		(new InputStreamReader(responsePOST.getEntity().getContent()));
		List<String> output = new ArrayList<String>();
		String line;
	    
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
			output.add(line);
		}
		rd.close();
		return output;
	}
	
	public void updateCurrentLocation() throws JSONException, ParseException, 
				ClientProtocolException, IOException
	{
		Location loc = locTracker.getCurrentLoc();
		
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        
        params.add(new BasicNameValuePair("csrfmiddlewaretoken", cookieMgr.getCSRFToken()));
        params.add(new BasicNameValuePair("longitude", new Double(loc.getLongitude()).toString()));
        params.add(new BasicNameValuePair("latitude", new Double(loc.getLatitude()).toString()));
        params.add(new BasicNameValuePair("accuracy", new Double(loc.getAccuracy()).toString()));
        params.add(new BasicNameValuePair("method", "html5request"));
        params.add(new BasicNameValuePair("format", "json"));
        params.add(new BasicNameValuePair("force", "True"));
        
        List<String> output = post(params,reverse("geolocation:index", null));
        //in this case the result is only one line of text, i.e just the first element of the list is fine
        MyApplication.currentLocation = new JSONObject(output.get(0));
	}
	
	public void updateLocationManually(String locationName, Double lat, Double lon, Double accuracy) 
			throws JSONException, ClientProtocolException, SocketException, MalformedURLException, 
				UnknownHostException, IOException, ParseException
	{
		//update by coordinates
		Location loc = locTracker.getCurrentLoc();
		
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        
		params.add(new BasicNameValuePair("csrfmiddlewaretoken", cookieMgr.getCSRFToken()));
        params.add(new BasicNameValuePair("longitude", lon.toString()));
        params.add(new BasicNameValuePair("latitude", lat.toString()));
        params.add(new BasicNameValuePair("accuracy", accuracy.toString()));
        params.add(new BasicNameValuePair("name", locationName));
        params.add(new BasicNameValuePair("method", "manual"));
        params.add(new BasicNameValuePair("format", "json"));
        
        List<String> output = post(params,reverse("geolocation:index", null));
        //in this case the result is only one line of text, i.e just the first element of the list is fine
        MyApplication.currentLocation = new JSONObject(output.get(0));
	}
	
	public void updateLocationGeocoded(String locationName) throws JSONException, 
				ClientProtocolException, IOException, ParseException
	{
		//update by geo code
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        
        params.add(new BasicNameValuePair("csrfmiddlewaretoken", cookieMgr.getCSRFToken()));
        params.add(new BasicNameValuePair("format", "json"));
        params.add(new BasicNameValuePair("method", "geocoded"));
        params.add(new BasicNameValuePair("name", locationName));
        
        List<String> output = post(params,reverse("geolocation:index", null));
        //in this case the result is only one line of text, i.e just the first element of the list is fine
        MyApplication.currentLocation = new JSONObject(output.get(0));
	}
	
	public void downloadImage(URL url, Bitmap bitmap, int newWidth, int newHeight) throws IOException
	{
		HttpURLConnection conn= (HttpURLConnection)url.openConnection();
		conn.setDoInput(true);
		conn.connect();
		InputStream is = conn.getInputStream();
		bitmap = BitmapFactory.decodeStream(is);
		
		//matrix used to resize image:
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //int newWidth = page.getWindow().getWindowManager().getDefaultDisplay().getWidth();
        //int newHeight = page.getWindow().getWindowManager().getDefaultDisplay().getWidth()*3/4;
       
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
       
        Matrix matrix = new Matrix();
        //resize the bitmap
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                  width, height, matrix, true);
	}
	
	public void releaseConnection()
	{
		if (get != null) { get.abort(); }
		if (post != null) { post.abort(); }
	}
}
