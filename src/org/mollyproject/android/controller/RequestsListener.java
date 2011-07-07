package org.mollyproject.android.controller;

import org.json.JSONObject;

public interface RequestsListener {
	//void onRequestSent(URL requestedURL);	
	//JSONObject processRequest() throws Exception;
	JSONObject onRequestSent(String locator) throws Exception;
}
