package org.mollyproject.android.view.apps.library;

import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.Page;

public class LibraryPage extends AbstractLibraryPage {

	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getAdditionalParams() {
		return null;
	}

	@Override
	public String getName() {
		return MollyModule.LIBRARY_PAGE;
	}
}
