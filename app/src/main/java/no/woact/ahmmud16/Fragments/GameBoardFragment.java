package no.woact.ahmmud16.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import no.woact.ahmmud16.Database.Constants.Constants;
import no.woact.ahmmud16.Database.Handler.DatabaseHandler;
import no.woact.ahmmud16.FragmentLoading.FragmentLoading;
import no.woact.ahmmud16.GameLogic.Brain;
import no.woact.ahmmud16.R;
import no.woact.ahmmud16.TimeTicker.TimerTicker;


/**
 * This fragment contains the gamebord, no matter if you want to play against TTTBot og another player, going to load the same fragment
 */
public class GameBoardFragment extends FragmentLoading implements View.OnClickListener {

    private ConstraintLayout gameBoardFragmentLayout;
    private ImageView starkImageView, lannisterImageView;
    private TextView vsTextView, starkPlayerTextView, lannisterPlayerTextView, timePlayTextView, playerWonView, whoStartFirstTextView, whoStartPlayerOneTextView, whoStartPlayerTwoTextView;
    private TableLayout tableLayout;
    private TableRow rowOne, rowTwo, rowThree;
    private ImageButton btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, whoStartPlayerOneImageBtn, whoStartPlayerTwoImageBtn;
    private Button endGame, yesBtn, noBtn, restartMatchBtn, changeGameModusBtn;
    private Bundle bundle;
    private Dialog dialog;
    private MainMenuFragment mainMenuFragment;
    private DatabaseHandler databaseHandler;
    private Brain brain;
    private String playerNameOne, playerNameTwo;
    private ArrayList<ImageButton> imageButtonsForAI;
    private TimerTicker timerTicker;
    private boolean botIsActivated = false;
    public boolean playerOne = true;

