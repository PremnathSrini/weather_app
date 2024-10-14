package classes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApp {

	public static JSONObject getWeatherData(String locationName)
	{
		JSONArray locationData = getLocationData(locationName);
		
		//extract latitudes and longitudes
		JSONObject location = (JSONObject) locationData.get(0);
		double latitude = (double) location.get("latitude");
		double longitude = (double) location.get("longitude");
		
		String urlString = "https://api.open-meteo.com/v1/forecast?"
				+ "latitude="+ latitude + "&longitude=" + longitude
				+ "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";
		try {
			HttpURLConnection conn = fetchApiResponse(urlString);
			if(conn.getResponseCode()!=200)
			{
				System.out.println("Could not connect to API");
				return null;
			}
			
			StringBuilder resultJson = new StringBuilder();
			Scanner sc = new Scanner(conn.getInputStream());
			while(sc.hasNext())
			{
				resultJson.append(sc.nextLine());
			}
			sc.close();
			conn.disconnect();
			
			JSONParser parser = new JSONParser();
			JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
			
			//hourly data
			JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");
			JSONArray time = (JSONArray) hourly.get("time");
			int index = findIndexOfCurrentTime(time);
			//get temperature
			JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
			double temperature = (double) temperatureData.get(index);
			
			//get weather code
			JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
			String weatherCondition = convertWeatherCode((long)weatherCode.get(index));
			
			
			//get humidity
			JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
			long humidity = (long)relativeHumidity.get(index);
			
			//get windspeed
			JSONArray windSpeedData = (JSONArray)hourly.get("wind_speed_10m");
			double windSpeed = (double)windSpeedData.get(index);
			
			//build the weatherjson
			JSONObject weatherData = new JSONObject();
			weatherData.put("temperature",temperature);
			weatherData.put("weatherCondition", weatherCondition);
			weatherData.put("humidity", humidity);
			weatherData.put("windSpeed", windSpeed);
			return weatherData;
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String convertWeatherCode(long weatherCode) {
		// TODO Auto-generated method stub
		String weatherCondition = "";
		if(weatherCode == 0L)
		{
			weatherCondition = "Clear";
		}
		else if(weatherCode <= 3L && weatherCode > 0L)
		{
			weatherCondition = "Cloudy";
		}
		else if((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 99L))
		{
			weatherCondition = "Rainy";
		}
		else if((weatherCode >= 71L && weatherCode <= 77L))
		{
			weatherCondition = "Snow";
		}
		
		return weatherCondition;
	}

	public static int findIndexOfCurrentTime(JSONArray timeList) {
		// TODO Auto-generated method stub
		String currentTime = getCurrentTime();
		
		for(int i=0;i<timeList.size();i++)
		{
			String time=(String) timeList.get(i);
			if(time.equalsIgnoreCase(currentTime))
			{
				return i;
			}
		}
		return 0;
	}

	public static String getCurrentTime() {
		// TODO Auto-generated method stub
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'T' HH':00' ");
		String formattedDateTime = currentDateTime.format(formatter);
		return formattedDateTime;
	}

	public static JSONArray getLocationData(String locationName) {
		locationName = locationName.replaceAll(" ","+");
		
		String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="
				+ locationName +"&count=10&language=en&format=json";
		
		try {
			HttpURLConnection conn = fetchApiResponse(urlString);
			if(conn.getResponseCode()!=200)
			{
				System.out.println("Could not connect to API");
				return null;
			}
			else
			{
				StringBuilder resultJson = new StringBuilder();
				Scanner sc= new Scanner(conn.getInputStream());
				while(sc.hasNext())
				{
					resultJson.append(sc.nextLine());
				}
				sc.close();
				conn.disconnect();
				
				
				JSONParser parser = new JSONParser();
				JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
				
				JSONArray locationData = (JSONArray) resultJsonObj.get("results");
				return locationData;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static HttpURLConnection fetchApiResponse(String urlString) {
		// TODO Auto-generated method stub
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			return conn;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
