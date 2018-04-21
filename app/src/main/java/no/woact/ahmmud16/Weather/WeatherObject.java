package no.woact.ahmmud16.Weather;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import no.woact.ahmmud16.Interfaces.ImageASync;

/**
 * Weather object class
 */
public class WeatherObject {

    private String main, description, icon, cityname;
    private Double temp, windSpeed;
    private Integer windInt;
    private Bitmap imageBitMap;
    private ImageASync imageASync;

    /**
     * Create an object of Weather so i can get information in MainMenuFragment class
     * <p>
     * Parsing incoming data into local variables
     * Setting imageAsync listener to the Weather object from parameter
     * Creating new object of WeatherImageDownload to download the image for the weather
     * Calling execute on image download
     *
     * @param mainObject the main object
     * @param jsonArray  the json array
     * @param windObject the wind object
     * @param weather    the weather
     * @param cityName   the city name
     */
    public WeatherObject(JSONObject mainObject, JSONArray jsonArray, JSONObject windObject, Weather weather, String cityName) {
        try {
            JSONObject getFirstObjectInArray = (JSONObject) jsonArray.get(0);

            setCityname(cityName);
            setMain(getFirstObjectInArray.getString("main"));
            setDescription(getFirstObjectInArray.getString("description"));
            setIcon(getFirstObjectInArray.getString("icon"));
            temp = (Double) mainObject.get("temp");

            // Reason for this try/catch is sometimes the api have integer as speed or double
            // Handling this with try/catch to set winspeed based on if its double or integer
            try {
                setWindSpeedDouble((Double) windObject.get("speed"));
            } catch (Exception e) {
                setWindSpeedInt((Integer) windObject.get("speed"));
            }

            imageASync = weather; //
            WeatherImageDownload weatherImageDownload = new WeatherImageDownload(this, weather); // Initialize object
            weatherImageDownload.execute(); // Start background process

        } catch (JSONException jsonException) {
            Log.d("JSONEXCEPTION: ", jsonException.getMessage());
        }
    }

    /**
     * Gets cityname.
     *
     * @return the cityname
     */
    public String getCityname() {
        return cityname;
    }

    /**
     * Sets cityname.
     *
     * @param cityname the cityname
     */
    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    /**
     * Gets temp.
     *
     * @return the temp
     */
    public Double getTemp() {
        return this.temp;
    }

    /**
     * Sets image bit map.
     *
     * @param bitMap the bit map
     */
    public void setImageBitMap(Bitmap bitMap) {
        this.imageBitMap = bitMap;
    }

    /**
     * Gets image bit map.
     *
     * @return the image bit map
     */
    public Bitmap getImageBitMap() {
        return this.imageBitMap;
    }

    /**
     * Gets main.
     *
     * @return the main
     */
    public String getMain() {
        return main;
    }

    private void setMain(String main) {
        this.main = main;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    private void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Gets converted temp.
     *
     * @return the converted temp
     */
    public Integer getConvertedTemp() {
        return (int) Math.round(this.getTemp() - 273.5);
    }

    /**
     * Gets wind speed.
     *
     * @return the wind speed
     */
    public Object getWindSpeed() {
        if (this.windInt != null) {
            return this.windInt;
        } else {
            return this.windSpeed;
        }
    }

    private void setWindSpeedInt(Integer windSpeedInt) {
        this.windInt = windSpeedInt;
    }

    private void setWindSpeedDouble(Double windSpeedDouble) {
        this.windSpeed = windSpeedDouble;
    }
}
