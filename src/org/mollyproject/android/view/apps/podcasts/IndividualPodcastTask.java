package org.mollyproject.android.view.apps.podcasts;

import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.text.Html;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IndividualPodcastTask extends BackgroundTask<Void,Void,JSONObject>
{

	public IndividualPodcastTask(Page page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try
		{
			String parentSlug = jsonContent.getJSONObject("category").getString("slug");
			myApp.setPodcastsSlug(parentSlug);
			
			JSONObject jsonInfo = jsonContent.getJSONObject("podcast");
			
			LinearLayout contentLayout = ((ContentPage) page).getContentLayout();
			
			LayoutInflater layoutInflater = page.getLayoutInflater();
			LinearLayout indPodcastLayout = (LinearLayout) layoutInflater.inflate(R.layout.individual_podcast_page, 
					contentLayout, true);
			
			ImageView podcastLogo = (ImageView) indPodcastLayout.findViewById(R.id.indPodcastIcon);
			String logoUrl = jsonInfo.getString("logo");
			if (myApp.hasPodcastIcon(logoUrl))
			{
				podcastLogo.setImageBitmap(myApp.getIcon(logoUrl));
			}
			else
			{
				new DownloadImageTask(page,podcastLogo, logoUrl).execute();
			}
			
			TextView titleText = (TextView) indPodcastLayout.findViewById(R.id.indPodcastTitle);
			titleText.setText(Html.fromHtml("<b>"+ jsonInfo.getString("title") + "</b>" + "<br/>" + "<br/>"));
			
			TextView descriptionText = (TextView) indPodcastLayout.findViewById(R.id.indPodcastDescription);
			descriptionText.setText(jsonInfo.getString("description"));
			
			LinearLayout scrLayout = (LinearLayout) indPodcastLayout.findViewById(R.id.indPodcastScrollLayout);
	
			//items list
			JSONArray items = jsonContent.getJSONArray("items");
			
			for (int i = 0; i < items.length(); i++)
			{
				//HashMap<String,String> itemMap = (HashMap<String, String>) itemMapsList.get(i);
				JSONObject item = items.getJSONObject(i);
				LinearLayout itemLayout = (LinearLayout) layoutInflater.inflate(R.layout.individual_podcast_result, 
						contentLayout, false);
				
				//description text
				TextView itemDescription = (TextView) itemLayout.findViewById(R.id.podcastIndividualText);
				
				itemDescription.setText(Html.fromHtml("<b>" + item.getString("title") + "</b>" + "<br/>"
							+ MyApplication.myDateFormat.format
								(MyApplication.podcastDateFormat.parse(item.getString("published_date"))) + "<br/>"
							+ item.getString("description")));
				
				//medium icon
				ImageView mediumIcon = (ImageView) itemLayout.findViewById(R.id.mediumIcon);
				mediumIcon.setImageResource(myApp.getImgResourceId(jsonInfo.getString("medium")));
				
				//podcast url and details
				JSONObject details = item.getJSONArray("enclosures").getJSONObject(0);
	
				String mediumText = details.getString("mimetype_display");
				TextView mediumTextView = (TextView) itemLayout.findViewById(R.id.mediumText); 
				mediumTextView.setText(mediumText);
				String mediaURLStr = details.getString("url");
				page.setURLClick(itemLayout, mediaURLStr);
				
				scrLayout.addView(itemLayout);
			}
			((ContentPage) page).doneProcessingJSON();
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (ParseException e) {
			e.printStackTrace();
			parseException = true;
		} catch (NullPointerException e) {
			e.printStackTrace();
			nullPointerException = true;
		}
	}

	@Override
	protected JSONObject doInBackground(Void... params) {
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
