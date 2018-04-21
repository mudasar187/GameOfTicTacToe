package no.woact.ahmmud16.Fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import no.woact.ahmmud16.FragmentLoading.FragmentLoading;
import no.woact.ahmmud16.R;

/**
 * This fragment contains where you choose if you want to play against computer or against another player
 */
public class ChooseGameModusFragment extends FragmentLoading implements View.OnClickListener {

    private TextView chooseGameHeaderTextView, quotesTextView;
    private Button singelPlayerBtn, multiPlayerBtn;
    private ConstraintLayout chooseGameModusFragmentLayout;
    private SingelPlayerFragment singelPlayerFragment;
    private MultiPlayerFragment multiPlayerFragment;

    public ChooseGameModusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_game_modus, container, false);

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
        singelPlayerFragment = new SingelPlayerFragment();
        multiPlayerFragment = new MultiPlayerFragment();
        chooseGameHeaderTextView = view.findViewById(R.id.chooseGameHeaderTextView);
        chooseGameModusFragmentLayout = view.findViewById(R.id.chooseFragmentLayout);
        quotesTextView = view.findViewById(R.id.quotesTextView);
        singelPlayerBtn = view.findViewById(R.id.singelPlayerBtn);
        multiPlayerBtn = view.findViewById(R.id.multiPlayerBtn);
    }

    /**
     * Initialize animation
     */
    private void initializeAnimation() {
        loadFragmentOneAnimation(getActivity(), R.anim.frominvisibletovisible, chooseGameModusFragmentLayout);
    }

    /**
     * Set click listeners
     */
    private void setClickListeners() {
        singelPlayerBtn.setOnClickListener(this);
        multiPlayerBtn.setOnClickListener(this);
    }

    /**
     * Switch method for which button have been pressed
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.singelPlayerBtn:
                loadFragmentWithBackStack(getActivity(), R.id.fragmentContainer, singelPlayerFragment);
                break;
            case R.id.multiPlayerBtn:
                loadFragmentWithBackStack(getActivity(),R.id.fragmentContainer, multiPlayerFragment);
                break;
        }
    }
}
