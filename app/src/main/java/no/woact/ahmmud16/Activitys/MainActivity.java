package no.woact.ahmmud16.Activitys;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import no.woact.ahmmud16.Database.Handler.DatabaseHandler;
import no.woact.ahmmud16.Fragments.SplashScreenFragment;
import no.woact.ahmmud16.MusicPlayer.MusicPlayer;
import no.woact.ahmmud16.R;


/**
 * This activity represent a frame for all fragment that are used in this application
 */
public class MainActivity extends AppCompatActivity  {

    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private SplashScreenFragment splashScreenFragment;
    private DatabaseHandler databaseHandler;
    private MusicPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize resources and load MainMenuFragment
        initializeResources();
        loadFragment();

        // Add TTTBot to database once application runs first time
        runCodeOnlyOnceWhenApplicationInstalled();
    }

    /**
     * This code only runs once when first time application is installed
     * I want to avoid adding TTTBot to database every time i want to play against TTTBot
     * So here it will add TTTBot to database only ONCE
     * https://stackoverflow.com/questions/15061653/run-a-piece-of-code-only-once-when-an-application-is-installed?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
     */
    private void runCodeOnlyOnceWhenApplicationInstalled() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if(firstStart) {
            // Add TTTBOT to database
            databaseHandler.addTTTBotAtFirstTime();
        }
        setSharedPreferencesToFalse();
    }

    /**
     * Use SharedPreferences to set false to not run code again
     */
    private void setSharedPreferencesToFalse() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    /**
     * Initialize resources
     */
    private void initializeResources() {
        frameLayout = findViewById(R.id.fragmentContainer);
        splashScreenFragment = new SplashScreenFragment();
        databaseHandler = new DatabaseHandler(getApplicationContext());
        musicPlayer = new MusicPlayer();
    }

    /**
     * This method load the SplashScreenFragment when MainActivity starts
     */
    private void loadFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction().add(R.id.fragmentContainer, splashScreenFragment, null);
        fragmentTransaction.commit();
    }

    /**
     * Play soundtrack when onStart() is called, if i put this method in onCreate it will crash and give you exception
     */
    @Override
    public void onStart() {
        super.onStart();
        musicPlayer.playSoundtrack(this, R.raw.appmusic);
    }

    /**
     * Resume soundtrack from where it stopped
     */
    @Override
    protected void onResume() {
        super.onResume();
        musicPlayer.resumeSoundTrack();
    }

    /**
     * Pause soundtrack when onPause() is called, this is to avoid music not playing while application is not in use
     */
    @Override
    protected void onPause() {
        super.onPause();
        musicPlayer.pauseSoundTrack();
    }

    /**
     * Stop soundtrack when onStop() is called, this is to avoid music not playing while application is not in use
     */
    @Override
    protected void onStop() {
        super.onStop();
        musicPlayer.stopSoundTrack();
    }
}