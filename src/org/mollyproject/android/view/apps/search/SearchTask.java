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

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchTask extends BackgroundTask<JSONObject, Void, JSONObject> {

	protected String searchQuery;
	public SearchTask(Page page, boolean toDestroy, boolean dialog)
	{
		super(page, toDestroy, dialog);
	}
	
	@Override
	public void updateView(JSONObject jsonContent) {
		try
		{
			JSONArray results = jsonContent.getJSONArray("results");
			LayoutInflater inflater = page.getLayoutInflater();
			LinearLayout generalResultsLayout = (LinearLayout) inflater.inflate(R.layout.general_search_results_page, 
					((SearchPage) page).getContentLayout(), false);
			((SearchPage) page).getContentLayout().addView(generalResultsLayout);
			
			LinearLayout resultsLayout = (LinearLayout) generalResultsLayout.findViewById(R.id.generalResultsList);
			
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
					final JSONObject result = results.getJSONObject(i);
					LinearLayout thisResult = (LinearLayout) inflater.inflate(R.layout.general_search_result, 
								((SearchPage) page).getContentLayout(), false);
					resultsLayout.addView(thisResult);
					thisResult.setLayoutParams(Page.paramsWithLine);
					ImageView appIcon = (ImageView) thisResult.findViewById(R.id.generalSearchIcon);
					appIcon.setImageResource(((MyApplication) page.getApplication())
							.getImgResourceId(result.get("application") + ":index_img"));
					
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
					//text
					TextView infoText = (TextView) thisResult.findViewById(R.id.generalSearchText);
					infoText.setText(Html.fromHtml(text));
					
					thisResult.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								if (result.getString("application").equals("places"))
								{
									System.out.println("SEARCH RESULT PRESSED");
									String[] placesArgs = new String[2];
									JSONObject entity = result.getJSONObject("entity");
									placesArgs[0] = entity.getString("identifier_scheme");
									placesArgs[1] = entity.getString("identifier_value");
									myApp.setPlacesArgs(placesArgs);
									Intent myIntent = new Intent
												(page, myApp.getPageClass(MollyModule.PLACES_ENTITY));
									page.startActivityForResult(myIntent, 0);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		} catch (JSONException e)
		{
			e.printStackTrace();
			jsonException = true;
		}
	}

	@Override
	protected JSONObject doInBackground(JSONObject... args) {
		//send query to server, and get back json response
		//try {
			//get the result:
			return args[0];
			//JSONArray results = args[0].getJSONArray("results"); //page.getRouter().onRequestSent(MollyModule.SEARCH_PAGE, null, Router.OutputFormat.JSON, 
					//"&query="+queries[0])
			
			/*List<Map<String,String>> resultMapsList = new ArrayList<Map<String,String>>();
			System.out.println("result maps list: " + resultMapsList);
			
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
		return null;*/
	}
}
