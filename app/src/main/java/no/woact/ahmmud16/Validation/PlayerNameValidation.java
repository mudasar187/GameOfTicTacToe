package no.woact.ahmmud16.Validation;

import android.app.Activity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class contains validations for the playernames and depend on the conditions, send a toast message
 * Reason for this is to ensure all names entered must be validated
 */
public class PlayerNameValidation {

    /**
     * Check one player
     * Also avoid that player can enter TTTBOT as player name
     * @param activity   the activity
     * @param playerName the player name
     * @return boolean
     */
    public boolean validateOnePlayer(Activity activity, String playerName) {

        if (playerName.trim().isEmpty()) {
            showText(activity, "ENTER YOUR PLAYERNAME");
            return false;
        } else if(playerName.equals("TTTBOT")) {
            showText(activity, "PLAYERNAME NOT AVAILABLE");
            return false;
        } else if(playerName.trim().length() <= 2) {
            showText(activity, "SHORT PLAYERNAME, MINIMUM 3 LETTERS");
            return false;
        }
        return true;
    }

    /**
     * Check two players
     * Also avoid that player can enter TTTBOT as player name
     * @param activity      the activity
     * @param playerNameOne the player name one
     * @param playerNameTwo the player name two
     * @return boolean
     */
    public boolean validateTwoPlayer(Activity activity, String playerNameOne, String playerNameTwo) {

        if(playerNameOne.trim().isEmpty() && playerNameTwo.trim().isEmpty()) {
            showText(activity, "MUST ENTER BOTH PLAYERNAMES");
            return false;
        } else if(playerNameOne.trim().isEmpty() || playerNameTwo.trim().isEmpty()) {
            showText(activity, "MUST ENTER BOTH PLAYERNAMES");
            return false;
        } else if(playerNameOne.trim().equals("") && playerNameTwo.trim().equals("")) {
            showText(activity, "MUST ENTER BOTH PLAYERNAMES");
            return false;
        } else if(playerNameOne.trim().equals("") || playerNameTwo.trim().equals("")) {
            showText(activity, "MUST ENTER BOTH PLAYERNAMES");
            return false;
        } else if(playerNameOne.trim().equals(playerNameTwo.trim())) {
            showText(activity, "PLAYERNAME MUST BE UNIQUE");
            return false;
        } else if(playerNameOne.trim().length() <= 2 && playerNameTwo.trim().length() <= 2) {
            showText(activity, "BOTH PLAYERNAMES SHORT, MINIMUM 3 LETTERS");
            return false;
        } else if(playerNameOne.trim().length() <= 2 || playerNameTwo.trim().length() <= 2) {
            showText(activity, "ONE PLAYER HAVE TO SHORT NAME, MINIMUM 3 LETTERS");
            return false;
        } else if(playerNameOne.equals("TTTBOT")) {
            showText(activity, "PLAYERNAME ONE NOT AVAILABLE");
            return false;
        } else if(playerNameTwo.equals("TTTBOT")) {
            showText(activity, "PLAYERNAME TWO NOT AVAILABLE");
            return false;
        }
        return true;
    }

    /**
     * Customize toast message
     * Reason for that is so i can show the toast in center with the text centred
     * If text is to long, text will be centered
     * @param activity
     * @param message
     */
    private void showText(Activity activity, String message) {

        Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        LinearLayout layout = (LinearLayout) toast.getView();

        if (layout.getChildCount() > 0) {
            TextView tv = (TextView) layout.getChildAt(0);
            tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
        toast.show();
    }

}
