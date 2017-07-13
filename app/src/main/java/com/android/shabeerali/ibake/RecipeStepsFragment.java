package com.android.shabeerali.ibake;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import com.android.shabeerali.ibake.data.RecipeObject;

public class RecipeStepsFragment extends Fragment implements RecipeStepsAdapter.RecipeListAdapterOnClickHandler  {
    private static final String TAG = "iBake - RecipeStepsFragment";

    protected RecyclerView mRecyclerView;
    protected RecipeStepsAdapter mAdapter;
    RecipeObject mRecipeObject;

    OnRecipeStepClickListener mCallback;

    public interface OnRecipeStepClickListener {
        void onRecipeStepSelected(int position, RecipeObject recipeObject);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void   setData(RecipeObject object) {
        mRecipeObject = object;
        if(mAdapter != null)
            mAdapter.setRecipesList(mRecipeObject);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnRecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeStepClickListener");
        }
    }
    @Override
    public void onClick(int position) {
        mCallback.onRecipeStepSelected(position, mRecipeObject);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_frag, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_recipe_steps);

        int scrollPosition = 0;
        scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                .findFirstCompletelyVisibleItemPosition();

        mRecyclerView.scrollToPosition(scrollPosition);
        mAdapter = new RecipeStepsAdapter(this.getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setRecipesList(mRecipeObject);

        return rootView;
    }
}
