package com.android.shabeerali.ibake;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.shabeerali.ibake.data.RecipeObject;

import java.util.ArrayList;

import static android.R.attr.fragment;

public class RecipeStepsDetails extends AppCompatActivity  implements View.OnClickListener {

    private static final String TAG = "iBake - RecipeStepsDetails";

    FrameLayout mVideoLayout;
    FrameLayout mDescLayout;
    RecipeObject mRecipeObject;
    int mPosition;
    Button mNextButton;
    Button mPrevButton;

    RecipeStepDetailFragment mStepFragment;
    RecipeVideoFragment mVideoFragment;
    FragmentManager mFragmentManager;
    RelativeLayout mNavBtnLayouts;

    public static final String STEP_POSITION = "step_position";
    public static final String RECIPE_OBJECT = "recipe_object";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_details);

        mVideoLayout = (FrameLayout) findViewById(R.id.video_container);
        mDescLayout = (FrameLayout) findViewById(R.id.description_container);
        mNextButton = (Button) findViewById(R.id.btn_next);
        mPrevButton = (Button) findViewById(R.id.btn_prev);
        mNextButton.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);
        mNavBtnLayouts = (RelativeLayout) findViewById(R.id.recipe_step_nav_btns);

        if (savedInstanceState == null) {
            Intent intentThatStartedThisActivity = getIntent();

            if (intentThatStartedThisActivity != null) {
                mPosition = intentThatStartedThisActivity.getIntExtra("position", 0);
                mRecipeObject = intentThatStartedThisActivity.getExtras().getParcelable("recipe_object");
            }
        } else {
            mPosition = savedInstanceState.getInt(STEP_POSITION);
            mRecipeObject = savedInstanceState.getParcelable(RECIPE_OBJECT);
        }

        mFragmentManager = getSupportFragmentManager();
        setupStepDetailFragments(false);
    }

    void setupStepDetailFragments(boolean replace) {

        Fragment desc_fragment = getSupportFragmentManager().findFragmentByTag("DESC_FRAGMENT");
        if (desc_fragment != null)
            getSupportFragmentManager().beginTransaction().remove(desc_fragment).commit();

        Fragment video_fragment = getSupportFragmentManager().findFragmentByTag("VIDEO_FRAGMENT");
        if (video_fragment != null)
            getSupportFragmentManager().beginTransaction().remove(video_fragment).commit();

        mDescLayout.setVisibility(View.VISIBLE);
        mVideoLayout.setVisibility(View.VISIBLE);
        mNavBtnLayouts.setVisibility(View.VISIBLE);

        mStepFragment = new RecipeStepDetailFragment();
        if (mPosition == 0) {
            mStepFragment.setDescription(0, buildIngredientList(mRecipeObject), mRecipeObject.number_of_steps);
        } else {
            mStepFragment.setDescription(mPosition, mRecipeObject.steps[mPosition - 1].description, mRecipeObject.number_of_steps);
        }

        mFragmentManager.beginTransaction()
                .add(R.id.description_container, mStepFragment, "DESC_FRAGMENT")
                .commit();

        if (mPosition == 0 || mRecipeObject.steps[mPosition - 1].videoURL.equals("")) {
            mVideoLayout.setVisibility(View.GONE);
        } else {
            mVideoFragment = new RecipeVideoFragment();
            mVideoFragment.setVideoUrl(mRecipeObject.steps[mPosition - 1].videoURL);
            mFragmentManager.beginTransaction()
                    .add(R.id.video_container, mVideoFragment, "VIDEO_FRAGMENT")
                    .commit();

            mVideoLayout.setVisibility(View.VISIBLE);
        }


        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (mPosition != 0 && !mRecipeObject.steps[mPosition - 1].videoURL.equals("")) {
                mDescLayout.setVisibility(View.GONE);
                mNavBtnLayouts.setVisibility(View.GONE);
            }
        }

        if (mPosition == 0) {
            mPrevButton.setVisibility(View.GONE);
        } else {
            mPrevButton.setVisibility(View.VISIBLE);
        }

        if (mPosition == mRecipeObject.number_of_steps) {
            mNextButton.setVisibility(View.GONE);
        } else {
            mNextButton.setVisibility(View.VISIBLE);
        }
    }

    private String buildIngredientList(RecipeObject recipe) {
        StringBuilder ingredientsList = new StringBuilder("");

        if (recipe != null) {
            ingredientsList.append("Ingredients: \n\n");
            for (int i = 0; i < recipe.number_of_ingredients; i++) {
                String ing = recipe.ingredients[i].ingredient + " : " + recipe.ingredients[i].quantity + "  " + recipe.ingredients[i].measure + "\n";
                ingredientsList.append(ing);
            }
        }
        return ingredientsList.toString();
    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putInt(STEP_POSITION, mPosition);
        currentState.putParcelable(RECIPE_OBJECT, mRecipeObject);
    }

    void handleNextButton() {
        if (mPosition != mRecipeObject.number_of_steps) {
            mPosition += 1;
        }
        setupStepDetailFragments(true);
    }

    void handlePrevButton() {
        if (mPosition != 0) {
            mPosition -= 1;
        }
        setupStepDetailFragments(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                handleNextButton();
                break;
            case R.id.btn_prev:
                handlePrevButton();
                break;
        }

    }
}


