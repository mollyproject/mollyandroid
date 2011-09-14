package org.mollyproject.android.view.apps.places;

import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.transport.BusPageRefreshTask;
import org.mollyproject.android.view.apps.transport.TrainPageRefreshTask;
import org.mollyproject.android.view.apps.transport.TransportPage;

public class TransportMapPageRefreshTask extends BackgroundTask<Void,Void,Void>{
	public static boolean transportMapNeedsRefresh; 
	public static String transportType;
	public static boolean overlayRendered;
	public TransportMapPageRefreshTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		overlayRendered = false;
	}

	@Override
	public void updateView(Void outputs) {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		overlayRendered = false;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		System.out.println("Transport Map refresh starting");
		if (!isCancelled())
		{
			//task's first run
			//check again, in case the task is cancelled already
			
			while (!((ContentPage) page).downloadedJSON())
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			TransportMapPageRefreshTask.transportMapNeedsRefresh = false;
			new TransportMapTask((PlacesResultsPage) page,false,false).execute
						(((ContentPage) page).getJSONContent());
		}
		
		while (!isCancelled())
		{
			while (!TransportMapPageRefreshTask.transportMapNeedsRefresh)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				if (transportType.equals(TransportPage.BUS))
				{
					Thread.sleep(BusPageRefreshTask.BUS_REFRESH_RATE);
				}
				else if (transportType.equals(TransportPage.RAIL))
				{
					Thread.sleep(TrainPageRefreshTask.TRAIN_REFRESH_RATE);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!isCancelled())
			{
				//check again, in case the task is cancelled already
				TransportMapPageRefreshTask.transportMapNeedsRefresh = false;
				new TransportMapTask((PlacesResultsPage) page,false,false).execute();
			}
		}
		System.out.println("Transport Map refresh ending");
		return null;
	}

}

