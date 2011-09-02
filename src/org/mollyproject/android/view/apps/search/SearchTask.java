package org.mollyproject.android.view.apps.search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchTask extends BackgroundTask<Void, Void, JSONObject> {

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
					null);
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
								null);
					resultsLayout.addView(thisResult);
					thisResult.setLayoutParams(Page.paramsWithLine);
					ImageView appIcon = (ImageView) thisResult.findViewById(R.id.generalSearchIcon);
					appIcon.setImageResource(MyApplication
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
							try {
								String app = result.getString("application");
								if (app.equals("places"))
								{
									System.out.println("SEARCH RESULT PRESSED");
									String[] placesArgs = new String[2];
									JSONObject entity = result.getJSONObject("entity");
									placesArgs[0] = entity.getString("identifier_scheme");
									placesArgs[1] = entity.getString("identifier_value");
									MyApplication.placesArgs = placesArgs;
									Intent myIntent = new Intent
												(page, MyApplication.getPageClass(MollyModule.PLACES_ENTITY));
									page.startActivityForResult(myIntent, 0);
								}
								else if (app.equals("podcasts"))
								{
									String indSlug = result.getString("url").replace("/podcasts/", "");
									MyApplication.indPodcastSlug = indSlug;
									Intent myIntent = new Intent
										(page, MyApplication.getPageClass(MollyModule.INDIVIDUAL_PODCAST_PAGE));
									page.startActivityForResult(myIntent, 0);
								}
							
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
			((ContentPage) page).doneProcessingJSON();
		} catch (JSONException e)
		{
			e.printStackTrace();
			jsonException = true;
		}
	}

	@Override
	protected JSONObject doInBackground(Void... args) {
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ((ContentPage) page).getJSONContent();
	}
}