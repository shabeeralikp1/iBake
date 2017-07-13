package com.android.shabeerali.ibake;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shabeerali.ibake.data.RecipeObject;
import com.squareup.picasso.Picasso;


public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipeListAdapterViewHolder> {
    private static final String TAG = "iBake - RecipesListAdapter";

    private RecipeObject[]  mRecipesList;
    private final RecipeListAdapterOnClickHandler mClickHandler;
    private Context context;


    public RecipesListAdapter(Context context, RecipeListAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;
    }

    public interface RecipeListAdapterOnClickHandler {
        void onClick(RecipeObject recipeInfo);
    }


    @Override
    public RecipeListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        RecipeListAdapterViewHolder rcv = new RecipeListAdapterViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecipeListAdapterViewHolder holder, int position) {
        holder.mRecipeImage.setVisibility(View.VISIBLE);
        holder.mRecipeName.setText(mRecipesList[position].name);
        if(! mRecipesList[position].image.equals("")) {
            Picasso.with(context).load(mRecipesList[position].image).into(holder.mRecipeImage);
        } else {
            holder.mRecipeImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mRecipesList) return 0;
        return mRecipesList.length;
    }

    public class RecipeListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mRecipeName;
        public ImageView mRecipeImage;

        public RecipeListAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mRecipeName = (TextView) itemView.findViewById(R.id.tv_recipe_name);
            mRecipeImage = (ImageView) itemView.findViewById(R.id.iv_recipe_image);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            RecipeObject recipeData = mRecipesList[adapterPosition];
            mClickHandler.onClick(recipeData);
        }
    }

    public void setRecipesList(RecipeObject[] recipesData) {
        mRecipesList = recipesData;
        notifyDataSetChanged();
    }
}
