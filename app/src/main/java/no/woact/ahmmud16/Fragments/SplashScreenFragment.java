package no.woact.ahmmud16.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import no.woact.ahmmud16.FragmentLoading.FragmentLoading;
import no.woact.ahmmud16.R;


/**
 * This is the loading SplashScreen when you start up 'The Game of TicTacToe'
 */
public class SplashScreenFragment extends FragmentLoading {

    private LinearLayout firstLinerLayout, secondLinerLayOut;
    private TextView introTextView;
    private MainMenuFragment mainMenuFragment;
    private Handler handler;

    public SplashScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        // Initialize resources and animations
        initializeResources(view);
        initializeAnimation();
        loadFragment();
        return view;
    }

    /**
     * Initialize resources
     * @param view
     */
    private void initializeResources(View view) {
        mainMenuFragment = new MainMenuFragment();
        handler = new Handler();
        firstLinerLayout = view.findViewById(R.id.firstLinerLayout);
        secondLinerLayOut = view.findViewById(R.id.secondLinerLayout);
        introTextView = view.findViewById(R.id.introTextView);
    }

    /**
     * Initialize animations
     */
    private void initializeAnimation() {
        loadFragmentTwoAnimation(getActivity(), R.anim.uptodownin, R.anim.downtoupin, firstLinerLayout, secondLinerLayOut);
    }

    /**
     * When starting this application this SplashScreenFragment load first, then it automatically load MainMenuFragment after 2000ms
     * Loading MainMenuFragment without backstack, this is because user cannot go back to intro fragment if user press onBackpressed()
     * So if user press onBackpressed() it will quit the application
     */
    public void loadFragment() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFragmentWithOutBackStack(getActivity(),R.id.fragmentContainer, mainMenuFragment);
            }
        }, 2000);
    }
}
