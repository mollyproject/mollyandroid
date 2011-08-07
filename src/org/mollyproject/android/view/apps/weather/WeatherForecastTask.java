package org.mollyproject.android.view.apps.weather;

import java.io.IOException;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.controller.BackgroundTask;
import org.mollyproject.android.controller.Router;
import org.mollyproject.android.view.apps.ContentPage;

import roboguice.inject.InjectView;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeatherForecastTask extends BackgroundTask<Void, Void, Void>{
	protected String temperature;
	protected String others;
	protected String city;
	public WeatherForecastTask(WeatherPage weatherPage, boolean b)
	{
		super(weatherPage,b);
		temperature = new String();
		others = new String();
	}
	
	@Override
	public void updateView(Void outputs) {
		LayoutInflater layoutInflater = (LayoutInflater) page.getApplication()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout observationBar = (LinearLayout) layoutInflater
						.inflate(R.layout.weather_observation,
						((WeatherPage) page).getContentLayout(), false);
		((WeatherPage) page).getContentLayout().addView(observationBar);
		
		//Cannot do injection by InjectView outside of an Activity, roboguice limitation
		TextView temperatureText = (TextView) page.findViewById(R.id.temperatureText);
		TextView otherText = (TextView) page.findViewById(R.id.otherText);
		TextView cityName = (TextView) page.findViewById(R.id.cityName);
		temperatureText.setText(temperature);
		otherText.setText(others);
		cityName.setText(city);
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		//Get weather info
		try {
			JSONObject jsonOutput = page.getRouter()
				.onRequestSent("weather:index", Router.OutputFormat.JSON, null);
			
			//There are 2 sections in the weather page, an observation bar and a 3-day forecast layout
			
			//get the observation bar first:
			JSONObject observation = jsonOutput.getJSONObject("observation");
			
			city = observation.getString("name");
			
			temperature = observation.getString("temperature")+"°C";
			
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
		} catch (UnknownHostException e) {
			e.printStackTrace();
			unknownHostException = true;
		} catch (JSONException e) {
			e.printStackTrace();
			jsonException = true;
		} catch (IOException e) {
			e.printStackTrace();
			ioException = true;
		}
		
		return null;
	}
	
}
