package org.mollyproject.android.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.json.JSONObject;
import org.mollyproject.android.controller.RequestsListener;
import org.mollyproject.android.view.pages.HomePage;
import org.mollyproject.android.view.pages.Page;

public class Renderer {

	protected List<RequestsListener> reqListeners;
	protected Page currentPage;
	protected Queue<JSONObject> responses;
	
	public Renderer()
	{
		reqListeners = new ArrayList<RequestsListener>();
		currentPage = HomePage.INSTANCE;
	}
	
	public void addRequestsListener(RequestsListener listener)
	{
		reqListeners.add(listener);
	}
	
	public void send(String urlStr) throws Exception
	{
		//Request req = new Request(urlStr);
		for (RequestsListener reqListener : reqListeners)
		{
			reqListener.onRequestSent(new URL (urlStr));
			//add the responses to a queue, a response can be empty, so do a test
			//currently, there is only one single requests listener in the list
			JSONObject o = reqListener.processRequest();
			if (o != null) responses.add(o);
		}
	}
	
	public void setNewPage(Page newPage) throws Exception
	{
		send(currentPage.getURLStr());
		currentPage = newPage;
		currentPage.refresh();
	}
}
