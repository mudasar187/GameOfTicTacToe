package no.woact.ahmmud16.Fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import no.woact.ahmmud16.Database.Handler.DatabaseHandler;
import no.woact.ahmmud16.Database.Model.Player;
import no.woact.ahmmud16.FragmentLoading.FragmentLoading;
import no.woact.ahmmud16.Adapter.HighScoreAdapter;
import no.woact.ahmmud16.R;


/**
 * This fragment contains highscore for all players that have been played on this game
 */
public class HighscoreFragment extends FragmentLoading {

    private TextView highscoreHeaderTextView, playerNameTextView, winTextView, drawTextView, lossTextView;
    private ConstraintLayout highScoreFragmentLayout;
    private List<Player> playerList;
    private RecyclerView recyclerView;
    private HighScoreAdapter mAdapter;
    private DatabaseHandler databaseHandler;

    public HighscoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_highscore, container, false);

        // Initialize resources and animations
        initializeResources(view);
        initializeAnimation();
        initializeListAndViewAndAdapter(view);
        return view;
    }

    /**
     * Initialize resources
     * @param view
     */
    private void initializeResources(View view) {
        playerNameTextView = view.findViewById(R.id.playerNameTextView);
        winTextView = view.findViewById(R.id.rowLossTextView);
        drawTextView = view.findViewById(R.id.rowWinTextView);
        lossTextView = view.findViewById(R.id.rowWinTextView);
        highscoreHeaderTextView = view.findViewById(R.id.chooseGameHeaderTextView);
        highScoreFragmentLayout = view.findViewById(R.id.highscoreFragmentLayout);
        databaseHandler = new DatabaseHandler(getActivity());
    }

    /**
     * Initialize view and adapter
     * @param view
     */
    private void initializeListAndViewAndAdapter(View view) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recycler_view);

        playerList = new ArrayList<>();
        playerList = databaseHandler.getPlayerList();
        mAdapter = new HighScoreAdapter(playerList);

        mAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * Initialize animations
     */
    private void initializeAnimation() {
        loadFragmentOneAnimation(getActivity(), R.anim.frominvisibletovisible, highScoreFragmentLayout);
    }
}
