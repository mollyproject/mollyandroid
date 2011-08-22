package org.mollyproject.android.view.apps.transport;

import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

public class TransportPageTask extends BackgroundTask<Void, Void, Void>{

	public TransportPageTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(Void outputs) {
		// TODO Auto-generated method stub
		((ContentPage) page).doneProcessingJSON();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("HERE");
		((MyApplication) page.getApplication()).setTransportCache(((ContentPage) page).getJSONContent());
		((TransportPage) page).getLAM().dispatchResume();
		return null;
	}

}
