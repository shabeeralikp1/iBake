package com.android.shabeerali.ibake;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.support.v4.app.FragmentManager;

import com.android.shabeerali.ibake.data.RecipeObject;

public class RecipeDetailActivity extends AppCompatActivity implements  RecipeStepsFragment.OnRecipeStepClickListener, View.OnClickListener {

    private static final String TAG = "iBake - RecipeDetailActivity";
    private RecipeObject mRecipe;
    private boolean mTwoPane;
    private int mPosition;

    FrameLayout mVideoLayout;
    FrameLayout mDescLayout;
    FragmentManager mFragmentManager;
    Button mNextButton;
    Button mPrevButton;
    TextView  mRecipenName;

    public static final String RECIPE_OBJECT = "recipe_object";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState == null) {
            Intent intentThatStartedThisActivity = getIntent();

            if (intentThatStartedThisActivity != null) {
                if(intentThatStartedThisActivity.hasExtra(RECIPE_OBJECT))
                    mRecipe = intentThatStartedThisActivity.getExtras().getParcelable(RECIPE_OBJECT);
            }
        } else {
            mRecipe = savedInstanceState.getParcelable(RECIPE_OBJECT);
        }

        mRecipenName = (TextView) findViewById(R.id.tv_recipe_name);
        mRecipenName.setText(mRecipe.name);
        mFragmentManager = getSupportFragmentManager();

        if(findViewById(R.id.step_details_linear_layout) != null) {
            mTwoPane = true;
            mNextButton = (Button) findViewById(R.id.btn_next);
            mPrevButton = (Button) findViewById(R.id.btn_prev);
            mNextButton.setOnClickListener(this);
            mPrevButton.setOnClickListener(this);

            mVideoLayout = (FrameLayout) findViewById(R.id.video_container) ;
            mDescLayout = (FrameLayout) findViewById(R.id.description_container) ;

            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            Fragment steps_fragment = getSupportFragmentManager().findFragmentByTag("STEPS_FRAGMENT");
            if(steps_fragment != null)
                getSupportFragmentManager().beginTransaction().remove(steps_fragment).commit();

            RecipeStepsFragment fragment = new RecipeStepsFragment();
            mFragmentManager.beginTransaction().replace(R.id.recipe_steps_list_container, fragment, "STEPS_FRAGMENT").commit();
            fragment.setData(mRecipe);
            mPosition = 0;
            setupStepDetailFragments();

        } else {
            mTwoPane = false;
            RecipeStepsFragment fragment = new RecipeStepsFragment();
            mFragmentManager.beginTransaction().replace(R.id.recipe_steps_list_container, fragment).commit();
            fragment.setData(mRecipe);
        }

    }

    public void onRecipeStepSelected(int position, RecipeObject recipeObject) {
        if(mTwoPane) {

            mPosition = position;
            mRecipe = recipeObject;
            setupStepDetailFragments();
        } else {

            Class destinationClass = RecipeStepsDetails.class;
            Intent intentToStartDetailActivity = new Intent(this, destinationClass);
            intentToStartDetailActivity.putExtra("position", position);
            intentToStartDetailActivity.putExtra("recipe_object", recipeObject);
            startActivity(intentToStartDetailActivity);
        }
    }


    private String buildIngredientList(RecipeObject recipe) {
        StringBuilder ingredientsList = new StringBuilder("");

        if(recipe != null) {
            ingredientsList.append("Ingredients: \n\n");
            for(int i = 0; i < recipe.number_of_ingredients; i++) {
                String ing = recipe.ingredients[i].ingredient  + " : " + recipe.ingredients[i].quantity  + "  " + recipe.ingredients[i].measure + "\n" ;
                ingredientsList.append(ing);
            }
        }

        return ingredientsList.toString();
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable(RECIPE_OBJECT, mRecipe);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void handleNextButton() {
        if(mPosition != mRecipe.number_of_steps ) {
            mPosition += 1;
        }
        setupStepDetailFragments();
    }

    void handlePrevButton() {
        if(mPosition != 0) {
            mPosition -= 1;
        }
        setupStepDetailFragments();
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

    void setupStepDetailFragments() {

        Fragment desc_fragment = getSupportFragmentManager().findFragmentByTag("DESC_FRAGMENT");
        if(desc_fragment != null)
            getSupportFragmentManager().beginTransaction().remove(desc_fragment).commit();

        Fragment video_fragment = getSupportFragmentManager().findFragmentByTag("VIDEO_FRAGMENT");
        if(video_fragment != null)
            getSupportFragmentManager().beginTransaction().remove(video_fragment).commit();

        mDescLayout.setVisibility(View.VISIBLE);
        mVideoLayout.setVisibility(View.VISIBLE);

        RecipeStepDetailFragment stepFragment = new RecipeStepDetailFragment();
        if(mPosition == 0) {
            stepFragment.setDescription(0, buildIngredientList(mRecipe), mRecipe.number_of_steps);
        } else {
            stepFragment.setDescription(mPosition, mRecipe.steps[mPosition - 1].description, mRecipe.number_of_steps );
        }

        mFragmentManager.beginTransaction()
                .replace(R.id.description_container, stepFragment,"DESC_FRAGMENT")
                .commit();


        if(mPosition == 0 || mRecipe.steps[mPosition - 1].videoURL.equals("")) {
            mVideoLayout.setVisibility(View.GONE);
        } else {
            RecipeVideoFragment videoFragment = new RecipeVideoFragment();
            videoFragment.setVideoUrl(mRecipe.steps[mPosition - 1].videoURL);
            mFragmentManager.beginTransaction()
                    .replace(R.id.video_container, videoFragment, "VIDEO_FRAGMENT")
                    .commit();
            mVideoLayout.setVisibility(View.VISIBLE);
        }

        if(mPosition == 0) {
            mPrevButton.setVisibility(View.GONE);
        } else {
            mPrevButton.setVisibility(View.VISIBLE);
        }

        if(mPosition == mRecipe.number_of_steps) {
            mNextButton.setVisibility(View.GONE);
        } else {
            mNextButton.setVisibility(View.VISIBLE);
        }
    }
}
