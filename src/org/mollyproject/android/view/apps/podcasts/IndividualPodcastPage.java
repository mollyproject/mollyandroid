package org.mollyproject.android.view.apps.podcasts;

import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.text.Html;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IndividualPodcastPage extends ContentPage {
	protected String slug; 
	@Override
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		slug = myApp.getIndPodcastSlug();
	};
	
	@Override
	public void onResume() {
		super.onResume();
		if (!jsonProcessed)
		{
			new IndividualPodcastTask(this, true, true).execute();
		}
	}
	
	@Override
	public String getAdditionalParams() {
		return ("&arg="+slug);
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getName() {
		return MollyModule.INDIVIDUAL_PODCAST_PAGE;
	}

	@Override
	public String getQuery() {
		return null;
	}
	
	private class IndividualPodcastTask extends BackgroundTask<Void,Void,JSONObject>
	{

		public IndividualPodcastTask(Page page,
				boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
			super(page, toDestroyPageAfterFailure, dialogEnabled);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void updateView(JSONObject outputs) {
			try
			{
				String parentSlug = jsonContent.getJSONObject("category").getString("slug");
				myApp.setPodcastsSlug(parentSlug);
				
				JSONObject jsonInfo = jsonContent.getJSONObject("podcast");
				
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
					new DownloadImageTask(IndividualPodcastPage.this,podcastLogo, logoUrl).execute();
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
					setURLClick(itemLayout, mediaURLStr);
					
					scrLayout.addView(itemLayout);
				}
				jsonProcessed = true;
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			while (!jsonDownloaded)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return jsonContent;
		}
		
	}

}
