package org.mollyproject.android.view.apps.contact;

import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ContactPage extends AbstractContactPage {
	public static String PHONE = "phone";
	public static String EMAIL = "email";
	public static String MEDIUM = "medium";
	protected LinearLayout contactSearchBar;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public String getAdditionalParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page getInstance() {
		return this;
	}
}
