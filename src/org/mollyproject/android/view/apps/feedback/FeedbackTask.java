package org.mollyproject.android.view.apps.feedback;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;

public class FeedbackTask extends BackgroundTask<String, Void, List<String>>{

	public FeedbackTask(FeedbackPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(List<String> outputs) {
		// TODO Auto-generated method stub
		
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
		        pairsList.add(new BasicNameValuePair("body", params[0]));
		        pairsList.add(new BasicNameValuePair("format", "json"));
		        pairsList.add(new BasicNameValuePair("language_code", "en"));
	        }
	        else
	        {
	        	throw new Exception();
	        }
        
			return MyApplication.router.post(pairsList, 
					MyApplication.router.reverse(MollyModule.FEEDBACK_PAGE,null));
		} catch (SocketException e) {
			e.printStackTrace();
			otherException = true;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			otherException = true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			malformedURLException = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			unknownHostException = true;
		} catch (IOException e) {
			e.printStackTrace();
			ioException = true;
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (ParseException e) {
			e.printStackTrace();
			parseException = true;
		} catch (Exception e) {
			e.printStackTrace();
			otherException = true;
		}
		
		return null;
	}

}
