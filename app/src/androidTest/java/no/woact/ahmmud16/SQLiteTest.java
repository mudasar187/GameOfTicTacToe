package no.woact.ahmmud16;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import no.woact.ahmmud16.Database.Constants.Constants;
import no.woact.ahmmud16.Database.Handler.DatabaseHandler;
import no.woact.ahmmud16.Database.Model.Player;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SQLiteTest {

    private DatabaseHandler databaseHandler;
    private String testBot = "TESTBOT";

    @Before
    public void setUp() {
        databaseHandler = new DatabaseHandler(InstrumentationRegistry.getTargetContext());
    }

    @After
    public void tearDown() {
        databaseHandler.close();
    }

    /**
     * Check if DatabaseHandler is initialized and not null
     */
    @Test
    public void testIfDatabaseIsNotNull() {
        assertNotNull(databaseHandler);
    }

    /**
     * Add dummy user to database and check exists
     */
    @Test
    public void checkIfUserExistsInDatabase() {

        databaseHandler.addNewUser(testBot);

        boolean check = databaseHandler.checkIfUserExists(testBot);

        assertTrue(check);

    }

    /**
     * Check if non exists user exists in database
     */
    @Test
    public void checkIfNonExistsUserIsInDatabase() {
        String dummyUserNotExists = "TESTBOT1";

        boolean check = databaseHandler.checkIfUserExists(dummyUserNotExists);

        assertFalse(check);
    }

    /**
     * Update win and check if it was updated
     */
    @Test
    public void testUpdateWin() {

        databaseHandler.updateStats(testBot, Constants.KEY_WIN);

        int win = getPlayerStats(testBot).getWin();

        assertEquals(1, win);
    }

    /**
     * Update draw and check if it was updated
     */
    @Test
    public void testUpdateDraw() {

        databaseHandler.updateStats(testBot, Constants.KEY_DRAW);

        int draw = getPlayerStats(testBot).getDraw();

        assertEquals(1, draw);
    }

    /**
     * Update loss and check if it was updated
     */
    @Test
    public void testUpdateLoss() {

        databaseHandler.updateStats(testBot, Constants.KEY_LOSS);

        int loss = getPlayerStats(testBot).getDraw();

        assertEquals(1, loss);

        deleteUserFromTable(testBot); // Delete dummy user after last test
    }


    /**
     * Method for getting stats from dummy user, using this for getting the stats for player
     */
    private Player getPlayerStats(String playerName) {
        SQLiteDatabase db = databaseHandler.getReadableDatabase();

        String GET_PLAYER = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.KEY_PLAYER_NAME
                + " = ?";
        Cursor cursor = db.rawQuery(GET_PLAYER, new String[]{playerName});

        if (cursor.moveToFirst()) {

            Player player = new Player();
            player.setPlayerName(cursor.getString(cursor.getColumnIndex(Constants.KEY_PLAYER_NAME)));
            player.setWin(cursor.getInt(cursor.getColumnIndex(Constants.KEY_WIN)));
            player.setDraw(cursor.getInt(cursor.getColumnIndex(Constants.KEY_DRAW)));
            player.setLoss(cursor.getInt(cursor.getColumnIndex(Constants.KEY_LOSS)));

            return player;
        }

        cursor.close();
        db.close();

        return null;
    }

    /**
     * Delete dummy user after tests
     *
     * @param playerName
     */
    private void deleteUserFromTable(String playerName) {

        SQLiteDatabase db = databaseHandler.getReadableDatabase();

        databaseHandler.getReadableDatabase();


        String whereClause = Constants.KEY_PLAYER_NAME + "=?";
        String[] whereArgs = new String[]{playerName};

        db.delete(Constants.TABLE_NAME, whereClause, whereArgs);

        db.close();
    }
}
