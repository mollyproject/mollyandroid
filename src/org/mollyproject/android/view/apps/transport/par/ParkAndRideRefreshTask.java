package org.mollyproject.android.view.apps.transport.par;

import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.transport.TransportPage;

public class ParkAndRideRefreshTask extends BackgroundTask<Void,Void,Void>{
	public static boolean parNeedsRefresh;
	public static final int PAR_REFRESH_RATE = 300000;
	public ParkAndRideRefreshTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(Void outputs) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		System.out.println("PAR refresh starting");
		if (TransportPage.firstLoad == true & MyApplication.transportCache != null)
		{
			TransportPage.firstLoad = false;
			//for the first request and with json data already downloaded, no need to refresh
			ParkAndRideRefreshTask.parNeedsRefresh = false;
			new ParkAndRideTask(page,false,false).execute(MyApplication.transportCache);
		}
		else if (!isCancelled())
		{
			//check again, in case the task is cancelled already
			ParkAndRideRefreshTask.parNeedsRefresh = false;
			//dialog enabled
			new ParkAndRideTask(page,false,false).execute();
		}
		//auto-refresh actually starts from the second request
		while (!isCancelled())
		{
			while (!ParkAndRideRefreshTask.parNeedsRefresh)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(PAR_REFRESH_RATE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!isCancelled())
			{
				//check again, in case the task is cancelled already
				ParkAndRideRefreshTask.parNeedsRefresh = false;
				new ParkAndRideTask(page,false,false).execute();
			}
		}
		System.out.println("PAR refresh ending");
		return null;
	}

}
