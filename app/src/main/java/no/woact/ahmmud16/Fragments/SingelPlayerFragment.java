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
 * This class contains where player can play against TTTBot
 */
public class SingelPlayerFragment extends FragmentLoading implements View.OnClickListener {

    private TextView quotesTextView, enterPlayerOneTextView, playerExistsTextView, playerExistsLongTextView;
    private EditText enterPlayerOneEditText;
    private Button startGameBtn, playerExistsYesBtn, playerExistsNoBtn;
    private ConstraintLayout playerVsComputerFragmentLayout;
    private MultiPlayerFragment multiPlayerFragment;
    private SingelPlayerFragment singelPlayerFragment;
    private GameBoardFragment gameBoardFragment;
    private Bundle bundle;
    private PlayerNameValidation playerNameValidation;
    private DatabaseHandler databaseHandler;
    private Dialog dialog;
    private String playerNameOne, playerNameTwo;

    public SingelPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_singel_player, container, false);

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
        singelPlayerFragment = new SingelPlayerFragment();
        multiPlayerFragment = new MultiPlayerFragment();
        bundle = new Bundle();
        playerNameValidation = new PlayerNameValidation();
        databaseHandler = new DatabaseHandler(getContext());
        dialog = new Dialog(getActivity());
        quotesTextView = view.findViewById(R.id.whenYouPlayTextView);
        enterPlayerOneTextView = view.findViewById(R.id.enterPlayerOneTextView);
        enterPlayerOneEditText = view.findViewById(R.id.enterPlayerOneEditText);
        startGameBtn = view.findViewById(R.id.startGameBtn);
        playerVsComputerFragmentLayout = view.findViewById(R.id.playerVsComputerFragmentLayout);
    }

    /**
     * Initialize animations
     */
    private void initializeAnimation() {
        loadFragmentOneAnimation(getActivity(), R.anim.frominvisibletovisible, playerVsComputerFragmentLayout);
    }

    /**
     * Set click listeners
     */
    private void setClickListeners() {
        startGameBtn.setOnClickListener(this);
    }

    /**
     * Send playerOne and TTTBot name to GameBoardFragment if validating on playerOne is good
     * Not checking TTTBot because already added once with use of SharedPreferences in MainActivity
     * @param checkPlayerName
     */
    private void validatePlayerAndStartGame(String checkPlayerName) {
        // Check validation
        if(playerNameValidation.validateOnePlayer(getActivity(), checkPlayerName)) {

            // If validation is good
            playerNameOne = checkPlayerName;
            playerNameTwo = "TTTBOT"; // Default init

            // Only checks playerNameOne in database, TTTBOT already added
            if(databaseHandler.checkIfUserExists(playerNameOne)) {
                // If exists
                myCustomPlayerNameExists(playerNameOne, playerNameTwo);
            } else {
                // If not, create bundle object, send to GameBoardFragment
                createBundleObject(playerNameOne, playerNameTwo);

                // Also add playerNameOne to database
                databaseHandler.addNewUser(playerNameOne);

                // Clear names
                clearNames();

                // Load GameBoardFragment
                loadFragmentWithBackStack(getActivity(), R.id.fragmentContainer, gameBoardFragment);
            }
        }
    }

    /**
     * Create Bundle object ready to send to GameBoardFragment
     * @param playerOne
     * @param tttbot
     */
    private void createBundleObject(String playerOne, String tttbot) {
        bundle.putString("playerOne", playerOne);
        bundle.putString("playerTwo", tttbot);
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
                // Validation check
                validatePlayerAndStartGame(enterPlayerOneEditText.getText().toString().toUpperCase());
                break;
        }
    }

    /**
     * Clear names variable just to ensure
     */
    private void clearNames() {
        playerNameOne = "";
        playerNameTwo = "";
    }

    /**
     * My custom dialog to show that player name exists and ask if he/she want to use that name
     * @param playerNameOne
     * @param playerNameTwo
     */
    private void myCustomPlayerNameExists(final String playerNameOne, final String playerNameTwo) {

        // Now allowed to click outside dialog to dismiss the dialog
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.player_exists_dialog);

        playerExistsTextView = dialog.findViewById(R.id.playerExistsTextView);
        playerExistsLongTextView = dialog.findViewById(R.id.playerExistsLongTextView);
        playerExistsYesBtn = dialog.findViewById(R.id.playerExistsYesBtn);
        playerExistsNoBtn = dialog.findViewById(R.id.playerExistsNoBtn);

        playerExistsYesBtn.setEnabled(true);
        playerExistsNoBtn.setEnabled(true);

        playerExistsTextView.setText(playerNameOne + " Exists !");

        // If yes, use the player name
        playerExistsYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBundleObject(playerNameOne, playerNameTwo);
                clearNames();
                loadFragmentWithBackStack(getActivity(), R.id.fragmentContainer, gameBoardFragment);
                dialog.dismiss();
            }
        });

        // If no, enter new playername
        playerExistsNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterPlayerOneEditText.setText("");
                clearNames();
                dialog.cancel();
            }
        });
        // Stop timer when dialog is displayed
        dialog.show();
    }
}
