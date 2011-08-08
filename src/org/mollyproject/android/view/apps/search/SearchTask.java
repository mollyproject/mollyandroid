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

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SearchTask extends BackgroundTask<String, Void, Void> {

	protected String searchQuery;
	protected List<Map<String,String>> resultMapsList = new ArrayList<Map<String,String>>();
	public SearchTask(SearchPage searchPage, boolean b)
	{
		super(searchPage, b);
	}
	
	public String getSearchQuery() { return searchQuery; }
	
	public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

	@Override
	public void updateView(Void outputs) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = page.getLayoutInflater();
		LinearLayout generalResultsLayout = (LinearLayout) inflater.inflate(R.layout.search_results_page, 
				((SearchPage) page).getContentLayout(), false);
		((SearchPage) page).getContentLayout().addView(generalResultsLayout);
		
		ScrollView scr = (ScrollView) page.findViewById(R.id.generalResultsScroll);
		LinearLayout resultsLayout = (LinearLayout) page.findViewById(R.id.generalResultsList);
		for (int i = 0; i < resultMapsList.size(); i++)
		{
			Map<String,String> resultMap = resultMapsList.get(i);
			
			LinearLayout thisResult = (LinearLayout) inflater.inflate(R.layout.general_search_result, 
						((SearchPage) page).getContentLayout(), false);
			resultsLayout.addView(thisResult);
			thisResult.setLayoutParams(Page.paramsWithLine);
			//image view
			//ImageView appIcon = (ImageView) page.findViewById(R.id.generalSearchIcon);
			ImageView appIcon = (ImageView) thisResult.getChildAt(0);
			appIcon.setImageResource(((MyApplication) page.getApplication())
					.getImgResourceId(resultMap.get("application") + ":index_img"));
			//text
			TextView infoText = (TextView) thisResult.getChildAt(1); //(TextView) page.findViewById(R.id.generalSearchText);
			infoText.setText(Html.fromHtml(resultMap.get("text")));
			System.out.println("app: "+resultMap.get("application"));
		}
		
	}

	@Override
	protected Void doInBackground(String... queries) {
		//send query to server, and get back json response
		try {
			//get the result:
			JSONArray results = page.getRouter().onRequestSent("search:index", Router.OutputFormat.JSON, 
					queries[0]).getJSONArray("results");
			
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
						text = text + result.getString("excerpt") + "<br />";
					}
					resultMap.put("text", text);
					//application
					resultMap.put("application", result.getString("application"));
					resultMapsList.add(resultMap);
				}
			}
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
