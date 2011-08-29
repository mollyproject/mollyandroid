package org.mollyproject.android.view.apps.transport;

import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

public class TrainPageRefreshTask extends BackgroundTask<Void,Void,Void>{
	public static boolean trainNeedsRefresh;
	public static final int TRAIN_REFRESH_RATE = 60000;
	public TrainPageRefreshTask(TrainPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(Void outputs) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		System.out.println("Train refresh starting");
		if (TransportPage.firstLoad == true)
		{
			TransportPage.firstLoad = false;
			//for the first request, json data already downloaded, no need to refresh
			TrainPageRefreshTask.trainNeedsRefresh = false;
			new TrainTask((TrainPage) page,false,false).execute(MyApplication.transportCache);
		}
		else if (!isCancelled())
		{
			//check again, in case the task is cancelled already
			TrainPageRefreshTask.trainNeedsRefresh = false;
			//dialog enabled
			new TrainTask((TrainPage) page,false,false).execute();
		}
		while (!isCancelled())
		{
			while (!TrainPageRefreshTask.trainNeedsRefresh)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(TRAIN_REFRESH_RATE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!isCancelled())
			{
				//check again, in case the task is cancelled already
				TrainPageRefreshTask.trainNeedsRefresh = false;
				new TrainTask((TrainPage) page,false,false).execute();
			}
		}
		System.out.println("Train refresh ending");
		return null;
	}

}
