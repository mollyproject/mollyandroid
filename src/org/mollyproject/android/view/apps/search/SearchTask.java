package org.mollyproject.android.view.apps.search;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.controller.Router.OutputFormat;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.ContentPage;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SearchTask extends BackgroundTask<String, Void, List<Map<String,String>>> {

	protected String searchQuery;
	public SearchTask(Page page, boolean b)
	{
		super(page, b);
	}
	
	@Override
	public void updateView(List<Map<String,String>> outputs) {
		
		((MyApplication) page.getApplication()).setGeneralOutput(outputs);
		Intent myIntent = new Intent(page, SearchPage.class);
		page.startActivityForResult(myIntent, 0);
	}

	@Override
	protected List<Map<String,String>> doInBackground(String... queries) {
		//send query to server, and get back json response
		try {
			//get the result:
			JSONArray results = page.getRouter().onRequestSent("search:index", null, Router.OutputFormat.JSON, 
					"&query="+queries[0]).getJSONArray("results");
			
			List<Map<String,String>> resultMapsList = new ArrayList<Map<String,String>>();
			System.out.println("result maps list: " + resultMapsList);
			//store results in the list of maps, I could have just returned the JSONArray then process it
			//in updateView, but I don't want to handle the exceptions outside of the background method
			if (results.length() == 0)
			{
				//No results found
			}
			else if (results.length() == 1)// & results.getJSONObject(0).getBoolean("redirect_if_sole_result"))
			{
				//treat the sole-result case as a special case
			}
			else
			{
				for (int i = 0; i < results.length(); i++)
				{
					Map<String,String> resultMap = new HashMap<String, String>();
					
					//LinearLayout resultLayout = page.getLayoutInflater();
					JSONObject result = results.getJSONObject(i);
					String text = new String();
					//title
					text = text + result.getString("title") + "<br />"; 
					
					//additional text:
					if (!result.isNull("additional"))
					{
						text = text + result.getString("additional") + "<br />";
					}
					
					
					//search excerpts:
					if (!result.isNull("excerpt"))
					{
						text = text + result.getString("excerpt");
					}
					resultMap.put("text", text);
					//application
					resultMap.put("application", result.getString("application"));
					resultMapsList.add(resultMap);
				}
			}
			
			return resultMapsList;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			unknownHostException = true;
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (IOException e) {
			e.printStackTrace();
			ioException = true;
		}
		return null;
	}
}
