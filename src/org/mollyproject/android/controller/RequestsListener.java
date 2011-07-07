package org.mollyproject.android.controller;

public interface RequestsListener {
	//void onRequestSent(URL requestedURL);	
	//JSONObject processRequest() throws Exception;
	void onRequestSent(String locator) throws Exception;
}
