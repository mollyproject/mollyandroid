package org.mollyproject.android.view.apps.home;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    protected List<JSONObject> apps;
    protected Page page;
    public ImageAdapter(Page page, List<JSONObject> apps) 
    { 
    	this.apps = apps; 
    	this.page = page;
    }

    public int getCount() { return apps.size(); }
    
    public Object getItem(int position) { return null; }

    public long getItemId(int position) { return 0; }
    
    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
    	try {
    		View appLayout = convertView;
    		if (appLayout == null) {
    			appLayout = page.getLayoutInflater().inflate
    	    			(R.layout.home_page_app, null);
    		}
    		JSONObject app = apps.get(position);
	    	final String name = app.getString("local_name") + ":index";
	        ((ImageView) appLayout.findViewById(R.id.appIcon)).setImageResource
	        				(MyApplication.getImgResourceId(name+"_img"));
	        
	        ((TextView) appLayout.findViewById(R.id.appName)).setText(app.getString("title"));
	        
	        appLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MyApplication.locator = name;
					Intent myIntent = new Intent(v.getContext(), MyApplication.getPageClass(name));
	                page.startActivityForResult(myIntent, 0);
				}
			});
        return appLayout;
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null; //shouldn't be here
    }
}