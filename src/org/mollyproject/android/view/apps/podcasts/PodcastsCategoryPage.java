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
					myApp.setPodcastsSlug(resultMap.get("slug"));
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
			new DownloadImageTask(podcastIcon, urlStr).execute();
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
	
	public class DownloadImageTask extends AsyncTask<Void, Void, Void>
	{
		protected ImageView imView;
		protected String urlStr;
		protected boolean defaultIcon;
		protected boolean iconCached;
		protected Bitmap bitmap;
		public DownloadImageTask(ImageView imView, String urlStr)
		{
			super();
			this.imView = imView;
			this.urlStr = urlStr;
			defaultIcon = false;
			iconCached = myApp.hasPodcastIcon(urlStr);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if (defaultIcon)
			{
				imView.setImageResource(R.drawable.android_button);
			}
			else
			{
				imView.setImageBitmap(bitmap);
			}
		};
		
		@Override
		protected Void doInBackground(Void... arg0) {
			if (!myApp.hasPodcastIcon(urlStr))
			{
				try {
					Random random = new Random();
					Thread.sleep(random.nextInt(10000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!myApp.hasPodcastIcon(urlStr))
			{
				try {
					URL url = new URL(urlStr);
					System.out.println(urlStr);
					HttpURLConnection conn= (HttpURLConnection)url.openConnection();
					conn.setDoInput(true);
					//conn.setConnectTimeout(5);
					conn.connect();
					InputStream is = conn.getInputStream();
					bitmap = BitmapFactory.decodeStream(is);

			        int width = bitmap.getWidth();
			        int height = bitmap.getHeight();
			        int newWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth()/4;
			        int newHeight = getWindow().getWindowManager().getDefaultDisplay().getWidth()/4;
			       
			        // calculate the scale - in this case = 0.4f
			        float scaleWidth = ((float) newWidth) / width;
			        float scaleHeight = ((float) newHeight) / height;
			       
			        // create a matrix for the manipulation
			        Matrix matrix = new Matrix();
			        // resize the bit map
			        matrix.postScale(scaleWidth, scaleHeight);
			        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
	                          width, height, matrix, true);
					myApp.updatePodcastIconsCache(urlStr, bitmap);
					//imView.setImageBitmap(bitmap);
				} catch (Exception e) {
					e.printStackTrace();
					defaultIcon = true;
					//imView.setImageResource(R.drawable.android_button);
				}
			}
			else
			{
				bitmap = myApp.getIcon(urlStr);
			}
			return null;
		}
	}
}
