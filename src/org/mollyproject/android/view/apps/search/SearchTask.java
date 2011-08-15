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
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.Page;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchTask extends BackgroundTask<JSONObject, Void, List<Map<String,String>>> {

	protected String searchQuery;
	public SearchTask(Page page, boolean toDestroy, boolean dialog)
	{
		super(page, toDestroy, dialog);
	}
	
	@Override
	public void updateView(List<Map<String,String>> resultMapsList) {
		
		LayoutInflater inflater = page.getLayoutInflater();
		LinearLayout generalResultsLayout = (LinearLayout) inflater.inflate(R.layout.general_search_results_page, 
				((SearchPage) page).getContentLayout(), false);
		((SearchPage) page).getContentLayout().addView(generalResultsLayout);
		
		LinearLayout resultsLayout = (LinearLayout) generalResultsLayout.findViewById(R.id.generalResultsList);
		
		for (int i = 0; i < resultMapsList.size(); i++)
		{
			Map<String,String> resultMap = resultMapsList.get(i);
			
			LinearLayout thisResult = (LinearLayout) inflater.inflate(R.layout.general_search_result, 
						((SearchPage) page).getContentLayout(), false);
			resultsLayout.addView(thisResult);
			thisResult.setLayoutParams(Page.paramsWithLine);
			ImageView appIcon = (ImageView) thisResult.findViewById(R.id.generalSearchIcon);
			appIcon.setImageResource(((MyApplication) page.getApplication())
					.getImgResourceId(resultMap.get("application") + ":index_img"));
			//text
			TextView infoText = (TextView) thisResult.findViewById(R.id.generalSearchText); //(TextView) generalResultsLayout.findViewById(R.id.generalSearchText);
			infoText.setText(Html.fromHtml(resultMap.get("text")));
		}
		
		/*Intent myIntent = new Intent(page, SearchPage.class);
		page.startActivityForResult(myIntent, 0);*/
	}

	@Override
	protected List<Map<String,String>> doInBackground(JSONObject... args) {
		//send query to server, and get back json response
		try {
			//get the result:
			JSONArray results = args[0].getJSONArray("results"); //page.getRouter().onRequestSent(MollyModule.SEARCH_PAGE, null, Router.OutputFormat.JSON, 
					//"&query="+queries[0])
			
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
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
		return null;
	}
}
