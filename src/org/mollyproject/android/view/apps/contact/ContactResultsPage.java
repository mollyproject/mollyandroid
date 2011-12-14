package org.mollyproject.android.view.apps.contact;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
import android.widget.EditText;

public class ContactResultsPage extends AbstractContactPage {
	protected String[] contactArgs = new String[2];
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		contactArgs[0] = MyApplication.contactQuery[0];
		contactArgs[1] = MyApplication.contactQuery[1];
		name = MollyModule.CONTACT_RESULTS_PAGE;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((EditText) contactSearchBar.findViewById(R.id.contactSearchField)).setText(contactArgs[0]);
	}
	
	@Override
	public void refresh() {
		//query from myApp is processed in the background
		new NewContactResultsTask(this,false,true).execute();
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
