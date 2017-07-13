package com.android.shabeerali.ibake;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.shabeerali.ibake.data.RecipeObject;
import com.android.shabeerali.ibake.utilities.NetworkUtils;
import com.android.shabeerali.ibake.utilities.RecipeDataJsonParser;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity  implements RecipesListAdapter.RecipeListAdapterOnClickHandler {

    private static final String TAG = "iBake - MainActivity";

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private LinearLayout mErrorLayout;
    private Button mTryAgainButton;

    private Context main_context;
    private RecipesListAdapter mRecipesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_context = this;
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorLayout  = (LinearLayout) findViewById(R.id.error_layout);
        mTryAgainButton = (Button) findViewById(R.id.bt_try_again);

        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRecipeLists();
            }
        });
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_recipes_list);

        mRecipesAdapter= new RecipesListAdapter(MainActivity.this, this);
        mRecyclerView.setAdapter(mRecipesAdapter);

        Configuration config = this.getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        loadRecipeLists();

    }

    void loadRecipeLists() {
        if(isOnline()) {
            showRecipesListView();
            new FetchRecipeListsTask().execute(this.getResources().getString(R.string.recipes_url));
        } else {
            showErrorMessage(this.getResources().getString(R.string.no_internet_connection));
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showRecipesListView() {
        mErrorLayout.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String message) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setText(message);
        mErrorLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(RecipeObject RecipeData) {
        Context context = this;
        Class destinationClass = RecipeDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("recipe_object", RecipeData);
        startActivity(intentToStartDetailActivity);
    }

    public class FetchRecipeListsTask extends AsyncTask<String, Void, RecipeObject[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected RecipeObject[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            URL url = null;

            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                String jsonRecipenfoResponse = NetworkUtils
                        .getResponseFromHttpUrl(url);

                RecipeObject[] recipebjects = RecipeDataJsonParser
                        .parseRecipeListInformation( jsonRecipenfoResponse);
                return recipebjects;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(RecipeObject[] recipesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (recipesData != null) {
                showRecipesListView();
                mRecipesAdapter.setRecipesList(recipesData);
            } else {
                showErrorMessage(main_context.getResources().getString(R.string.error_message));
            }
        }
    }
}
