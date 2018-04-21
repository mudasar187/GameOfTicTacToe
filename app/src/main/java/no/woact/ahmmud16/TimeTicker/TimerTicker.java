package no.woact.ahmmud16.TimeTicker;

import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

/**
 * THis class control the time ticker in GameBoardFragment
 */
import no.woact.ahmmud16.R;

/**
 * The type Timer ticker.
 */
public class TimerTicker {

    private Chronometer chronometer;
    private long whenTimeStopped;

    public TimerTicker(View view) {
        chronometer = view.findViewById(R.id.chronometerTextView);
        whenTimeStopped = 0;
    }

    /**
     * Start chronometer
     */
    public void startTimer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    /**
     * Stop chronometer
     */
    public void stopTimer() {
        chronometer.stop();
    }

    /**
     * Pause chronometer and save where timer stopped
     */
    public void pauseTimer() {
        whenTimeStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
    }

    /**
     * Resume timer from where it stopped
     */
    public void resumeTimer() {
        if(whenTimeStopped != 0) {
            chronometer.setBase(SystemClock.elapsedRealtime() + whenTimeStopped);
            chronometer.start();
        }
    }
}
