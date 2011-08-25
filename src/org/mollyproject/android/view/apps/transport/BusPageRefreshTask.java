package org.mollyproject.android.view.apps.transport;

import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.map.PlacesResultsPage;

public class BusPageRefreshTask extends BackgroundTask<Void,Void,Void>{
	public static boolean busNeedsRefresh; 
	public BusPageRefreshTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(Void outputs) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		System.out.println("Bus refresh starting");
		if (TransportPage.firstLoad == true || PlacesResultsPage.firstLoad == true)
		{
			TransportPage.firstLoad = false;
			PlacesResultsPage.firstLoad = false;
			//for the first request, json data already downloaded, no need to refresh
			BusPageRefreshTask.busNeedsRefresh = false;
			new BusTask(page,false,false).execute(MyApplication.transportCache);
		}
		else if (!isCancelled())
		{
			//check again, in case the task is cancelled already
			BusPageRefreshTask.busNeedsRefresh = false;
			//dialog enabled
			new BusTask((BusPage) page,false,false).execute();
		}
		while (!isCancelled())
		{
			while (!BusPageRefreshTask.busNeedsRefresh)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!isCancelled())
			{
				//check again, in case the task is cancelled already
				BusPageRefreshTask.busNeedsRefresh = false;
				new BusTask((BusPage) page,false,false).execute();
			}
		}
		System.out.println("Bus refresh ending");
		return null;
	}

}
