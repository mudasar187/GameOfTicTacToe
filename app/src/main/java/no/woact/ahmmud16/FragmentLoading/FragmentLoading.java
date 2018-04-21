package no.woact.ahmmud16.FragmentLoading;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

/**
 * This class contains method for loading fragment and animations in each fragment class
 * Made this class so I do not repeat much code in each fragment class
 * For example, if I want to enter method to load a fragment with three animations,
 * i can create the method here and create an object of this class so I can only call the correct method with right values in the parameter
 * This class is abstract because i dont want to create an object of this class
 * Im extending Fragment because a Java class only extends one class at time, so my FragmentLoading class extend Fragment
 * and do all the job for loading fragment and loading fragment with animation inside here
 */
public abstract class FragmentLoading extends Fragment {

    private FragmentActivity myContext;
    private Animation animationOne;
    private Animation animationTwo;

    public FragmentLoading() {
        // Empty constructor
    }

    /**
     * Load fragment thats take 3 args, activity, container and fragment with addBackToStack() method
     * This method can call on the fragments where i want to say if i press onBackPressed() i go back to the last fragment i was in
     *
     * @param activity  the activity
     * @param container the container
     * @param fragment  the fragment
     */
    public void loadFragmentWithBackStack(Activity activity, int container, Fragment fragment) {

        myContext = (FragmentActivity) activity; // Need to cast, otherwise you will get 'null object reference'

        myContext.getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment, null)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Load fragment thats take 3 args, activity, container and fragment without addBackToStack() method
     * This method can be call on the fragments where i dont want to go back to the last fragment
     *
     * @param activity  the activity
     * @param container the container
     * @param fragment  the fragment
     */
    public void loadFragmentWithOutBackStack(Activity activity, int container, Fragment fragment) {

        myContext = (FragmentActivity) activity; // Need to cast, otherwise you will get 'null object reference'

        myContext.getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment, null)
                .commit();
    }

    /**
     * This method switch between fragments
     * Lets suppose you have fragment A -> B -> C -> D, when your on frament D, you want to go back to fragment B
     * And when you click back press button on your phone it will go from B -> A and click back again and application will exit
     *
     * @param activity                     the activity
     * @param numberOfFragmentYouWantToPop the number of fragment you want to pop
     */
    public void switchBetweenFragment(Activity activity, int numberOfFragmentYouWantToPop) {

        myContext = (FragmentActivity) activity;

        myContext.getSupportFragmentManager()
                .popBackStack(myContext.getSupportFragmentManager()
                        .getBackStackEntryAt(myContext.getSupportFragmentManager()
                                .getBackStackEntryCount()-numberOfFragmentYouWantToPop).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Reload the fragment
     * This method is called in GameBoardFragment where i use this method when user want to restart match
     *
     * @param activity the activity
     */
    public void reloadFragment(Activity activity) {

        myContext = (FragmentActivity) activity;

        myContext.getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }

    /**
     * Set animation on one constraint layout, which means load this fragment with one animation file
     * When user go from fragment A to fragment B, its load the fragment with this type of animation
     *
     * @param activity         the activity
     * @param layout           the layout
     * @param constraintLayout the constraint layout
     */
    public void loadFragmentOneAnimation(Activity activity, int layout, ConstraintLayout constraintLayout) {
        animationOne = AnimationUtils.loadAnimation(activity, layout);
        constraintLayout.setAnimation(animationOne);
    }

    /**
     * Set animation on two linear layouts, which means load this fragment with two animation file
     * When user start the application, user will see the text coming from above and the crown coming from bottom
     *
     * @param activity        the activity
     * @param layoutOne       the layout one
     * @param layoutTwo       the layout two
     * @param linearLayoutOne the linear layout one
     * @param linearLayoutTwo the linear layout two
     */
    public void loadFragmentTwoAnimation(Activity activity, int layoutOne, int layoutTwo, LinearLayout linearLayoutOne, LinearLayout linearLayoutTwo) {
        animationOne = AnimationUtils.loadAnimation(activity, layoutOne);
        animationTwo = AnimationUtils.loadAnimation(activity, layoutTwo);
        linearLayoutOne.setAnimation(animationOne);
        linearLayoutTwo.setAnimation(animationTwo);
    }

}
