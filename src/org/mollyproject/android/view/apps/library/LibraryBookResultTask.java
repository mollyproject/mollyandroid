package org.mollyproject.android.view.apps.library;

import org.json.JSONObject;
import org.mollyproject.android.view.apps.ComplexMapResultTask;
import org.mollyproject.android.view.apps.PageWithMap;

import android.widget.LinearLayout;

public class LibraryBookResultTask extends ComplexMapResultTask{

	public LibraryBookResultTask(LibraryBookResultPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void updateView(JSONObject jsonContent) {
		super.updateView(jsonContent);
		if (!exceptionCaught)
		{
			LinearLayout mapLayout = ((PageWithMap) page).getMapLayout();
			
			LinearLayout 
			
		}
	}
}
