package org.mollyproject.android.view.apps.weather;

import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.os.Bundle;

public class WeatherPage extends ContentPage {

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		new WeatherForecastTask(this, true, true).execute();
	}
	
	
	@Override
	public Page getInstance() {
		return this;
	}


	@Override
	public String getAdditionalParams() {
		// TODO Auto-generated method stub
		return null;
	}

}
