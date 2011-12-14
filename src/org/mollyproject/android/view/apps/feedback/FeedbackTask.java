package org.mollyproject.android.view.apps.feedback;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;

import android.widget.Toast;

public class FeedbackTask extends BackgroundTask<String, Void, List<String>>{

	public FeedbackTask(FeedbackPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(List<String> outputs) {
		System.out.println(outputs.get(0));
		Toast.makeText(page.getApplicationContext(), "Your feedback has been sent", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected List<String> doInBackground(String... params) {
		//first string should be the feedback body
		//the second optional string should be the email
		// 0 < params.length < 3
		try {
			if (params.length < 1 || params.length > 2)
			{
				throw new Exception();
			}
	        List<NameValuePair> pairsList = new ArrayList<NameValuePair>();
	        if (MyApplication.csrfToken != null)
	        {
		        pairsList.add(new BasicNameValuePair("csrfmiddlewaretoken", MyApplication.csrfToken));
		        String body = "[Android App] " + params[0];
		        pairsList.add(new BasicNameValuePair("body", body));
		        pairsList.add(new BasicNameValuePair("format", "json"));
		        pairsList.add(new BasicNameValuePair("language_code", "en"));
		        if (params[1].length() > 0)
		        {
		        	pairsList.add(new BasicNameValuePair("email", params[1]));
		        }
	        }
	        else
	        {
	        	throw new Exception();
	        }
        
			return MyApplication.router.post(pairsList, 
					MyApplication.router.reverse(MollyModule.FEEDBACK_PAGE,null));
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
		return null;
	}

}
