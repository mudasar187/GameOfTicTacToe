package no.woact.ahmmud16.GameLogic.AI;

import java.util.ArrayList;

import no.woact.ahmmud16.GameLogic.Brain;

/**
 * This class is a controller for AI, which returns the best possible move
 */
public class Controller {

    private int upperLeftCorner = 1;
    private int upperRightCorner = 3;
    private int lowerLeftCorner = 7;
    private int lowerRightCorner = 9;
    private int middleOfTheBoard = 5;
    private boolean cornerIsDefended;

    public Controller() {
        this.cornerIsDefended = false;
    }

    /**
     * Return the best move based on all private methods below
     *
     * @return int
     */
    public int checkBestPossibleMoves() {

        Integer moveNumber = winNum(); // Check for any win number

        if (moveNumber != null) {
            // If winNumber has value (wich is not null), then win number is returned
            // If this happends, bot wins
            return moveNumber;
        } else {
            // Here we know winNumber is null
            // We call dangerNum to get the last number player one needs to block
            // Now moveNumber contains the number player one need to win, if it exists
            moveNumber = dangerNum();
            if (moveNumber != null) {
                // If it exists then its returned to block player one
                return moveNumber;
            } else {
                // If its null, then it means that player one is not close to win, so there is nothing to block
                // The best possible number is then requested by possibleNum() method
                moveNumber = possibleNum();
                // Move number now contains the best possible number.
            }
        }
        // Now, moveNumber is the best possible number
        // Because if bot is missing 1 to win or player one is missing 1 to win, it would have been returned before
        return moveNumber;
    }

    /**
     * Checking if bot can win, and return win number
     * @return
     */
    private Integer winNum() {
        return checkWin();
    }

    /**
     * Checking if bot can loose, and return number to block player one
     * @return
     */
    private Integer dangerNum() {
        return checkDanger();
    }

    /**
     * Return danger number
     * @return
     */
    private Integer checkDanger() {
        // Danger list is being set to the combination arraylist which is a threat
        ArrayList<Integer> dangerList = new ArrayList<>();
        for (ArrayList<Integer> array: Brain.combinations) {
            // amountOfNumbers variable is counting how many threats it is in each array.
            int amountOfNumbersFound = 0;
            // lastSpotIsTaken is used to check if bot has the last number to the threat
            boolean lastSpotIsTaken = false;
            for (Integer num: array) {
                // Counting how many threats
                if (Brain.gameBoard.get(num - 1) == 1) {
                    amountOfNumbersFound += 1;
                }
                // Check if arraylist is a threat or not
                if (Brain.gameBoard.get(num - 1) == 2) {
                    lastSpotIsTaken = true;
                }
            }

            // If player one has two numbers and bot dont have the last, then danger list is set to array.
            // Loops breaks when the first danger is found.
            if (amountOfNumbersFound == 2 && !lastSpotIsTaken) {
                dangerList = array;
                break;
            }

        }

        Integer dangerNumber = null;
        // Looking for the number which player one needs to win if dangerList is not empty.
        // If empty list is empty, then it means that there is not danger, and loop gets ignored and null is returned
        if (!dangerList.isEmpty()) {
            for (Integer num: dangerList) {
                if (Brain.gameBoard.get(num - 1) == 0) {
                    // Found the number player one need to win
                    dangerNumber = num;
                }
            }
        }

        // Returning the number player one needs to win to block
        return dangerNumber;
    }

    /**
     * Return win number
     * @return
     */
    private Integer checkWin() {
        // combMatch is collecting the arrays which miss one number to get complete(to win)
        ArrayList<ArrayList<Integer>> combMatch = new ArrayList<>();
        // winNum is being set to the number which is missing in one array to win below
        Integer winNum = null;
        for (ArrayList<Integer> array: Brain.combinations) {
            int amountsOfNumbersFound = 0;
            // Checking if gameBoard has number 2 in the index spot of num - 1 to check if player two has the number.
            for (Integer num: array) {
                if (Brain.gameBoard.get(num - 1) == 2) {
                    // Amounts of numbers found is incrementing for each number found
                    amountsOfNumbersFound += 1;
                }
                    // If bot has two numbers then its being added to combMatch for deeper checking below.
                if (amountsOfNumbersFound == 2) {
                    combMatch.add(array);
                }
            }
        }

        if (!combMatch.isEmpty()) {
            for (ArrayList<Integer> winPossobile: combMatch) {
                for (Integer num: winPossobile) {
                    // Getting the last number needed to win if it exists.
                    if (Brain.gameBoard.get(num - 1) != 2 && Brain.gameBoard.get(num - 1) == 0) {
                        winNum = num;
                        break;
                    }
                }
            }
        }
        // Returning the number to win, its either a number or null
        return winNum;
    }

