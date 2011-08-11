package org.mollyproject.android.view.apps.podcasts;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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

import android.text.Html;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IndividualPodcastTask extends BackgroundTask<String, Void, Void>{
	
	protected Map<String,String> podcastMap;
	protected List<Map<String,String>> itemMapsList;
	public IndividualPodcastTask(IndividualPodcastPage indPodcastPage, boolean toDestroy,
			boolean dialog) {
		super(indPodcastPage, toDestroy, dialog);
		podcastMap = new HashMap<String,String>();
		itemMapsList = new ArrayList<Map<String,String>>();
	}

	@Override
	public void updateView(Void outputs) {
		//process podcastMap and itemMaps
		
		LayoutInflater inflater = page.getLayoutInflater();
		LinearLayout indPodcastLayout = (LinearLayout) inflater.inflate(R.layout.individual_podcast_page, 
				((IndividualPodcastPage) page).getContentLayout(), true);
		
		ImageView podcastLogo = (ImageView) page.findViewById(R.id.indPodcastIcon);
		String logoUrl = podcastMap.get("logoUrl");
		if (myApp.hasPodcastIcon(logoUrl))
		{
			podcastLogo.setImageBitmap(myApp.getIcon(logoUrl));
		}
		else
		{
			new DownloadImageTask(page,podcastLogo, logoUrl).execute();
		}
		
		TextView descriptionText = (TextView) page.findViewById(R.id.indPodcastDescription);
		descriptionText.setText(Html.fromHtml("<b>"+ podcastMap.get("title") + "</b>" + "<br/>" + "<br/>"
					+ podcastMap.get("description")));
		
		LinearLayout scrLayout = (LinearLayout) page.findViewById(R.id.indPodcastScrollLayout);
		//items list
		for (int i = 0; i < itemMapsList.size(); i++)
		{
			HashMap<String,String> itemMap = (HashMap<String, String>) itemMapsList.get(i);
			LinearLayout itemLayout = (LinearLayout) inflater.inflate(R.layout.individual_podcast_result, 
					((IndividualPodcastPage) page).getContentLayout(), false);
			
			//description text
			TextView itemDescription = (TextView) itemLayout.getChildAt(0);
			itemDescription.setText(Html.fromHtml("<b>" + itemMap.get("title") + "</b>" + "<br/>"
						+ itemMap.get("published_date") + "<br/>"
						+ itemMap.get("description")));
			
			//medium icon
			RelativeLayout mediumLayout = (RelativeLayout) itemLayout.getChildAt(1);
			
			ImageView mediumIcon = (ImageView) mediumLayout.getChildAt(0);
			mediumIcon.setImageResource(myApp.getImgResourceId(podcastMap.get("medium")));
			
			scrLayout.addView(itemLayout);
		}
	}

	@Override
	protected Void doInBackground(String... args) {
		JSONObject jsonOutput;
		try {
			
			jsonOutput = page.getRouter().onRequestSent
					("podcasts:podcast", args[0], Router.OutputFormat.JSON, null);
			JSONObject jsonInfo = jsonOutput.getJSONObject("podcast");
			podcastMap.put("title",jsonInfo.getString("title"));
			podcastMap.put("description",jsonInfo.getString("description"));
			podcastMap.put("logoUrl",jsonInfo.getString("logo"));
			podcastMap.put("medium", jsonInfo.getString("medium"));
			JSONArray items = jsonOutput.getJSONArray("items");
			for (int i = 0; i < items.length(); i++)
			{
				JSONObject item = items.getJSONObject(i);
				Map<String,String> itemMap = new HashMap<String,String>();
				itemMap.put("title", item.getString("title"));
				itemMap.put("description", item.getString("description"));
				
				Date publishedDate = MyApplication.podcastDateFormat.parse(item.getString("published_date"));
				itemMap.put("published_date", MyApplication.myDateFormat.format(publishedDate));
				itemMapsList.add(itemMap);
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
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