    public GameBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_board, container, false);

        // Initialize resources and animations
        initializeResources(view);
        initializeAnimation();
        changeBackground(view);
        setClickListeners();
        getPlayerNames();

        // Show dialog for who is starting the game
        myCustomWhoStartFirstDialog(this);

        return view;
    }

    /**
     * Pause timer when application call OnPause(), this is to avoid timer to tick while application is not in use
     */
    @Override
    public void onPause() {
        super.onPause();
        timerTicker.pauseTimer();
    }

    /**
     * Pause timer when application call OnPause(), this is to avoid timer to tick while application is not in use
     */
    @Override
    public void onStop() {
        super.onStop();
        timerTicker.pauseTimer();
    }

    /**
     * Resume timer when user come in to application again, if remove (whenTimerStopped = 0)
     * it will start before choosing who want to start when GameBoardFragment load
     */
    @Override
    public void onResume() {
        super.onResume();
        timerTicker.resumeTimer();
    }

    /**
     * Initialize animation
     */
    private void initializeAnimation() {
        loadFragmentOneAnimation(getActivity(), R.anim.frominvisibletovisible, gameBoardFragmentLayout);
    }

    /**
     * Initialize resources
     * Also insert imageButtons into an array for AI
     * @param view
     */
    private void initializeResources(View view) {
        bundle = new Bundle();
        mainMenuFragment = new MainMenuFragment();
        dialog = new Dialog(getActivity());
        brain = new Brain();
        databaseHandler = new DatabaseHandler(getActivity());
        timerTicker = new TimerTicker(view);
        tableLayout = view.findViewById(R.id.gameBoard);
        rowOne = view.findViewById(R.id.rowOne);
        rowTwo = view.findViewById(R.id.rowTwo);
        rowThree = view.findViewById(R.id.rowThree);
        btnOne = view.findViewById(R.id.btnOne);
        btnTwo = view.findViewById(R.id.btnTwo);
        btnThree = view.findViewById(R.id.btnThree);
        btnFour = view.findViewById(R.id.btnFour);
        btnFive = view.findViewById(R.id.btnFive);
        btnSix = view.findViewById(R.id.btnSix);
        btnSeven = view.findViewById(R.id.btnSeven);
        btnEight = view.findViewById(R.id.btnEight);
        btnNine = view.findViewById(R.id.btnNine);
        endGame = view.findViewById(R.id.endGameBtn);
        starkImageView = view.findViewById(R.id.starkImageView);
        lannisterImageView = view.findViewById(R.id.lannisterImageView);
        vsTextView = view.findViewById(R.id.vsTextView);
        timePlayTextView = view.findViewById(R.id.timePlayTextView);
        starkPlayerTextView = view.findViewById(R.id.starkPlayerTextView);
        lannisterPlayerTextView = view.findViewById(R.id.lannisterPlayerTextView);
        gameBoardFragmentLayout = view.findViewById(R.id.gameBoardFragmentLayout);

        // For AI
        imageButtonsForAI = new ArrayList<>(Arrays.asList(btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine));
    }

    /**
     * Change background when GameBoardFragment is loaded in
     * @param view
     */
    private void changeBackground(View view) {
        view.setBackgroundResource(R.drawable.map);
    }

    /**
     * Get the player names from SingelPlayerFragment or MultiPlayerFragment by bundle object
     * and set their name in textview on each side of VS
     */
    private void getPlayerNames() {
        // Check if bundle is not null
        if (bundle != null) {
            bundle = getArguments();
            playerNameOne = bundle.getString("playerOne");
            playerNameTwo = bundle.getString("playerTwo");

            // Set to textview in "VS" box
            starkPlayerTextView.setText(playerNameOne);
            lannisterPlayerTextView.setText(playerNameTwo);
        } else {
            Log.d("BUNDLE OBJECT:", "IS EMPTY!");
        }
    }

    /**
     * Set click listeners
     */
    private void setClickListeners() {
        endGame.setOnClickListener(this);
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNine.setOnClickListener(this);
    }

    /**
     * Switch method for which button have been pressed
     * Try/catch because of two possible different button types is registered here
     * Check which players turn
     * @param v
     */
    @Override
    public void onClick(View v) {

        ImageButton buttonTaps = null;
        try {
            buttonTaps = (ImageButton) v;
        } catch (Exception e) {
            e.getMessage();
        }

        // Which image to set
        int image;

        // Check which players turn
        if(playerOne) {
            image = R.drawable.shieldplayer;
        } else {
            // If not player 1's turn, then player two's turn/bot
            image = R.drawable.swordplayer;
        }

        // To register which button was tapped to change index spot in gameboard to either 1 or 2 (player 1 or player2).
        int buttonTappedNumber = 0;

        switch (v.getId()) {
            case R.id.endGameBtn:
                showExitGameDialog();
                break;
            case R.id.btnOne:
                buttonTaps.setImageResource(image);
                buttonTappedNumber = 0;
                break;
            case R.id.btnTwo:
                buttonTaps.setImageResource(image);
                buttonTappedNumber = 1;
                break;
            case R.id.btnThree:
                buttonTaps.setImageResource(image);
                buttonTappedNumber = 2;
                break;
            case R.id.btnFour:
                buttonTaps.setImageResource(image);
                buttonTappedNumber = 3;
                break;
            case R.id.btnFive:
                buttonTaps.setImageResource(image);
                buttonTappedNumber = 4;
                break;
            case R.id.btnSix:
                buttonTaps.setImageResource(image);
                buttonTappedNumber = 5;
                break;
            case R.id.btnSeven:
                buttonTaps.setImageResource(image);
                buttonTappedNumber = 6;
                break;
            case R.id.btnEight:
                buttonTaps.setImageResource(image);
                buttonTappedNumber = 7;
                break;
            case R.id.btnNine:
                buttonTaps.setImageResource(image);
                buttonTappedNumber = 8;
                break;
            default:
                break;
        }

        // Make sure image button was tapped by YOU.
        if (buttonTaps != null) {
            imageButtonsForAI.get(buttonTappedNumber).setEnabled(false);

            // Telling brain to switch value of button tapped index to 1 from 0 in gameboard arraylist
            // Telling brain that bot is activated or not, to let brain know if bot should make a move
            // Bot is activated if player 2 username is TTTBot.
            brain.getBtnTapped(buttonTappedNumber, this, botIsActivated);
        }
    }

    /**
     * AI use this method to configure imageButton
     * Choosen number is the number AI choose
     * Get choosen number from controller which return moveNumber to brain in chooseMove() method which from goBot() method
     *
     * @param chosenNumber the chosen number
     */
    public void setImageForAI(int chosenNumber) {
        imageButtonsForAI.get(chosenNumber).setEnabled(false);
        imageButtonsForAI.get(chosenNumber).setImageResource(R.drawable.swordplayer);
    }

    /**
     * This method check if the dialog is not showing, then show it
     * Reason for that is, when user double click on the "END GAME" button quick, it will load 2 dialog
     * This method will avoid that 2 dialog shows up
     */
    private void showExitGameDialog() {
        if (!dialog.isShowing()) {
            myCustomExitGameDialog();
        }
    }

    /**
     * Same reason as above, but sending who has won in parameter, to set which name it going to show in dialog for who won
     *
     * @param whoWon the who won
     */
    public void showWhenPlayerWinDialog(Boolean whoWon) {
        if(!dialog.isShowing()) {
            myCustomWinLooseDrawDialog(whoWon);
        }
    }

    /**
     * Exit the game, stop timer and call method for switch between fragment
     * Also pop the fragment between the fragment user going to switch
     */
    private void exitGame() {
        timerTicker.stopTimer(); // Stop timer when user exit the game
        dialog.dismiss(); // Dismiss the dialog
        switchBetweenFragment(getActivity(), 3); // Pop 3 fragments from the stack and go back to MainMenuFragment
    }

    /**
     * My custom dialog, ask user if user want to exit the game or not
     * If user click yes, exit game and stop timer
     * If user click no, resume game and resume timer from where it paused when dialog showed up
     */
    private void myCustomExitGameDialog() {

        // Now allowed to click outside dialog to dismiss the dialog
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.exit_game_dialog);

        yesBtn = dialog.findViewById(R.id.yesBtn);
        noBtn = dialog.findViewById(R.id.noBtn);

        yesBtn.setEnabled(true);
        noBtn.setEnabled(true);

        // If yes
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               exitGame();
               timerTicker.stopTimer();
            }
        });

        // If no
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerTicker.resumeTimer(); // resume timer if user click no
                dialog.cancel();
            }
        });
        // Stop timer when dialog is displayed
        timerTicker.pauseTimer();
        dialog.show();
    }

    /**
     * My custom dialog for showing who win, or if its draw and give user some options like restart match, change game modus or exit game
     * Sets the name of the winner in dialog for who won and give user options
     * Also updating the player stats
     */
    private void myCustomWinLooseDrawDialog(Boolean playerOne) {

        // Now allowed to click outside dialog to dismiss the dialog
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.win_loose_draw_dialog);

        restartMatchBtn = dialog.findViewById(R.id.restartMatchBtn);
        changeGameModusBtn = dialog.findViewById(R.id.changeGameModusBtn);
        endGame = dialog.findViewById(R.id.exitGameBtn);
        playerWonView = dialog.findViewById(R.id.playerWon);

        // Set playerWonView to which player has won
        if (playerOne != null) {
            if (playerOne) {
                playerWonView.setText(this.playerNameOne + " Wins!");
                // Update playerNameOne with 1 win and playerNameTwo with 1 loss
                databaseHandler.updateStats(playerNameOne, Constants.KEY_WIN);
                databaseHandler.updateStats(playerNameTwo, Constants.KEY_LOSS);
            } else {
                playerWonView.setText(this.playerNameTwo + " Wins!");
                // Update playerNameTwo with 1 win and playerNameOne with 1 loss
                databaseHandler.updateStats(playerNameTwo, Constants.KEY_WIN);
                databaseHandler.updateStats(playerNameOne, Constants.KEY_LOSS);
            }
        } else {
            playerWonView.setText(R.string.its_draw);
            // Update draw on both players
            databaseHandler.updateStats(playerNameOne, Constants.KEY_DRAW);
            databaseHandler.updateStats(playerNameTwo, Constants.KEY_DRAW);
        }

        restartMatchBtn.setEnabled(true);
        changeGameModusBtn.setEnabled(true);
        endGame.setEnabled(true);

        // If restart match
        restartMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                reloadFragment(getActivity()); // Reload fragment
            }
        });

        // If change game modus
        changeGameModusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                switchBetweenFragment(getActivity(), 2); // Pop two fragments from stack and go back to ChooseGameModusFragment
            }
        });

        // If exit game
        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitGame();
            }
        });
        // Stop timer when dialog is displayed
        timerTicker.stopTimer();
        dialog.show();
    }


    /**
     * My custom dialog for who start the first tap
     * @param gameBoardFragment
     */
    private void myCustomWhoStartFirstDialog(final GameBoardFragment gameBoardFragment) {

        // Now allowed to click outside dialog to dismiss the dialog
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.who_start_first_dialog);

        whoStartFirstTextView = dialog.findViewById(R.id.whoStartFirstTextView);
        whoStartPlayerOneTextView = dialog.findViewById(R.id.whoStartPlayerOneTextView);
        whoStartPlayerTwoTextView = dialog.findViewById(R.id.whoStartPlayerTwoTextView);
        whoStartPlayerOneImageBtn = dialog.findViewById(R.id.whoStartPlayerOneImageBtn);
        whoStartPlayerTwoImageBtn = dialog.findViewById(R.id.whoStartPlayerTwoImageBtn);

        whoStartPlayerOneTextView.setText(playerNameOne);
        whoStartPlayerTwoTextView.setText(playerNameTwo);
        botIsActivated = playerNameTwo.equals("TTTBOT"); // If playerNameTwo is equals to "TTTBOT" then activate AI

        whoStartPlayerOneImageBtn.setEnabled(true);
        whoStartPlayerTwoImageBtn.setEnabled(true);

        // Player one begin and start timer
        whoStartPlayerOneImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whoStartGame(true); // If player one, set to true
            }
        });

        // Player two begin and start timer
        whoStartPlayerTwoImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whoStartGame(false); // If player two, set player one to false and player two start
                brain.goBot(botIsActivated, gameBoardFragment); // Bot is activated if playerNameTwo is equal to TTTBOT
            }
        });
        dialog.show();
    }

    /**
     * Method for declaring for if playerOne start or not
     * If playerOne start set true, if not and the other player start set false
     * Start timer and then dismiss dialog
     * @param isStarting
     */
    private void whoStartGame(boolean isStarting) {
        playerOne = isStarting;
        timerTicker.startTimer();
        dialog.dismiss();
    }
}