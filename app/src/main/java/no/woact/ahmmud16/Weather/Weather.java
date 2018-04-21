package no.woact.ahmmud16.Weather;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import no.woact.ahmmud16.Fragments.MainMenuFragment;
import no.woact.ahmmud16.Interfaces.ImageASync;
import no.woact.ahmmud16.Interfaces.WeatherASync;

/**
 * This class make an connection to the URL and getting the weather information from a JSON object
 * Tested the URL in POSTMAN https://www.getpostman.com/
 * and figure out what information i would like to get and create an Weather object of it
 * This class is a ASync class because we dont want the UI to hangup while waiting for response from server
 */
public class Weather extends AsyncTask<Void, Void, StringBuilder> implements ImageASync {

    private final String WEATHER_API_KEY = "your api key";
    private final String API_CALL_OSLO_WEATHER = "http://api.openweathermap.org/data/2.5/weather?q=Oslo&"+WEATHER_API_KEY;
    private WeatherASync weatherASync;

    /**
     * Instantiates a new Weather.
     *
     * @param mainMenuFragment the main menu fragment
     */
    public Weather(MainMenuFragment mainMenuFragment) {
        weatherASync = mainMenuFragment;
    }

    /**
     * Creating connection to the API to fetch weather and read the raw response in background
     * @param voids
     * @return JSON response string
     */
    @Override
    protected StringBuilder doInBackground(Void... voids) {
        StringBuilder weatherString = new StringBuilder();
        try {
            URL url = new URL(API_CALL_OSLO_WEATHER);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String readLine = "";
            while(readLine != null) {
                readLine = bufferedReader.readLine();
                weatherString.append(readLine);
            }
        } catch (MalformedURLException malFormedURLException) {
            Log.d("MALFORMEDEXCEPTION: ", malFormedURLException.getMessage());
        } catch (IOException ioExepction) {
            Log.d("IOEXEPTION: ", ioExepction.getMessage());
        }
        // Contains each lines from response from server
        return weatherString;
    }


    /**
     * Getting weatherString from method above, which contains the response
     * Now fetch the information out from the weatherString
     * Convert the raw respons to JSON object
     * @param stringBuilder
     */
    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        try {
            JSONObject weatherJSON = new JSONObject(stringBuilder.toString()); // sending weatherString in parameter

            // Initialize JSONObject to be ready for parsing
            JSONArray weatherArrayObject = null;
            JSONObject weatherMainObject = null;
            JSONObject weatherWind = null;
            String cityName = null;

            // Start parsing JSONObject
            if (weatherJSON.has("main")) {
                weatherMainObject = weatherJSON.getJSONObject("main");
            }

            if (weatherJSON.has("weather")) {
                weatherArrayObject = (JSONArray) weatherJSON.get("weather");
            }

            if(weatherJSON.has("wind")) {
                weatherWind = weatherJSON.getJSONObject("wind");
            }

            if(weatherJSON.has("name")) {
                cityName = weatherJSON.getString("name");
            }

            // Now validate
            parseJSONAndValidateWeatherObject(weatherMainObject, weatherWind, weatherArrayObject, cityName);
        } catch (JSONException jsonException) {
            Log.d("JSONEXCEPTION: ", jsonException.getMessage());
        } catch (Exception ex) {
            Log.d("EXCETPION: ", ex.getMessage());
        }
    }

    /**
     * Check if the information is valid
     * If information is good, make an weatherObject to get information over to MainMenuFragment
     *
     * Validating if parsed information is not empty or null
     * Creating new WeatherObject to parse data further.
     * @param mainObject
     * @param windObject
     * @param jsonArray
     * @param cityName
     */
    private void parseJSONAndValidateWeatherObject(JSONObject mainObject, JSONObject windObject, JSONArray jsonArray, String cityName) {
        if(mainObject != null && jsonArray != null && windObject != null && jsonArray.length() > 0 && cityName != null && !cityName.isEmpty()) {
            // If valid, make an WeatherObject
            new WeatherObject(mainObject, jsonArray, windObject, this, cityName);
        }
    }

    /**
     * Image async callback
     *
     * Listens to when image is done downloading, and lets MainMenuFragment know with the parsed WeatherObject.
     * @param weatherObject
     */
    @Override
    public void getImage(WeatherObject weatherObject) {
        weatherASync.getWeatherObject(weatherObject);
    }
}
