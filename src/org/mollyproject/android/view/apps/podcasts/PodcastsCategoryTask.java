package org.mollyproject.android.view.apps.podcasts;

import java.io.IOException;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.Router;

public class PodcastsCategoryTask extends BackgroundTask<Void, Void, Void>{
	protected String slug;
	
	public PodcastsCategoryTask(String slug, PodcastsCategoryPage podcastsCategoryPage, boolean b)
	{
		super(podcastsCategoryPage, b);
		this.slug = slug;
	}
	
	@Override
	public void updateView(Void outputs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			JSONObject jsonOutput = page.getRouter().onRequestSent("podcast:category", slug,
					Router.OutputFormat.JSON, "");
			JSONArray podcasts = jsonOutput.getJSONArray("podcasts"); 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

}
