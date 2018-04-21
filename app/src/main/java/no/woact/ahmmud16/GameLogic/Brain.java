package no.woact.ahmmud16.GameLogic;

import java.util.ArrayList;
import java.util.Arrays;

import no.woact.ahmmud16.Fragments.GameBoardFragment;
import no.woact.ahmmud16.GameLogic.AI.Controller;

/**
 * This class is the brain, checking the combinations
 */
public class Brain {

    /**
     * The Game board.
     */
    // Static because of usage out of this class
    public static ArrayList<Integer> gameBoard;

    /**
     * The Combinations.
     */
    public static ArrayList<ArrayList<Integer>> combinations;

    public Brain() {

        // Initialize index's, contains 1 and 2 when player choose
        gameBoard = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0));

        // Initialize combinations
        combinations = new ArrayList<>();
        combinations.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        combinations.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
        combinations.add(new ArrayList<>(Arrays.asList(7, 8, 9)));
        combinations.add(new ArrayList<>(Arrays.asList(1, 4, 7)));
        combinations.add(new ArrayList<>(Arrays.asList(2, 5, 8)));
        combinations.add(new ArrayList<>(Arrays.asList(3, 6, 9)));
        combinations.add(new ArrayList<>(Arrays.asList(1, 5, 9)));
        combinations.add(new ArrayList<>(Arrays.asList(3, 5, 7)));

    }

    /**
     * Checking combinations
     */
    private void checkCombinations(GameBoardFragment gameBoardFragment) {

        Boolean winnerFound = null;

        // Checking each array inside arraylist
        for (ArrayList<Integer> array: combinations) {
            if (gameBoard.get(array.get(0) - 1) == 1 && gameBoard.get(array.get(1) - 1) == 1 && gameBoard.get(array.get(2) - 1) == 1) {

                // Player 1 won, set winnerFound = true;
                winnerFound = true;

                // Reset gameboard
                resetGameBoard();

                // Show winLooseDrawDialog
                gameBoardFragment.showWhenPlayerWinDialog(true);
                break;
            }

            if (gameBoard.get(array.get(0) - 1) == 2 && gameBoard.get(array.get(1) - 1) == 2 && gameBoard.get(array.get(2) - 1) == 2) {

                // Player 2 won, set winnerFound = false
                winnerFound = false;

                // Reset gameBoard
                resetGameBoard();

                // Show winLooseDrawDialog
                gameBoardFragment.showWhenPlayerWinDialog(false);
                break;
            }
        }

        if (winnerFound == null) {
            if (!gameBoard.contains(0)) {

                // Draw if no one win
                resetGameBoard();

                // Show winLooseDrawDialog
                gameBoardFragment.showWhenPlayerWinDialog(null);
            }
        }
    }

    /**
     * Registrate who tap the button and put inside array and then call checkCombinations() method to check if someone win
     * This method is called from GameBoardFragment
     *
     * @param index             the index
     * @param gameBoardFragment the game board fragment
     * @param aiActivated       the ai activated
     */
    public void getBtnTapped(int index, GameBoardFragment gameBoardFragment, boolean aiActivated) {

        // Enter who clicked which button.
        int playerValue;

        // Check who's turn it is to give playerValue a value
        if (gameBoardFragment.playerOne) {
            playerValue = 1; // If its player one set number 1 to gameBoard index value receive from parameter
        } else {
            playerValue = 2; // If its player two set number 2 to gameBoard index value receive from parameter
        }

        // Setting playerValue to choosen index in gameBoard
        gameBoard.set(chooseMove(aiActivated, gameBoardFragment.playerOne, index), playerValue);

        // Check if player one win or draw
        checkCombinations(gameBoardFragment);

        // If AI is activated then player turn is switched.
        gameBoardFragment.playerOne = !gameBoardFragment.playerOne;

        // If AI is activated bot makes turn, configuring gameBoard and configuring chosen imageButton in GameBoardFragment.
        // If not activated, this is ignored
        goBot(aiActivated, gameBoardFragment);

    }

    /**
     * If bot is activated and its player two's turn
     * If gameboard has more untaken spots left, AI makes move, configuring imageButton in fragment, switching turn and checking for win or draw.
     *
     * @param aiActivated       the ai activated
     * @param gameBoardFragment the game board fragment
     */
    public void goBot(boolean aiActivated, GameBoardFragment gameBoardFragment) {
        // If activated and not player one's turn
        if (aiActivated && !gameBoardFragment.playerOne) {

            // Check if there any 0 value left in gameBoard, then AI make move otherwise not
            if (gameBoard.contains(0)) {

                // Get best possible move from chooseMove() method below
                int aiMove = chooseMove(true, false, null);

                // Set the possible move to index in gameBoard array to value two
                gameBoard.set(aiMove, 2);

                // Configuring imageButton
                gameBoardFragment.setImageForAI(aiMove);

                // Switch turn
                gameBoardFragment.playerOne = !gameBoardFragment.playerOne;

                // Check combinations for win or draw
                checkCombinations(gameBoardFragment);
            }
        }
    }

    /**
     * Init Controller and finds the best move
     * @param aiActivated
     * @param playerOne
     * @param index
     * @return
     */
    private int chooseMove(boolean aiActivated, boolean playerOne, Integer index) {

        // Init controller
        Controller aiController = new Controller();

        int choosenIndex;

        // If AI activated and not players turn
        if (aiActivated && !playerOne) {
            // Get best choosenIndex from Controller
            choosenIndex = aiController.checkBestPossibleMoves() - 1;
        } else {
            // If not AI/player two's turn
            choosenIndex = index;
        }
        return choosenIndex;
    }

    /**
     * Reset board when someone win or draw
     */
    private void resetGameBoard() {

        for(int num : gameBoard) {
            num = 0;
        }
    }
}
