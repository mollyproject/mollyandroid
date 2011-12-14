package org.mollyproject.android.view.apps.webcam;

import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;

public class IndividualWebcamTask extends BackgroundTask<Void, Void, Void> {

	public IndividualWebcamTask(ContentPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(Void outputs) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected Void doInBackground(Void... arg0) {
		//wait till the json is downloaded, the loop should only run in the first load
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Webcam refresh starting");
		if (IndividualWebcamPage.firstLoad == true & MyApplication.webcamCache != null)
		{
			IndividualWebcamPage.firstLoad = false;
			//for the first request, json data already downloaded, no need to refresh
			IndividualWebcamPage.webcamNeedsRefresh = false;
			new IndividualWebcamUpdateTask((ContentPage) page,false,false).execute(MyApplication.webcamCache);
		}
		else if (!isCancelled())
		{
			//check again, in case the task is cancelled already
			IndividualWebcamPage.webcamNeedsRefresh = false;
			//dialog enabled
			new IndividualWebcamUpdateTask((ContentPage) page,false,false).execute();
		}
		while (!isCancelled() & !page.isFinishing())
		{
			while (!IndividualWebcamPage.webcamNeedsRefresh)
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
				IndividualWebcamPage.webcamNeedsRefresh = false;
				new IndividualWebcamUpdateTask((ContentPage) page,false,false).execute();
			}
		}
		System.out.println("Webcam refresh ending");
		return null;
	}

}
