package org.mollyproject.android.view.apps.podcasts;

import java.util.Map;
import java.util.Queue;

import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.widget.ImageView;

public class ImageBatchesTask extends BackgroundTask<Void,Void,Queue<Map<ImageView, String>>>{

	public ImageBatchesTask(Page page, boolean toDestroyPageAfterFailure,
			boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateView(Queue<Map<ImageView, String>> downloadQueue) {
		/*while(!downloadQueue.isEmpty())
		{
			while (((PodcastsCategoryPage) page).getRunningImgThreads() > 10)
			{
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Map<ImageView,String> imageCache = downloadQueue.poll();
			//each cache contains one view only
			for (ImageView imView : imageCache.keySet())
			{
				new DownloadImageTask(page, imView, imageCache.get(imView)).execute();
			}
		}*/
	}

	@Override
	protected Queue<Map<ImageView, String>> doInBackground(Void... params) {
		while (!((ContentPage) page).jsonProcessed())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
		
		Queue<Map<ImageView, String>> downloadQueue = ((PodcastsCategoryPage) page).getDownloadQueue();
		System.out.println("Queue length: "+downloadQueue.size());
		while(!downloadQueue.isEmpty() & !isCancelled())
		{
			while (((PodcastsCategoryPage) page).getRunningImgThreads() > 4)
			{
				//System.out.println("Threads running: " + ((PodcastsCategoryPage) page).getRunningImgThreads());
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Map<ImageView,String> imageCache = downloadQueue.poll();
			//each cache contains one view only
			for (ImageView imView : imageCache.keySet())
			{
				new DownloadImageTask(page, imView, imageCache.get(imView)).execute();
			}
		}
		System.out.println("Is cancelled "+ isCancelled());
		return null;
	}

}
