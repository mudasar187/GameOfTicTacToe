package no.woact.ahmmud16.Weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

import no.woact.ahmmud16.Interfaces.ImageASync;

/**
 * This class contains method for getting the image from Weather json file from the URL and download it
 */
public class WeatherImageDownload extends AsyncTask<Void, Void, Bitmap> {

    private WeatherObject weatherObject;
    private ImageASync imageASync;
    private String imageDownloadUrl;


    /**
     * Get weatherObject which is parsed and then set image async interface and set new imageobject
     * <p>
     * Getting image bitmap in an Async task
     * When done downloading, parse bitmap, and let Weather object know that the image is done and ready.
     *
     * @param weatherObject the weather object
     * @param weather       the weather
     */
    public WeatherImageDownload(WeatherObject weatherObject, Weather weather) {
        this.weatherObject = weatherObject;
        this.imageASync = weather;
        imageDownloadUrl = "https://openweathermap.org/img/w/" + weatherObject.getIcon() + ".png";
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Background process for downloading image
     * @param voids
     * @return bitmap
     */
    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = null;
        try {
            InputStream imageStream = new URL(imageDownloadUrl).openStream();
            bitmap = BitmapFactory.decodeStream(imageStream);
        } catch (Exception exception) {
            Log.d("EXCEPTION: ", exception.getMessage());
        }
        return bitmap;
    }

    /**
     * Running this method when doInBackground() method above is finished
     * Parsing Bitmap from image to weatherobject
     * @param bitmap
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        this.weatherObject.setImageBitMap(bitmap);
        imageASync.getImage(this.weatherObject); // Sending message to Weather that image is downloaded
    }
}