    /**
     * Returns the best available number
     * @return
     */
    private int possibleNum() {
        int returnNumber = -1;

        if (!this.cornerIsDefended) {
            // Check if there is a corner trap
            Integer needsDefendsNumber = checkCornerTrap();
            if (needsDefendsNumber != null) {
                return needsDefendsNumber;
            }
        }

        // If the different spots in gameBoard is taken and the one spot is untaken, return the untaken spot
        if (Brain.gameBoard.get(upperLeftCorner) == 1 && Brain.gameBoard.get(upperRightCorner) == 1 && Brain.gameBoard.get(upperLeftCorner -1) == 0) {
            return upperLeftCorner;
        }
        else if (Brain.gameBoard.get(upperLeftCorner -1) == 1 && Brain.gameBoard.get(middleOfTheBoard) == 1 && Brain.gameBoard.get(upperRightCorner - 1) == 0) {
            return upperRightCorner;
        }
        else if (Brain.gameBoard.get(upperRightCorner) == 1 && Brain.gameBoard.get(lowerLeftCorner) == 1 && Brain.gameBoard.get(lowerLeftCorner - 1) == 0) {
            return lowerLeftCorner;
        }
        else if (Brain.gameBoard.get(middleOfTheBoard) == 1 && Brain.gameBoard.get(lowerLeftCorner) == 1 && Brain.gameBoard.get(lowerLeftCorner + 1) == 0) {
            return lowerRightCorner;

            // Get corners if needed

        }
        else if (Brain.gameBoard.get(middleOfTheBoard - 1) == 0) {
            return middleOfTheBoard;
        }
        else if (Brain.gameBoard.get(upperLeftCorner - 1) == 0) {
            return upperLeftCorner;
        }
        else if (Brain.gameBoard.get(middleOfTheBoard + 1) == 0) {
            return lowerLeftCorner;
        }
        else if (Brain.gameBoard.get(upperLeftCorner + 1) == 0) {
            return upperRightCorner;
        }
        else if (Brain.gameBoard.get(lowerRightCorner - 1) == 0) {
            return lowerRightCorner;
        }
        else {
            // If this is even called, then it gets the first available untaken spot in gameBoard
            for (ArrayList<Integer> array: Brain.combinations) {
                for (Integer num: array) {
                    if (Brain.gameBoard.get(num - 1) == 0) {
                        returnNumber = num;
                        break;
                    }
                }
                if (returnNumber > 0) {
                    break;
                }
            }
        }


        // Returns the first available spot in gameBoard.
        return returnNumber;
    }

    /**
     * In tic tac toe its always possible to win if we not handle corners, this method make it 100% CLEARLY NOT WIN OVER BOT
     * @return
     *
        1 | 2 | 3
        ----------
        4 | 5 | 6
        ----------
        7 | 8 | 9

     * If player one choose 0 and 8 or 2 and 6, AI must know that AI need to block or player one win
     * AI take 1 , 3 , 5  or 7 if empty
     * If player choose 2 or 6 AI must choose 1,3,5,7 or player win
     */
    private Integer checkCornerTrap() {

        Integer cornerDefense = null;

        if (Brain.gameBoard.get(upperLeftCorner - 1) == 1 && Brain.gameBoard.get(lowerRightCorner - 1) == 1 ||
                Brain.gameBoard.get(lowerLeftCorner - 1) == 1 && Brain.gameBoard.get(upperRightCorner - 1) == 1) {

            if (Brain.gameBoard.get(upperRightCorner) == 0) {
                cornerDefense = middleOfTheBoard - 1;
                this.cornerIsDefended = true;
            }
            else if (Brain.gameBoard.get(middleOfTheBoard) == 0) {
                cornerDefense = lowerLeftCorner - 1;
                this.cornerIsDefended = true;
            }
            else if (Brain.gameBoard.get(2) == 0) {
                cornerDefense = upperRightCorner;
                this.cornerIsDefended = true;
            }
            else if (Brain.gameBoard.get(lowerLeftCorner) == 0) {
                cornerDefense = lowerRightCorner - 1;
                this.cornerIsDefended = true;
            }
        }
        return cornerDefense;
    }

}
