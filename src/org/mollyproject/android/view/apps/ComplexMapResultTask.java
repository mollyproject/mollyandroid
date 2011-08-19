package org.mollyproject.android.view.apps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ComplexMapResultTask extends BackgroundTask<Void,Void,JSONObject>{
	protected boolean exceptionCaught = false;
	public ComplexMapResultTask(PageWithMap page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			MapView mapView = ((PageWithMap) page).getMapView();
			JSONObject jsonMap = jsonContent.getJSONObject("map");
			List<OverlayItem> overlayItems= new ArrayList<OverlayItem>();
			MapController mapController = mapView.getController();
			
			if (mapView.getHeight() >= jsonMap.getInt("width") 
					& mapView.getWidth() >= jsonMap.getInt("height"))
			{
				mapController.setZoom(jsonMap.getInt("zoom"));
			}
			//else
			//{
			//	mapController.setZoom(jsonMap.getInt("zoom")-2);
			//}
			
			JSONArray jsonMapCentre = jsonMap.getJSONArray("map_centre");
			//set map centre
			//WARNING: lon comes before lat here
			//0: lon
        	//1: lat
			GeoPoint mapCentre = new GeoPoint(jsonMapCentre.getDouble(1),jsonMapCentre.getDouble(0));
			
	        mapController.setCenter(mapCentre);
	        //OverlayItem centreMarker = new OverlayItem("Centre", "", mapCentre);
	        //overlayItems.add(centreMarker);
	        
	        //process all markers/overlay items
			JSONArray jsonMarkers = jsonMap.getJSONArray("markers");
	        //System.out.println(jsonMap.toString(1));
	        for (int i = 0; i < jsonMarkers.length(); i++)
	        {
	        	JSONArray marker = jsonMarkers.getJSONArray(i);
	        	//0: lat
	        	//1: lon
	        	//2: icon to use
	        	//3: title
	        	GeoPoint markerPosition = new GeoPoint(marker.getDouble(0),marker.getDouble(1));
	        	OverlayItem markerOverlay = new OverlayItem(marker.getString(3), "", markerPosition);
	        	markerOverlay.setMarker(drawNumberOnImage(i+1, R.drawable.android_button));
	        	overlayItems.add(markerOverlay);
	        }
	        ItemizedIconOverlay<OverlayItem> overlay = new ItemizedIconOverlay<OverlayItem>(page,overlayItems, null);
	        mapView.getOverlays().add(overlay);
	        ((ContentPage) page).doneProcessingJSON();
		} catch (JSONException e) {
			e.printStackTrace();
			exceptionCaught = true;
			jsonException = true;
		}
	}

	@Override
	protected JSONObject doInBackground(Void... arg0) {
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
	
	public BitmapDrawable drawNumberOnImage(Integer num, Integer id)
	{
		Drawable image = page.getResources().getDrawable(id);
		// Store our image size as a constant
		final int IMAGE_WIDTH = image.getIntrinsicWidth();
		final int IMAGE_HEIGHT = image.getIntrinsicHeight();
		image.setBounds(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		// You can also use Config.ARGB_4444 to conserve memory or ARGB_565 if 
		// you don't have any transparency.
		Bitmap canvasBitmap = Bitmap.createBitmap(IMAGE_WIDTH, 
		                                          IMAGE_HEIGHT, 
		                                          Bitmap.Config.ARGB_8888);
		// Create a canvas, that will draw on to canvasBitmap. canvasBitmap is
		// currently blank.
		Canvas imageCanvas = new Canvas(canvasBitmap);
		// Set up the paint for use with our Canvas
		Paint imagePaint = new Paint();
		imagePaint.setTextAlign(Align.CENTER);
		imagePaint.setTextSize(30f);
		imagePaint.setFakeBoldText(true);
		// Draw the image to our canvas
		image.draw(imageCanvas);
		//imageCanvas.draw
		// Draw the text on top of our image
		imageCanvas.drawText(num.toString(), IMAGE_WIDTH / 2, IMAGE_HEIGHT / 2, imagePaint);
		// This is the final image that you can use 
		return new BitmapDrawable(canvasBitmap);
	}
	
}
