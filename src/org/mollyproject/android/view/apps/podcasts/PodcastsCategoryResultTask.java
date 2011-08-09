package org.mollyproject.android.view.apps.podcasts;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.view.apps.Page;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PodcastsCategoryResultTask extends BackgroundTask<List<Map<String,String>>, Void, List<Map<String,String>>>{
	
	public PodcastsCategoryResultTask(PodcastsCategoryPage podcastsCategoryPage, boolean toDestroy, boolean dialog)
	{
		super(podcastsCategoryPage, toDestroy, dialog);
	}
	@Override
	public void updateView(List<Map<String, String>> resultMapsList) {
		LayoutInflater inflater = page.getLayoutInflater();
		
		LinearLayout podcastsLayout = (LinearLayout) inflater.inflate(R.layout.search_results_page, 
				((PodcastsCategoryPage) page).getContentLayout(), false);
		((PodcastsCategoryPage) page).getContentLayout().addView(podcastsLayout);
		
		TextView header = (TextView) page.findViewById(R.id.searchResultsHeader);
		
		LinearLayout resultsLayout = (LinearLayout) page.findViewById(R.id.generalResultsList);
		for (int i = 0; i < resultMapsList.size(); i++)
		{
			Map<String,String> resultMap = resultMapsList.get(i);
			
			LinearLayout thisResult = (LinearLayout) inflater.inflate(R.layout.podcast_category_result, 
					((PodcastsCategoryPage) page).getContentLayout(),false);
			resultsLayout.addView(thisResult);
			thisResult.setLayoutParams(Page.paramsWithLine);
			
			LinearLayout iconsLayout = (LinearLayout) thisResult.getChildAt(0);
			
			ImageView podcastIcon = (ImageView) iconsLayout.getChildAt(0); 
			String urlStr = resultMap.get("logoURL");
			//downloadImageAndShow(urlStr,podcastIcon);
			
			TextView infoText = (TextView) thisResult.getChildAt(1);
			infoText.setText(Html.fromHtml("<font size=18>" + resultMap.get("title") + "</font>" +
					"<br/>" + resultMap.get("description")));
		}
	}

	@Override
	protected List<Map<String, String>> doInBackground(
			List<Map<String, String>>... resultMapsLists) {
		return resultMapsLists[0];
	}

	protected void downloadImageAndShow(String urlStr, ImageView imView)
	{
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn= (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			int length = conn.getContentLength();
			InputStream is = conn.getInputStream();
			Bitmap bmImg = BitmapFactory.decodeStream(is);
			imView.setImageBitmap(bmImg);
		} catch (IOException e) {
			imView.setImageResource(R.drawable.android_button);
			e.printStackTrace();
		}
	}
	
}
