package no.woact.ahmmud16.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import no.woact.ahmmud16.Database.Handler.DatabaseHandler;
import no.woact.ahmmud16.FragmentLoading.FragmentLoading;
import no.woact.ahmmud16.R;
import no.woact.ahmmud16.Validation.PlayerNameValidation;

/**
 * This class is the fragment where player one and player two can play against each other
 */
public class MultiPlayerFragment extends FragmentLoading implements View.OnClickListener {

    private TextView quotesTextView, enterPlayerOneTextView, enterPlayerTwoTextView, playerExistsTextView, playerExistsLongTextView;
    private EditText enterPlayerOneEditText, enterPlayerTwoEditText;
    private Button startGameBtn, playerExistsYesBtn, playerExistsNoBtn;
    private ConstraintLayout playerVsPlayerFragmentLayout;
    private GameBoardFragment gameBoardFragment;
    private PlayerNameValidation playerNameValidation;
    private Bundle bundle;
    private DatabaseHandler databaseHandler;
    private Dialog dialog;
    private String playerNameOne, playerNameTwo;

    public MultiPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_multi_player, container, false);

        // Initialize resources and animations
        initializeResources(view);
        initializeAnimation();
        setClickListeners();
        return view;
    }

    /**
     * Initialize resources
     * @param view
     */
    private void initializeResources(View view) {
        gameBoardFragment = new GameBoardFragment();
        playerNameValidation = new PlayerNameValidation();
        bundle = new Bundle();
        databaseHandler = new DatabaseHandler(getActivity());
        dialog = new Dialog(getActivity());
        quotesTextView = view.findViewById(R.id.warsTeachPeopleTextView);
        enterPlayerOneTextView = view.findViewById(R.id.enterPlayerOneTextView);
        enterPlayerTwoTextView = view.findViewById(R.id.enterPlayerTwoTextView);
        enterPlayerOneEditText = view.findViewById(R.id.enterPlayerOneEditText);
        enterPlayerTwoEditText = view.findViewById(R.id.enterPlayerTwoEditText);
        startGameBtn = view.findViewById(R.id.startGameBtn);
        playerVsPlayerFragmentLayout = view.findViewById(R.id.playerVsPlayerFragmentLayout);
    }

    /**
     * Initialize animations
     */
    private void initializeAnimation() {
        loadFragmentOneAnimation(getActivity(), R.anim.frominvisibletovisible, playerVsPlayerFragmentLayout);
    }

    /**
     * Set click listeners
     */
    private void setClickListeners() {
        startGameBtn.setOnClickListener(this);
    }


    /**
     * This method validate both playerNames, if validating is good then create a bundle object ready to send to GameBoardFragment
     * And then checks if these player exists in database,
     * if both exists or one of them show dialog box who exists and ask if user want to use the name
     * @param checkPlayerNameOne
     * @param checkPlayerNameTwo
     */
    private void validatePlayerAndStartGame(String checkPlayerNameOne, String checkPlayerNameTwo) {
        // Check validation
        if(playerNameValidation.validateTwoPlayer(getActivity(), checkPlayerNameOne, checkPlayerNameTwo)) {
            // If validation is good
            playerNameOne = checkPlayerNameOne;
            playerNameTwo = checkPlayerNameTwo;

            // Create bundle object
            createBundleObject(playerNameOne, playerNameTwo);

            // Check if they exists in database
            boolean playerOneExists = databaseHandler.checkIfUserExists(playerNameOne);
            boolean playerTwoExists = databaseHandler.checkIfUserExists(playerNameTwo);

            // Check what to show in dialog based on who exists
            if(playerOneExists && playerTwoExists) {
                myCustomPlayerNameExists(playerNameOne, playerNameTwo, null);
            } else {
                if (playerOneExists && !playerTwoExists) {
                    // If player two not exists, add to database and ask if player one want to use exists name
                    databaseHandler.addNewUser(playerNameTwo);
                    myCustomPlayerNameExists(playerNameOne, playerNameTwo, true);
                } else if (!playerOneExists && playerTwoExists) {
                    // If player one not exists, add to database and ask if player two want to use exists name
                    databaseHandler.addNewUser(playerNameOne);
                    myCustomPlayerNameExists(playerNameOne, playerNameTwo, false);
                } else {
                    // If no one exists, add both to database and start game
                    databaseHandler.addNewUser(playerNameOne);
                    databaseHandler.addNewUser(playerNameTwo);
                    clearNames(null);
                    loadFragmentWithBackStack(getActivity(), R.id.fragmentContainer, gameBoardFragment);
                }
            }
        }
    }

    /**
     * Create Bundle object ready to send to GameBoardFragment
     * @param playerOne
     * @param playerTwo
     */
    private void createBundleObject(String playerOne, String playerTwo) {
        bundle.putString("playerOne", playerOne);
        bundle.putString("playerTwo", playerTwo);
        gameBoardFragment.setArguments(bundle);
    }

    /**
     * Switch method for which button have been pressed
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startGameBtn:
                validatePlayerAndStartGame(enterPlayerOneEditText.getText().toString().toUpperCase(), enterPlayerTwoEditText.getText().toString().toUpperCase());
                break;
        }
    }

    /**
     * If a user exists in database and user want to use name clear names and start game
     * If user dont want to use exists name in database, clear the one who dont want to use name so user can enter new name
     * @param bothExists
     */
    private void clearNames(Boolean bothExists) {
        if (bothExists == null) {
            playerNameOne = "";
            playerNameTwo = "";
        } else if (bothExists) {
            playerNameOne = "";
        } else {
            playerNameTwo = "";
        }
        enterPlayerOneEditText.setText(playerNameOne);
        enterPlayerTwoEditText.setText(playerNameTwo);
    }

    /**
     * My custom dialog for showing which player name exists and ask user if he/she want to use that name
     * Call the method above clearNames()
     * @param playerNameOne
     * @param playerNameTwo
     * @param bothPlayersExists
     */
    private void myCustomPlayerNameExists(final String playerNameOne, final String playerNameTwo, final Boolean bothPlayersExists) {

        // Now allowed to click outside dialog to dismiss the dialog
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.player_exists_dialog);

        playerExistsTextView = dialog.findViewById(R.id.playerExistsTextView);
        playerExistsLongTextView = dialog.findViewById(R.id.playerExistsLongTextView);
        playerExistsYesBtn = dialog.findViewById(R.id.playerExistsYesBtn);
        playerExistsNoBtn = dialog.findViewById(R.id.playerExistsNoBtn);

        playerExistsYesBtn.setEnabled(true);
        playerExistsNoBtn.setEnabled(true);

        //  Set playerExistsTextView based on who exists
        if (bothPlayersExists == null) {
            playerExistsTextView.setText(R.string.both_exists);
        } else if (bothPlayersExists) {
            playerExistsTextView.setText(playerNameOne + " exists !");
        } else if (!bothPlayersExists) {
            playerExistsTextView.setText(playerNameTwo + " exists !");
        }

        // If yes, use the player name
        playerExistsYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBundleObject(playerNameOne, playerNameTwo);
                clearNames(bothPlayersExists);
                loadFragmentWithBackStack(getActivity(), R.id.fragmentContainer, gameBoardFragment);
                dialog.dismiss();
            }
        });

        // If no, enter new player name
        playerExistsNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearNames(bothPlayersExists);
                dialog.cancel();
            }
        });
        // Stop timer when dialog is displayed
        dialog.show();
    }
}
