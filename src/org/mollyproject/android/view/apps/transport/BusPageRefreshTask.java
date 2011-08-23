package org.mollyproject.android.view.apps.transport;

import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.Page;

public class BusPageRefreshTask extends BackgroundTask<Void,Void,Void>{

	public BusPageRefreshTask(BusPage page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
	}

	@Override
	public void updateView(Void outputs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		if (((BusPage) page).firstReq)
		{
			new BusTask((BusPage) page,false,false).execute
				(((MyApplication) page.getApplication()).getTransportCache());
		}
		while (!isCancelled())
		{
			while (!((BusPage) page).needsRefreshing())
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!isCancelled())
			{
				//check again, in case the task is cancelled already
				((BusPage) page).toBeRefreshed(false);
				new BusTask((BusPage) page,false,false).execute();
			}
		}
		System.out.println("Bus refresh ending");
		return null;
	}

}
