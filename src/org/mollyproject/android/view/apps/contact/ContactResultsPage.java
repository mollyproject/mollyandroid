package org.mollyproject.android.view.apps.contact;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class ContactResultsPage extends AbstractContactPage {
	protected String[] args;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		args = MyApplication.contactQuery;
		System.out.println(args[0]);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//query from myApp is extracted here and processed in the background
		if (!jsonProcessed)
		{
			new ContactResultsTask((AbstractContactPage) getInstance(),
				contactSearchBar,false,true).execute(jsonContent);
		}
	}
	
	@Override
	public String getAdditionalParams() {
		return null;
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getName() {
		return MollyModule.CONTACT_RESULTS_PAGE;
	}

	@Override
	public String getQuery() throws UnsupportedEncodingException {
		
		return ("&query="+URLEncoder.encode(args[0],"UTF-8")+"&medium="+args[1]);
	}

}
