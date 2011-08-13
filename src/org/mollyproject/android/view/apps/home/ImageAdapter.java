package org.mollyproject.android.view.apps.home;

import java.util.List;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    protected List<String> apps;
    protected Page page;
    public ImageAdapter(Page page, List<String> apps) 
    { 
    	this.apps = apps; 
    	this.page = page;
    }

    public int getCount() { return apps.size(); }
    
    public Object getItem(int position) { return null; }

    public long getItemId(int position) { return 0; }
    
    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
                
        //View view = page.getLayoutInflater().inflate(R.layout.grid_icon_layout, 
        //		((HomePage) page).getHomeLayout());       
        ImageView imageView;

        final MyApplication myApp = (MyApplication) page.getApplication();
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(page);
        } else {
            imageView = (ImageView) convertView;
        }
        
        imageView.setImageResource(myApp.getImgResourceId(apps.get(position)+"_img"));
        imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myApp.setNextLocator(apps.get(position));
				Intent myIntent = new Intent(v.getContext(), myApp.getPageClass(apps.get(position)));
                page.startActivityForResult(myIntent, 0);
			}
		});
        return imageView;
    }
}