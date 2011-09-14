package org.mollyproject.android.view.apps.contact;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class ContactResultsPage extends AbstractContactPage {
	protected String[] contactArgs;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		contactArgs = MyApplication.contactQuery;
		name = MollyModule.CONTACT_RESULTS_PAGE;
	}
	
	@Override
	public void refresh() {
		//query from myApp is extracted here and processed in the background
		new ContactResultsTask(this,contactSearchBar,false,true).execute(jsonContent);
	}

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getQuery() throws UnsupportedEncodingException {
		
		return ("&query="+URLEncoder.encode(contactArgs[0],"UTF-8")+"&medium="+contactArgs[1]);
	}

}
