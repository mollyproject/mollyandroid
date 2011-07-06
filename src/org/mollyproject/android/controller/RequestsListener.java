package org.mollyproject.android.controller;

import java.net.URL;

import org.json.JSONObject;

public interface RequestsListener {
	void onRequestSent(URL requestedURL);
	JSONObject processRequest() throws Exception;
}
