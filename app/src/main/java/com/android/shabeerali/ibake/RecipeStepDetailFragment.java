package com.android.shabeerali.ibake;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class RecipeStepDetailFragment extends Fragment {

    private static final String TAG = "iBake - RecipeStepDetailFragment";

    public static final String STEP_DESC= "step_desc";
    public static final String STEP_POS= "step_pos";

    private String mStepDescription;
    private int    mPosition;
    private int    mTotalSteps;


    TextView  mTvStepDescription;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public RecipeStepDetailFragment() {
    }

    public void setDescription(int position, String description, int totalSteps) {

        mStepDescription = description;
        mPosition = position;
        mTotalSteps = totalSteps;
    }
    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Load the saved state (the list of images and list index) if there is one
        if(savedInstanceState != null) {
            mStepDescription = savedInstanceState.getString(STEP_DESC);
            mPosition = savedInstanceState.getInt(STEP_POS);
        }

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.recipe_step_desc_frag, container, false);

        mTvStepDescription = (TextView) rootView.findViewById(R.id.tv_step_description) ;

        mTvStepDescription.setText(mStepDescription);

        // Return the rootView
        return rootView;
    }


    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putString(STEP_DESC, mStepDescription);
        currentState.putInt(STEP_POS, mPosition);
    }


}


