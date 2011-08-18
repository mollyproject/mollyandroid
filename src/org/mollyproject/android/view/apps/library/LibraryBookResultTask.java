package org.mollyproject.android.view.apps.library;

import org.json.JSONObject;
import org.mollyproject.android.view.apps.ComplexMapResultTask;

public class LibraryBookResultTask extends ComplexMapResultTask{

	public LibraryBookResultTask(LibraryBookResultPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void updateView(JSONObject jsonContent) {
		super.updateView(jsonContent);
		
	}
}
