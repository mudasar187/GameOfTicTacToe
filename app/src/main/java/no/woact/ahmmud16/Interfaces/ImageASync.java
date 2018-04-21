package no.woact.ahmmud16.Interfaces;

import no.woact.ahmmud16.Weather.WeatherObject;

/**
 * Interface class for getting image for weather
 */
public interface ImageASync {

    /**
     * This interface is implemented to make it possible for Weatherclass to get notified when image is done downloading
     *
     * @param weatherObject the weather object
     */
    void getImage(WeatherObject weatherObject);
}
