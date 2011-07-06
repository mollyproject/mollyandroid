package org.mollyproject.android.controller;

import java.net.MalformedURLException;
import java.net.URL;

public class Request {

	private URL url;
	public Request(String s) throws MalformedURLException
	{
		url = new URL(s+"/?format=json");
	}
	
	public URL getRequestedURL()
	{
		return url;
	}
}
