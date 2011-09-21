package org.mollyproject.android.view.apps.weblearn.announcement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.JSONProcessingTask;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WebLearnAnnouncementTask extends JSONProcessingTask {

	public WebLearnAnnouncementTask(ContentPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			JSONObject announcement = jsonContent.getJSONObject("announcement");
			LinearLayout announcementLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.weblearn_announcement, null);
			
			//Title of the announcement
			((TextView) announcementLayout.findViewById(R.id.announcementTitle)).setText(announcement.getString("title"));
			
			//Body of the announcement
			String body = announcement.getString("body");
			if (!announcement.isNull("createdByDisplayName") & !announcement.isNull("createdOn"))
			{
				body = body + "<br/>" + "<p><small><em>" + announcement.getString("createdByDisplayName") + " at " + 
									announcement.getString("createdOn") + "</em></small></p>";
			}
			((TextView) announcementLayout.findViewById(R.id.announcementText)).setText(Html.fromHtml(body));
			
			LinearLayout attachmentsLayout = (LinearLayout) announcementLayout.findViewById(R.id.attachmentsLayout);
			
			if (announcement.has("attachments"))
			{
				if (!announcement.isNull("attachments"))
				{
					JSONArray attachments = announcement.getJSONArray("attachments");
					for (int i = 0; i < attachments.length(); i++)
					{
						JSONObject attachment = attachments.getJSONObject(i);
						LinearLayout attachmentLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.clickable_search_result, null);
						((TextView) attachmentLayout.findViewById(R.id.clickableResultText)).setText(attachment.getString("name"));
						final String url = attachment.getString("url");
						attachmentLayout.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent myIntent = new Intent(Intent.ACTION_VIEW);
								myIntent.setData(Uri.parse(url));
								page.startActivityForResult(myIntent, 0);
							}
						});
						
						attachmentLayout.setLayoutParams(Page.paramsWithLine);
						attachmentsLayout.addView(attachmentLayout);
					}
				}
			}
			
			page.getContentLayout().removeAllViews();
			page.getContentLayout().addView(announcementLayout);
			
			((ContentPage) page).doneProcessingJSON();
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
	}
	
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		if(Page.manualRefresh)
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

}
