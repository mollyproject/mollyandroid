package org.mollyproject.android.view.apps.contact;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.controller.Router;
import android.content.Intent;

public class ContactSearchTask extends BackgroundTask<String, Void, Void>
{

	public ContactSearchTask(ContactPage contactPage, boolean b) {
		super(contactPage, b);
	}

	@Override
	public void updateView(Void outputs) {
	}

	@Override
	protected Void doInBackground(String... args) {
		//args = { query, medium }
		try {
			String searchQuery = "query="+URLEncoder.encode(args[0],"UTF-8")+"&medium="+args[1];
			((ContactPage) page).setContactOutput(page.getRouter().onRequestSent(MollyModule
					.getName(ContactResultsPage.class),
					Router.OutputFormat.JSON, searchQuery));
			Intent myIntent = new Intent (page, page.getRouter().getDestination());
			//myApp.timeStart();
			page.startActivityForResult(myIntent,0);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			unknownHostException = true;
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (IOException e) {
			e.printStackTrace();
			ioException = true;
		}
		return null;
	}
}
