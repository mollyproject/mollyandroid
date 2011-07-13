package org.mollyproject.android.view;

import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.selection.SelectionManager;
import org.mollyproject.android.view.pages.Page;

public class Renderer {
	protected SelectionManager mgr;	
	protected Queue<JSONObject> responses;
	
	public Renderer()
	{		
		responses = new LinkedList<JSONObject>();
		mgr = new SelectionManager();
	}
	
	public void render(JSONObject o) throws JSONException
	{
		String name = o.getString("view_name");
		Page page = mgr.getPage(name); //Page to render
		
	}
}
