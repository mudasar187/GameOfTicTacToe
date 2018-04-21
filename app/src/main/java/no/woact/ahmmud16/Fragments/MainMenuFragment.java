package no.woact.ahmmud16.Fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import no.woact.ahmmud16.Interfaces.WeatherASync;
import no.woact.ahmmud16.FragmentLoading.FragmentLoading;
import no.woact.ahmmud16.R;
import no.woact.ahmmud16.Weather.Weather;
import no.woact.ahmmud16.Weather.WeatherObject;


/**
 * This fragment contains the Menu options with weather information
 * This class loads when SplashScreenFragment is finished
 */
public class MainMenuFragment extends FragmentLoading implements View.OnClickListener, WeatherASync {

    private TextView headerGameOfTicTacToeTextView, cityTextView, weatherTempTextView, weatherWindSpeedTextView, weatherDescriptionTextView, celiusTextView;
    private ImageView weatherImage, weatherWindImageView;
    private Button newGameBtn, highscoreBtn;
    private Weather weather;
    private WeatherObject weatherObject;
    private ConstraintLayout mainMenuFragmentLayout;
    private HighscoreFragment highscoreFragment;
    private ChooseGameModusFragment chooseGameModusFragment;

    public MainMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        // Initialize resources and animations
        initializeResources(view);
        initalizeAnimation();
        setClickListeners();
        return view;
    }

    /**
     * Initialize resources
     * @param view
     */
    private void initializeResources(View view) {
        chooseGameModusFragment = new ChooseGameModusFragment();
        highscoreFragment = new HighscoreFragment();
        weather = new Weather(this);
        celiusTextView = view.findViewById(R.id.celiusTextView);
        celiusTextView.setVisibility(View.INVISIBLE);
        mainMenuFragmentLayout = view.findViewById(R.id.mainMenuFragmentLayout);
        headerGameOfTicTacToeTextView = view.findViewById(R.id.headerGameOfTicTacToeTextView);
        cityTextView = view.findViewById(R.id.cityTextView);
        weatherDescriptionTextView = view.findViewById(R.id.weatherDescriptionTextView);
        weatherTempTextView = view.findViewById(R.id.weatherTempTextView);
        weatherWindSpeedTextView = view.findViewById(R.id.weatherWindSpeedTextView);
        weatherImage = view.findViewById(R.id.weatherImageView);
        weatherWindImageView = view.findViewById(R.id.weatherWindImageView);
        weatherWindImageView.setVisibility(View.INVISIBLE);
        newGameBtn = view.findViewById(R.id.newGameBtn);
        highscoreBtn = view.findViewById(R.id.highScoreBtn);
        weather.execute();
    }

    /**
     * Initialize animations
     */
    private void initalizeAnimation() {
        loadFragmentOneAnimation(getActivity(), R.anim.frominvisibletovisible, mainMenuFragmentLayout);
    }

    /**
     * Set click listeners
     */
    private void setClickListeners() {
        newGameBtn.setOnClickListener(this);
        highscoreBtn.setOnClickListener(this);
    }

    /**
     * This methods checks if there is already downloaded weather
     * If weather is not downloaded, then download it
     * If yes, update UI with the weather with the weatherObject that already is downloaded
     */
    @Override
    public void onResume() {
        super.onResume();
        if (weatherObject != null) {
            this.updateUIWithWeather(weatherObject);
        }
    }

    /**
     * If getMain() is not empty, update UI with the weather
     * @param weatherObject
     */
    @Override
    public void getWeatherObject(WeatherObject weatherObject) {
        if (!weatherObject.getMain().equals("")) {
            this.updateUIWithWeather(weatherObject);
        }
    }

    /**
     * Insert the widgets with information received from weatherObject
     * @param weatherObject
     */
    private void updateUIWithWeather(WeatherObject weatherObject) {
        this.cityTextView.setText(weatherObject.getCityname());
        this.weatherDescriptionTextView.setText(weatherObject.getDescription());
        this.weatherTempTextView.setText(String.valueOf(weatherObject.getConvertedTemp()));
        this.weatherWindSpeedTextView.setText(String.valueOf(weatherObject.getWindSpeed()));
        this.weatherImage.setImageBitmap(weatherObject.getImageBitMap());
        this.weatherWindImageView.setVisibility(View.VISIBLE);
        this.celiusTextView.setVisibility(View.VISIBLE);
        this.weatherObject = weatherObject;
    }

    /**
     * Switch method for which button have been pressed
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newGameBtn:
                loadFragmentWithBackStack(getActivity(),R.id.fragmentContainer, chooseGameModusFragment);
                break;
            case  R.id.highScoreBtn:
                loadFragmentWithBackStack(getActivity(),R.id.fragmentContainer, highscoreFragment);
                break;
        }
    }
}