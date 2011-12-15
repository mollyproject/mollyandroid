package org.mollyproject.android.view.apps.webcam;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IndividualWebcamUpdateTask extends JSONProcessingTask {
	protected Bitmap bitmap;
	protected LinearLayout webcamLayout;
	public IndividualWebcamUpdateTask(ContentPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		webcamLayout = page.getContentLayout();
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			URL url = new URL(jsonContent.getJSONObject("webcam").getString("url"));
			
			new SingleImageDownload(page, false, true).execute(url);
			
			//remember: the webcamLayout is now contentLayout
			JSONObject webcam = jsonContent.getJSONObject("webcam");
			//(LinearLayout) page.getLayoutInflater().inflate(R.layout.webcam, null);
			
			TextView webcamName = (TextView) webcamLayout.findViewById(R.id.webcamName);
			webcamName.setText(webcam.getString("title") + '\n' + MyApplication.hourFormat.format(new Date()));
			
			TextView webcamDetails = (TextView) webcamLayout.findViewById(R.id.webcamDetails);
			String description = new String();
			if (webcam.has("description"))
			{
				description = webcam.getString("description");
			}
			if (webcam.has("credit"))
			{
				description = description + '\n' + webcam.getString("credit");
			}
			webcamDetails.setText(description);
			
			((ContentPage) page).doneProcessingJSON();
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		} finally {
			IndividualWebcamPage.webcamNeedsRefresh = true;
		}
		
	}
	
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		JSONObject jsonContent = null;
		try {
			//Set or download the json
			if (params.length > 0)
			{
				jsonContent = params[0];
			}
			else 
			{
				jsonContent = MyApplication.router.requestJSON(page.getName(), page.getAdditionalParams(), null);
				MyApplication.webcamCache = jsonContent;
			}
			//download the image, these images cannot be reused so no need to cache them
			
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
		return jsonContent;
	}
	
	private class SingleImageDownload extends BackgroundTask<URL, Void, String>
	{

		public SingleImageDownload(Page page,
				boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
			super(page, toDestroyPageAfterFailure, dialogEnabled);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void updateView(String outputs) {
			try {
				ImageView webcamImage = (ImageView) webcamLayout.findViewById(R.id.webcamImage);
				webcamImage.setImageBitmap(bitmap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(URL... params) {
			try {
				HttpURLConnection conn= (HttpURLConnection)params[0].openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is;
				
				is = conn.getInputStream();
				
				bitmap = BitmapFactory.decodeStream(is);
				
				//matrix used to resize image:
		        int width = bitmap.getWidth();
		        int height = bitmap.getHeight();
		        int newWidth = page.getWindow().getWindowManager().getDefaultDisplay().getWidth();
		        int newHeight = page.getWindow().getWindowManager().getDefaultDisplay().getWidth()*width/height; //get the correct ratio
		       
		        float scaleWidth = ((float) newWidth) / width;
		        float scaleHeight = ((float) newHeight) / height;
		       
		        Matrix matrix = new Matrix();
		        //resize the bitmap
		        matrix.postScale(scaleWidth, scaleHeight);
		        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
	                      width, height, matrix, true);
				return "done downloading";
			} catch (IOException e) {
				e.printStackTrace();
				otherException = true;
			}
			return null;
		}
		
	}
}
