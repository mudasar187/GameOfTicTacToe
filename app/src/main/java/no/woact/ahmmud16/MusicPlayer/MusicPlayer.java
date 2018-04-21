package no.woact.ahmmud16.MusicPlayer;

import android.app.Activity;
import android.media.MediaPlayer;

/**
 * This class control the soundtrack in MainActivity
 */
public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private int soundLength;

    public MusicPlayer() {
        mediaPlayer = new MediaPlayer();
        soundLength = 0;
    }

    /**
     * Create mediaplayer and play soundtrack
     *
     * @param activity   the activity
     * @param soundTrack the sound track
     */
    public void playSoundtrack(Activity activity, int soundTrack) {
        mediaPlayer = MediaPlayer.create(activity, soundTrack);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    /**
     * Release mediaplayer
     */
    public void stopSoundTrack() {
        mediaPlayer.release();
    }

    /**
     * Pause soundtrack and save where soundtrack stopped
     */
    public void pauseSoundTrack() {
        mediaPlayer.pause();
        soundLength = mediaPlayer.getCurrentPosition();
    }

    /**
     * Resume the soundtrack from where soundtrack stopped
     */
    public void resumeSoundTrack() {
        mediaPlayer.seekTo(soundLength);
        mediaPlayer.start();
    }
}
