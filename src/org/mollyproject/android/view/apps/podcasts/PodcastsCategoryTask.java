package org.mollyproject.android.view.apps.podcasts;

import java.util.concurrent.RejectedExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
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

public class PodcastsCategoryTask extends BackgroundTask<JSONArray, Void, JSONArray>
{
	protected volatile boolean rejectedDownloadImages = false;
	public PodcastsCategoryTask(PodcastsCategoryPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONArray podcasts) {
		updatePage(podcasts);
	}

	@Override
	protected JSONArray doInBackground(JSONArray... params) {
		
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JSONArray podcasts = null;
		if(params.length > 0)
		{
			podcasts = params[0];
		}
		else 
		{
			try {
				podcasts = ((ContentPage) page).getJSONContent().getJSONArray("podcasts");
			} catch (JSONException e) {
				e.printStackTrace();
				jsonException = true;
			};
		}
		return podcasts;
	}
	
	public void updatePage(JSONArray podcasts)
	{
		LinearLayout contentLayout = ((ContentPage) page).getContentLayout();
		contentLayout.removeAllViews();
		
		LayoutInflater layoutInflater = page.getLayoutInflater();
		
		LinearLayout podcastsLayout = (LinearLayout) layoutInflater.inflate(R.layout.general_search_results_page, 
				contentLayout, false);
		contentLayout.addView(podcastsLayout);
		
		LinearLayout resultsLayout = (LinearLayout) podcastsLayout.findViewById(R.id.generalResultsList);
		
		for (int i = 0; i < podcasts.length(); i++)
		{
			try
			{
				final JSONObject result = podcasts.getJSONObject(i);
				
				LinearLayout thisResult = (LinearLayout) layoutInflater.inflate(R.layout.podcast_category_result, 
						contentLayout,false);
				resultsLayout.addView(thisResult);
				
				TextView mediumText = (TextView) podcastsLayout.findViewById(R.id.searchResultsHeader);
				//mediumText.setText(mediumTexts.get(currentlyShowing));
				
				final String slug = result.getString("slug");
				thisResult.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						myApp.setIndPodcastSlug(slug);
						Intent myIntent = new Intent(page, IndividualPodcastPage.class);
						page.startActivityForResult(myIntent, 0);
					}
				});
				
				thisResult.setLayoutParams(Page.paramsWithLine);
				
				String medium = result.getString("medium");
				ImageView mediumIcon = (ImageView) thisResult.findViewById(R.id.mediaIcon);
				mediumIcon.setImageResource(myApp.getImgResourceId(medium));
				
				//medium text
				ImageView podcastIcon = (ImageView) thisResult.findViewById(R.id.podcastIcon); 
				String urlStr = result.getString("logo");
				
				TextView podcastText = (TextView) thisResult.findViewById(R.id.podcastText);
				
				String description = result.getString("description");
				if (description.length() > 60)
				{
					int j = 40;
					if (description.charAt(40) != ' ')
					{
						
						while (description.charAt(j) != ' ') { j++;	}
					}
					description = description.substring(0, j)+"...";
				}
				podcastText.setText(Html.fromHtml("<font size=18>" + result.getString("title") + "</font>" +
						"<br/>" + description));
				
				((PodcastsCategoryPage) page).populateArrays(result,medium);
				
				new DownloadImageTask(page,podcastIcon, urlStr).execute();
				
			} catch (RejectedExecutionException e) {
				e.printStackTrace();
				if (!rejectedDownloadImages)
				{
					/*Toast.makeText(this, "Image download request rejected. " +
						"Some images will not show up properly.", Toast.LENGTH_LONG).show();*/
					Page.popupErrorDialog("Image request rejected","Some images will not show up properly."
							, page, false);
					rejectedDownloadImages = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				jsonException = true;
			}
		}
		((ContentPage) page).doneProcessingJSON();
		((PodcastsCategoryPage) page).firstLoadDone();
	}
	
}
