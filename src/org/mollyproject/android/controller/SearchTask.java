package org.mollyproject.android.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;

public class SearchTask extends BackgroundTask<String, Void, List<View>> {

	protected String searchQuery;
	protected Router router;
	
	public void search(String query) throws UnknownHostException, JSONException, IOException
	{
		JSONObject jsonOutput = router.onRequestSent("search:index", Router.OutputFormat.JSON, query);
		
		
		JSONObject results = jsonOutput.getJSONObject("results");
		
	}
	
	public String getSearchQuery() { return searchQuery; }
	
	public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

	@Override
	public void updateView(List<View> outputs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected List<View> doInBackground(String... queries) {
		//send query to server, and get back json response
		try {
			//get the result:
			JSONArray results = router.onRequestSent("search:index", Router.OutputFormat.JSON, 
					queries[0]).getJSONArray("results");
			
			
			if (results.length() == 0)
			{
				//No results found
			}
			else if (results.length() == 1)
			{
				//treat the sole-result case as a special case
				if (results.getJSONObject(0).getBoolean("redirect_if_sole_result"))
				{
					//if this boolean value is true, go straight to the page in the result
				}
				else
				{
					//populate the page as normal
				}
			}
			else
			{
				for (int i = 0; i < results.length(); i++)
				{
					
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
