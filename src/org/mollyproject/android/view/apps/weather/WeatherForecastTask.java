package org.mollyproject.android.view.apps.weather;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.MyApplication;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherForecastTask extends BackgroundTask<Void, Void, JSONObject>{
	public WeatherForecastTask(WeatherPage weatherPage, boolean toDestroy, boolean dialog)
	{
		super(weatherPage,toDestroy, dialog);
	}
	
	@Override
	public void updateView(JSONObject jsonContent) {
		try {
			//There are 2 sections in the weather page, an observation bar and a 3-day forecast layout
			
			//get the observation bar first:
			JSONObject observation = jsonContent.getJSONObject("observation");
			
			String city = "Today in " + observation.getString("name");
			
			String temperature = observation.getString("temperature")+"�C";
			
			String others = new String();
			//min and max temperature
			if (!observation.isNull("min_temperature"))
			{
				others = others + "Minimum temperature is "+ 
						observation.getString("min_temperature") +'\n';
			}
			if (!observation.isNull("max_temperature"))
			{
				others = others + "Maximum temperature is "+ 
						observation.getString("max_temperature") +'\n';
			}
			
			//wind direction and speed
			if (!observation.isNull("wind_direction") & !observation.isNull("wind_speed"))
			{
				others = others + observation.getString("wind_speed") 
								+ "mph " + observation.getString("wind_direction") + '\n';
			}
			
			//humidity
			if (!observation.isNull("humidity"))
			{
				others = others + observation.getString("humidity") + "% Relative Humidity" + '\n';
			}
			
			//pressure
			if (!observation.isNull("pressure"))
			{
				others = others + observation.getString("pressure")+" mbar";
				if (!observation.isNull("pressure_state"))
				{
					String stateSign = observation.getString("pressure_state");
					if (stateSign.equals("-"))
					{
						others = others + " and falling";
					}
					else if (stateSign.equals("+"))
					{
						others = others + " and rising";
					}
					others = others + '\n';
				}
			}
			
			//UV risk
			if (!observation.isNull("uv_risk"))
			{
				others = others + "UV risk is " + observation.getString("uv_risk") + '\n';
			}
			
			//sunrise and sunset
			if (!observation.isNull("sunrise"))
			{
				others = others + "Sunrise at " + observation.getString("sunrise");
			}
			if (!observation.isNull("sunset"))
			{
				others = others + "Sunset at " + observation.getString("sunset");
			}
			
			//Forecast information
			
			
			LayoutInflater layoutInflater = (LayoutInflater) page.getApplication()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout observationBar = (LinearLayout) layoutInflater
						.inflate(R.layout.weather_observation,
						((WeatherPage) page).getContentLayout(), false);
			((WeatherPage) page).getContentLayout().removeAllViews();
			((WeatherPage) page).getContentLayout().addView(observationBar);
			
			//Cannot do injection by InjectView outside of an Activity, roboguice limitation
			TextView temperatureText = (TextView) observationBar.findViewById(R.id.temperatureText);
			temperatureText.setText(temperature);
			
			TextView otherText = (TextView) observationBar.findViewById(R.id.otherText);
			otherText.setText(others);
			
			TextView cityName = (TextView) observationBar.findViewById(R.id.cityName);
			cityName.setText(city);
			
			ImageView currentWeatherIcon = (ImageView) observationBar.findViewById(R.id.currentWeatherIcon);
			currentWeatherIcon.setImageResource(MyApplication.getImgResourceId(observation.getString("icon")));
			
			//Forecast
			JSONArray forecasts = jsonContent.getJSONArray("forecasts"); 
			LinearLayout forecastLayout = (LinearLayout) page.getLayoutInflater().inflate(R.layout.weather_forecast, 
					((ContentPage) page).getContentLayout(),false);
			((WeatherPage) page).getContentLayout().addView(forecastLayout);
			String dayNames[] = new DateFormatSymbols().getWeekdays();
			Calendar cal = Calendar.getInstance();
			
			for (int i = 0; i < forecasts.length(); i++)
			{
				JSONObject forecast = forecasts.getJSONObject(i);
				
				LinearLayout forecastDayLayout = (LinearLayout) layoutInflater.inflate
						(R.layout.weather_forecast_day, ((ContentPage) page).getContentLayout(),false);
				forecastDayLayout.setLayoutParams(Page.paramsWithLine);
				forecastLayout.addView(forecastDayLayout);
				
				TextView dateText = (TextView) forecastDayLayout.findViewById(R.id.weatherDate);
				if (cal.get(Calendar.DAY_OF_WEEK) + i == 7)
				{
					dateText.setText(dayNames[(cal.get(Calendar.DAY_OF_WEEK) + i)]);
				}
				else
				{
					dateText.setText(dayNames[(cal.get(Calendar.DAY_OF_WEEK) + i)%7]);
				}
				
				TextView lowTemp = (TextView) forecastDayLayout.findViewById(R.id.lowestTemp);
				lowTemp.setText(forecast.getString("min_temperature")+"�C");
				
				TextView highTemp = (TextView) forecastDayLayout.findViewById(R.id.highestTemp);
				highTemp.setText(forecast.getString("max_temperature")+"�C");
				
				ImageView weatherForecastIcon = (ImageView) forecastDayLayout.findViewById(R.id.weatherForecastIcon);
				weatherForecastIcon.setImageResource(MyApplication.getImgResourceId(forecast.getString("icon")+"_small"));
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		}
	}

	@Override
	protected JSONObject doInBackground(Void... arg0) {
		
		while (!((ContentPage) page).downloadedJSON())
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ((ContentPage) page).getJSONContent();
	}
	
}
