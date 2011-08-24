package org.mollyproject.android.view.apps.podcasts;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.controller.MyApplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<Void, Void, Void>
{
	protected ImageView imView;
	protected String urlStr;
	protected boolean defaultIcon;
	protected Bitmap bitmap;
	protected Page page;
	protected MyApplication myApp;
	public DownloadImageTask(Page page, ImageView imView, String urlStr)
	{
		super();
		this.page = page;
		this.imView = imView;
		this.urlStr = urlStr;
		defaultIcon = false;
		myApp = (MyApplication) page.getApplication();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((PodcastsCategoryPage) page).incRunningImgThreads();
	}
	
	@Override
	protected void onPostExecute(Void result) {
		if (defaultIcon)
		{
			imView.setImageResource(MyApplication.getImgResourceId("default_white"));
		}
		else
		{
			imView.setImageBitmap(bitmap);
		}
		((PodcastsCategoryPage) page).decRunningImgThreads();
	};
	
	@Override
	protected Void doInBackground(Void... arg0) {
		//first the thread waits for an amount of time, this is to avoid "deadlocks" when all 
		//20+ image download thread starts all to gether 
		if (!myApp.hasPodcastIcon(urlStr))
		{
			try {
				Random random = new Random();
				Thread.sleep(random.nextInt(10000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (!myApp.hasPodcastIcon(urlStr))
		{
			//now check again and start download the image if necessary
			try {
				URL url = new URL(urlStr);
				System.out.println(urlStr);
				HttpURLConnection conn= (HttpURLConnection)url.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				
				//matrix used to resize image:
		        int width = bitmap.getWidth();
		        int height = bitmap.getHeight();
		        int newWidth = page.getWindow().getWindowManager().getDefaultDisplay().getWidth()/4;
		        int newHeight = page.getWindow().getWindowManager().getDefaultDisplay().getWidth()/4;
		       
		        float scaleWidth = ((float) newWidth) / width;
		        float scaleHeight = ((float) newHeight) / height;
		       
		        Matrix matrix = new Matrix();
		        //resize the bitmap
		        matrix.postScale(scaleWidth, scaleHeight);
		        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                          width, height, matrix, true);
				myApp.updatePodcastIconsCache(urlStr, bitmap);
			} catch (Exception e) {
				e.printStackTrace();
				defaultIcon = true;
			}
		}
		else
		{
			bitmap = myApp.getIcon(urlStr);
		}
		return null;
	}
}
