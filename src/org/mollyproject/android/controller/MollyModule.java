package org.mollyproject.android.controller;

import org.mollyproject.android.Splash;
import org.mollyproject.android.view.apps.HomePage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.UnimplementedPage;
import org.mollyproject.android.view.apps.contact.ContactPage;
import org.mollyproject.android.view.apps.library.LibraryPage;
import org.mollyproject.android.view.apps.results_release.ResultsReleasePage;

import com.google.inject.AbstractModule;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class MollyModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Page.class).annotatedWith(Names.named("contact")).to(ContactPage.class);
		bind(Page.class).annotatedWith(Names.named("library")).to(LibraryPage.class);
		bind(Page.class).annotatedWith(Names.named("results")).to(ResultsReleasePage.class);
		bind(Page.class).annotatedWith(Names.named("splash")).to(Splash.class);
		bind(Page.class).annotatedWith(Names.named("home")).to(HomePage.class);
		bind(Page.class).annotatedWith(Named.class).to(UnimplementedPage.class); 
	}

}
