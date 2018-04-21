package no.woact.ahmmud16.Database.Model;

import java.io.Serializable;

/**
 * Player model for database
 */
public class Player implements Serializable {

    private int id;
    private String playerName;
    private int win;
    private int draw;
    private int loss;


    public Player() {
        // Empty constructor
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets player name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets player name.
     *
     * @param username the username
     */
    public void setPlayerName(String username) {
        this.playerName = username;
    }

    /**
     * Gets win.
     *
     * @return the win
     */
    public int getWin() {
        return win;
    }

    /**
     * Sets win.
     *
     * @param win the win
     */
    public void setWin(int win) {
        this.win = win;
    }

    /**
     * Gets draw.
     *
     * @return the draw
     */
    public int getDraw() {
        return draw;
    }

    /**
     * Sets draw.
     *
     * @param draw the draw
     */
    public void setDraw(int draw) {
        this.draw = draw;
    }

    /**
     * Gets loss.
     *
     * @return the loss
     */
    public int getLoss() {
        return loss;
    }

    /**
     * Sets loss.
     *
     * @param loss the loss
     */
    public void setLoss(int loss) {
        this.loss = loss;
    }
}
