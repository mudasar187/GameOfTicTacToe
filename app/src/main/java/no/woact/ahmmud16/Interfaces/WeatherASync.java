package no.woact.ahmmud16.Interfaces;

import no.woact.ahmmud16.Weather.WeatherObject;

/**
 * Interface class for getting weather object
 */
public interface WeatherASync {

    /**
     *  Makes it possible to notify MainMenuFragment to update UI with given object
     *
     * @param weatherObject the weather object
     */
    void getWeatherObject(WeatherObject weatherObject);
}
