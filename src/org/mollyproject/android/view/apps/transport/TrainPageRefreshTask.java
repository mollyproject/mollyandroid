package org.mollyproject.android.view.apps.transport;

import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

public class TrainPageRefreshTask extends BackgroundTask<Void,Void,Void>{

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
		if (((TrainPage) page).firstReq)
		{
			new TrainTask((TrainPage) page,false,false).execute
				(((MyApplication) page.getApplication()).getTransportCache());
			((TrainPage) page).firstReq = false;
		}
		while (!isCancelled())
		{
			while (!((TrainPage) page).needsRefreshing())
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
				((AutoRefreshPage) page).toBeRefreshed(false);
				new TrainTask((TrainPage) page,false,false).execute();
			}
		}
		System.out.println("Train refresh ending");
		return null;
	}

}
