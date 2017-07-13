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


public class RecipeStepsAdapter  extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsAdapterViewHolder> {
    private static final String TAG = "iBake - iRecipeStepsAdapter";

    private RecipeObject  mRecipeObject;
    private final RecipeListAdapterOnClickHandler mClickHandler;
    private Context context;


    public RecipeStepsAdapter(Context context, RecipeListAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;
    }

    public interface RecipeListAdapterOnClickHandler {
        void onClick(int position);
    }


    @Override
    public RecipeStepsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_item,  parent, false);
        RecipeStepsAdapterViewHolder rcv = new RecipeStepsAdapterViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecipeStepsAdapterViewHolder holder, int position) {
        if(position == 0) {
            holder.mRecipeStepDesc.setText("Ingredients");
        } else {
            holder.mRecipeThumbnailImg.setVisibility(View.VISIBLE);
            holder.mRecipeStepDesc.setText(mRecipeObject.steps[position - 1].shortDescription);
            if(!mRecipeObject.steps[position - 1].thumbnailURL.equals("")) {
                Picasso.with(context).load(mRecipeObject.steps[position - 1].thumbnailURL).into(holder.mRecipeThumbnailImg);
            } else {
                holder.mRecipeThumbnailImg.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (null == mRecipeObject) return 0;
        return mRecipeObject.steps.length + 1;
    }

    public class RecipeStepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mRecipeStepDesc;
        public ImageView mRecipeThumbnailImg;

        public RecipeStepsAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mRecipeStepDesc = (TextView) itemView.findViewById(R.id.tv_recipe_step_short_desc);
            mRecipeThumbnailImg = (ImageView) itemView.findViewById(R.id.tv_recipe_step_thumbnail_img);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    public void setRecipesList(RecipeObject recipesData) {
        mRecipeObject = recipesData;
        notifyDataSetChanged();
    }
}
