package no.woact.ahmmud16.Database.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

import no.woact.ahmmud16.Database.Constants.Constants;
import no.woact.ahmmud16.Database.Model.Player;

/**
 * DatabaseHandler class, contains all operations related to database
 * Create and upgrade table methods
 * CRUD operations methods
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    /**
     * Instantiates a new Database handler.
     *
     * @param context the context
     */
    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    /**
     * Create table
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table
        String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY, " + Constants.KEY_PLAYER_NAME
                + " TEXT, " + Constants.KEY_WIN + " INT, " + Constants.KEY_DRAW + " INT, "
                + Constants.KEY_LOSS + " INT);";

        db.execSQL(CREATE_TABLE);
    }

    /**
     * Upgrade table if any changes
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Update database if any changes added
        String UPGRADE_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;

        db.execSQL(UPGRADE_TABLE);
    }

    /**
     * This method is to add TTTBot to table at first time table is created
     * Called only once in MainActivity class with SharedPreferences method
     */
    public void addTTTBotAtFirstTime() {

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_PLAYER_NAME, "TTTBOT");

        db.insert(Constants.TABLE_NAME, null, values);

        db.close();
    }

    /**
     * Add a new user to the table when user enter their name in SingelPlayerFragment and MultiPlayerFragment
     *
     * @param playerName the player name
     */
    public void addNewUser(String playerName) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.KEY_PLAYER_NAME, playerName);

        db.insert(Constants.TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Checks if a user exists in database
     * When user enter their name in SingerPlayerFragment or MultiPlayerFragment, it will check user before
     * starting game when they click "START GAME", and show dialog if user exists to inform that user exists in database
     * If not exists, add user and start game
     *
     * @param playerName the player name
     * @return boolean
     */
    public boolean checkIfUserExists(String playerName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String FIND_USER = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.KEY_PLAYER_NAME
                + " = ?";

        Cursor cursor = db.rawQuery(FIND_USER, new String[]{playerName});

        boolean isFound = (cursor.getCount() > 0);
        cursor.close();
        db.close();

        return isFound;
    }

    /**
     * Update player's stats, when user win, draw or loose
     *
     * @param playername the playername
     * @param columnName the column name
     * @return boolean
     */
    public boolean updateStats(String playername, String columnName) {

        SQLiteDatabase db = this.getWritableDatabase();

        String WHERE_CLAUSE = Constants.KEY_PLAYER_NAME + "=?";
        String[] WHERE_ARGS = new String[]{playername};

        int defaultValue = -1; // default to not update

        Cursor cursor = db.query(
                Constants.TABLE_NAME,
                null,
                WHERE_CLAUSE,
                WHERE_ARGS,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            defaultValue = cursor.getInt(cursor.getColumnIndex(columnName)) + 1;
        }
        cursor.close();
        if (defaultValue < 1) {
            db.close();
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(columnName,defaultValue);

        if (db.update(Constants.TABLE_NAME,values,WHERE_CLAUSE,WHERE_ARGS) > 0) {
            db.close();
            return true;
        }

        db.close();
        return false;
    }

    /**
     * Get all rows from table
     * Add to arraylist
     * Send to adapter that show the players in RecyclerView
     *
     * @return player list
     */
    public ArrayList<Player> getPlayerList() {

        ArrayList<Player> playerList = new ArrayList<>();

        playerList.clear();

        SQLiteDatabase db = this.getReadableDatabase();

        String SELECT_ALL = "SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY " + Constants.KEY_WIN + " DESC";
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if(cursor.moveToFirst()) {
            do {
                Player player = new Player();
                player.setPlayerName(cursor.getString(cursor.getColumnIndex(Constants.KEY_PLAYER_NAME)));
                player.setWin(cursor.getInt(cursor.getColumnIndex(Constants.KEY_WIN)));
                player.setDraw(cursor.getInt(cursor.getColumnIndex(Constants.KEY_DRAW)));
                player.setLoss(cursor.getInt(cursor.getColumnIndex(Constants.KEY_LOSS)));

                playerList.add(player);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return playerList;
    }
}
