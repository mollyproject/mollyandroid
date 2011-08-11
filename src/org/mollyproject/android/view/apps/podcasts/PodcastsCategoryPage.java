package org.mollyproject.android.view.apps.podcasts;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PodcastsCategoryPage extends ContentPage {
	protected static final int AUDIO = 0;
	protected static final int VIDEO = 1;
	protected static final int ALL = 2;
	//protected List<Map<String,String>> resultMapsList;
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		new PodcastsCategoryTask(this,true, true).execute(myApp.getPodcastsSlug());
	}
	
	@Override
	public Page getInstance() {
		return this;
	}
	
	@Override
	public String getAdditionalParams() {
		// TODO Auto-generated method stub
		return myApp.getPodcastsSlug();
	}
	
	public void updatePage(List<Map<String,String>> resultMapsList)
	{
		LinearLayout resultsLayout = (LinearLayout) findViewById(R.id.generalResultsList);
		for (int i = 0; i < resultMapsList.size(); i++)
		{
			final Map<String,String> resultMap = resultMapsList.get(i);
			
			LinearLayout thisResult = (LinearLayout) layoutInflater.inflate(R.layout.podcast_category_result, 
					contentLayout,false);
			resultsLayout.addView(thisResult);
			
			thisResult.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					myApp.setIndPodcastSlug(resultMap.get("slug"));
					Intent myIntent = new Intent(PodcastsCategoryPage.this, IndividualPodcastPage.class);
					PodcastsCategoryPage.this.startActivityForResult(myIntent, 0);
				}
			});
			
			thisResult.setLayoutParams(Page.paramsWithLine);
			
			LinearLayout iconsLayout = (LinearLayout) thisResult.getChildAt(0);
			
			ImageView mediumIcon = (ImageView) iconsLayout.getChildAt(1);
			mediumIcon.setImageResource(myApp.getImgResourceId(resultMap.get("medium")));
			
			ImageView podcastIcon = (ImageView) iconsLayout.getChildAt(0); 
			String urlStr = resultMap.get("logoURL");
			TextView infoText = (TextView) thisResult.getChildAt(1);
			infoText.setText(Html.fromHtml("<font size=18>" + resultMap.get("title") + "</font>" +
					"<br/>" + resultMap.get("description")));
			new DownloadImageTask(this,podcastIcon, urlStr).execute();
			/*if (myApp.hasPodcastIcon(urlStr))
			{
				podcastIcon.setImageBitmap(myApp.getIcon(urlStr));
			}
			else
			{
				try {
					URL url = new URL(urlStr);
					HttpURLConnection conn= (HttpURLConnection)url.openConnection();
					conn.setDoInput(true);
					//conn.setConnectTimeout(5);
					conn.connect();
					InputStream is = conn.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					myApp.updatePodcastIconsCache(urlStr, bitmap);
					podcastIcon.setImageBitmap(bitmap);
				} catch (Exception e) {
					e.printStackTrace();
					//defaultIcon = true;
					podcastIcon.setImageResource(R.drawable.android_button);
				}
				//new DownloadImageTask(podcastIcon, urlStr).execute();
				//((PodcastsCategoryPage) page).downloadImageAndShow(urlStr,podcastIcon);
			}*/
		}
	}
	
}
