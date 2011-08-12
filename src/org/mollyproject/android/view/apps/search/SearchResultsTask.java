package org.mollyproject.android.view.apps.search;

import java.util.List;
import java.util.Map;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

import android.text.Html;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchResultsTask extends BackgroundTask<List<Map<String,String>>, Void, List<Map<String,String>>>{
	
	public SearchResultsTask(SearchPage searchPage, boolean toDestroy, boolean dialog)
	{
		super(searchPage, toDestroy, dialog);
	}
	
	@Override
	public void updateView(List<Map<String,String>> resultMapsList) {
		LayoutInflater inflater = page.getLayoutInflater();
		LinearLayout generalResultsLayout = (LinearLayout) inflater.inflate(R.layout.general_search_results_page, 
				((SearchPage) page).getContentLayout(), false);
		((SearchPage) page).getContentLayout().addView(generalResultsLayout);
		
		LinearLayout resultsLayout = (LinearLayout) generalResultsLayout.findViewById(R.id.generalResultsList);
		
		for (int i = 0; i < resultMapsList.size(); i++)
		{
			Map<String,String> resultMap = resultMapsList.get(i);
			
			LinearLayout thisResult = (LinearLayout) inflater.inflate(R.layout.general_search_result, 
						((SearchPage) page).getContentLayout(), false);
			resultsLayout.addView(thisResult);
			thisResult.setLayoutParams(Page.paramsWithLine);
			//image view
			//ImageView appIcon = (ImageView) generalResultsLayout.findViewById(R.id.generalSearchIcon);
			ImageView appIcon = (ImageView) thisResult.getChildAt(0);
			appIcon.setImageResource(((MyApplication) page.getApplication())
					.getImgResourceId(resultMap.get("application") + ":index_img"));
			//text
			TextView infoText = (TextView) thisResult.getChildAt(1); //(TextView) generalResultsLayout.findViewById(R.id.generalSearchText);
			infoText.setText(Html.fromHtml(resultMap.get("text")));
			System.out.println("app: "+resultMap.get("application"));
		}
				
	}

	@Override
	protected List<Map<String,String>> doInBackground(List<Map<String, String>>... resultMapsLists) {
		return resultMapsLists[0];
	}

}
