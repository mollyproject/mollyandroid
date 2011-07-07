package org.mollyproject.android.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.json.JSONObject;
import org.mollyproject.android.controller.RequestsListener;
import org.mollyproject.android.view.pages.Page;
import org.mollyproject.android.view.pages.PlacesPage;

public class Renderer {

	protected List<RequestsListener> reqListeners;
	protected Page currentPage;
	protected Queue<JSONObject> responses;
	
	public Renderer()
	{
		reqListeners = new ArrayList<RequestsListener>();
		currentPage = PlacesPage.INSTANCE;
		responses = new LinkedList<JSONObject>();
	}
	
	public void addRequestsListener(RequestsListener listener)
	{
		reqListeners.add(listener);
	}
	
	/*public void send(String urlStr) throws Exception
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
	}*/
	
	public void sendAndGetResponse(String locator) throws Exception
	{
		for (RequestsListener reqListener : reqListeners)
		{
			JSONObject o = reqListener.onRequestSent(locator);
			//add the responses to a queue, a response can be empty, so do a test
			//currently, there is only one single requests listener in the list			
			if (o != null) responses.add(o);
		}
	}
	
	public void setNewPage(Page newPage) throws Exception
	{
		sendAndGetResponse(currentPage.getLocator());
		//currentPage = newPage;
		//currentPage.refresh();
	}
}
