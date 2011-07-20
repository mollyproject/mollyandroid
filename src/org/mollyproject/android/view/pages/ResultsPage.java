package org.mollyproject.android.view.pages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.selection.SelectionManager;

import com.google.common.collect.ArrayListMultimap;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ResultsPage extends ContentPage {
	public static final Page INSTANCE = new ResultsPage();
	protected Router router;
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//myApp.addBreadCrumb(SelectionManager.getName(INSTANCE));
		/*try {
			webView.loadUrl(Router.getFrom(setLocator(SelectionManager.RESULTS_PAGE)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}*/
		ScrollView scr = new ScrollView(getApplicationContext());
		router = myApp.getRouter();
		String jsonText = null;
		
		LinearLayout allText = new LinearLayout(getApplicationContext());
		allText.setOrientation(LinearLayout.VERTICAL);
		
		ArrayListMultimap<String,String> examsByDate = ArrayListMultimap.create();
		try {
			jsonText = router.onRequestSent(SelectionManager.getName(INSTANCE));
			//Process the json text received
			if (jsonText != null)
			{
				JSONObject jsonObj = new JSONObject(jsonText);
				JSONArray entries = (JSONArray) jsonObj.get("entries");
				if (entries.length() > 0)
				{
					DateFormat defaultDateFormat = new SimpleDateFormat
							("EEE, d MMM yyyy HH:mm:ss Z");
					DateFormat myDateFormat = new SimpleDateFormat
							("d MMM yyy");
					for (int i = 0; i < entries.length(); i++)
					{
						JSONObject entry = entries.getJSONObject(i);
						//trim off result
						String title = entry.getString("course_code") + ": " +
								entry.getString("title")
								.replace("OxfordExams: Results for ","")
								.replace(" now available", "");
						title = title.substring(0, title.length() - 7);
						
						Date updatedDate = defaultDateFormat.parse
											(entry.getString("updated"));
						String myDate = myDateFormat.format(updatedDate);
						examsByDate.put(myDate, title);
						System.out.println(myDate+" "+title);
					}
				}
			}
			
			
			Iterator<String> dates = examsByDate.keySet().iterator();
			while(dates.hasNext())
			{
				String thisDate = dates.next();
				TextView dateView = new TextView(getApplicationContext());
				dateView.setTextSize(24);
				dateView.setText('\n'+thisDate);
				allText.addView(dateView);
				
				List<String> results = examsByDate.get(thisDate);
				for (String result : results)
				{
					String thisResult = result;
					TextView resultView = new TextView(getApplicationContext());
					resultView.setTextSize(18);
					resultView.setText(thisResult);
					allText.addView(resultView);
				}
			}
		} catch (Exception e) {
			String tryAgain = "There is a problem with loading the page." +'\n' +
					"Please try again later.";
			TextView tryAgainView = new TextView(getApplicationContext());
			tryAgainView.setText(tryAgain);
			allText.addView(tryAgainView);
		}
		scr.addView(allText);
		contentLayout.addView(scr,ViewGroup.LayoutParams.FILL_PARENT);
		setContentView(contentLayout);
	}
	
	public Page getInstance()
	{
		return INSTANCE;
	}
	

}
