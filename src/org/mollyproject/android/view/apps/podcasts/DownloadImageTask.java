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
	protected boolean iconCached;
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
		iconCached = ((MyApplication) page.getApplication()).hasPodcastIcon(urlStr);
	}
	
	@Override
	protected void onPostExecute(Void result) {
		if (defaultIcon)
		{
			imView.setImageResource(myApp.getImgResourceId("default_white"));
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
		        int newWidth = page.getWindow().getWindowManager().getDefaultDisplay().getWidth()/4;
		        int newHeight = page.getWindow().getWindowManager().getDefaultDisplay().getWidth()/4;
		       
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
