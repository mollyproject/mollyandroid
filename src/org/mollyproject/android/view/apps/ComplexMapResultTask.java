package org.mollyproject.android.view.apps;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.controller.MyApplication;
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
import android.widget.LinearLayout;

public class ComplexMapResultTask extends JSONProcessingTask {
	protected boolean exceptionCaught = false;
	public ComplexMapResultTask(ContentPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			MapView mapView = ((PageWithMap) page).getMapView();
			//reduce the size of the map view
			mapView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
	        		page.getWindowManager().getDefaultDisplay().getHeight()/3));
			
			JSONObject jsonMap = jsonContent.getJSONObject("map");
			System.out.println(jsonMap.toString());
			MyApplication.mapData = jsonMap.toString();
			List<OverlayItem> overlayItems= new ArrayList<OverlayItem>();
			MapController mapController = mapView.getController();
			
			if (mapView.getHeight() >= jsonMap.getInt("width") 
					& mapView.getWidth() >= jsonMap.getInt("height"))
			{
				mapController.setZoom(jsonMap.getInt("zoom"));
			}
			else
			{
				mapController.setZoom(jsonMap.getInt("zoom")-2);
			}
			
			JSONArray jsonMapCentre = jsonMap.getJSONArray("map_centre");
			//set map centre
			//WARNING: lon comes before lat here
			//0: lon
        	//1: lat
			GeoPoint mapCentre = new GeoPoint(jsonMapCentre.getDouble(1),jsonMapCentre.getDouble(0));
			
	        mapController.setCenter(mapCentre);
	        
	        //process all markers/overlay items
			JSONArray jsonMarkers = jsonMap.getJSONArray("markers");
			mapView.getOverlays().clear();
	        for (int i = 0; i < jsonMarkers.length(); i++)
	        {
	        	JSONArray marker = jsonMarkers.getJSONArray(i);
	        	//0: lat
	        	//1: lon
	        	//2: icon to use
	        	//3: title
	        	GeoPoint markerPosition = new GeoPoint(marker.getDouble(0),marker.getDouble(1));
	        	OverlayItem markerOverlay = new OverlayItem(marker.getString(3), "", markerPosition);
	        	//TODO: get the new set of icons (non-numbered marker & green star)
	        	if (!marker.getString(2).equals("green_star"))
	        	{
	        		System.out.println("marker icon: " + marker.getString(2));
	        		markerOverlay.setMarker(drawNumberOnImage(i, R.drawable.android_button));
	        	}
	        	overlayItems.add(markerOverlay);
	        }
	        
	        //add all the overlay items upon the map
	        ItemizedIconOverlay<OverlayItem> overlay 
	        				= new ItemizedIconOverlay<OverlayItem>(page,overlayItems, null);
	        mapView.getOverlays().add(overlay);
	        ((ContentPage) page).doneProcessingJSON();
		} catch (Exception e) {
			e.printStackTrace();
			exceptionCaught = true;
		}
	}

	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		if (Page.manualRefresh)
		{
			return super.doInBackground();
		}
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
