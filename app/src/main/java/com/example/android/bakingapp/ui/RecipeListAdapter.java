package com.example.android.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipeList;
    private Context mContext;
    private final RecipeClickListener mRecipeClickListener;

    public interface RecipeClickListener {
        void onRecipeClick(int recipeId);
    }

    RecipeListAdapter(List<Recipe> recipeList, RecipeClickListener clickListener) {
        this.mRecipeList = recipeList;
        this.mRecipeClickListener = clickListener;
    }

    void setRecipeList(List<Recipe> recipeList) {
        this.mRecipeList = recipeList;
        notifyDataSetChanged();
    }

    Recipe getRecipe(int position) {
        return mRecipeList.get(position);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);

        holder.mNameTV.setText(recipe.getName());
        holder.mServingsTV.setText("Makes " + recipe.getServings() + " Servings"); //TODO: Resource string with placeholder
    }

    @Override
    public int getItemCount() {
        return mRecipeList != null ? mRecipeList.size() : 0;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name_tv) TextView mNameTV;
        @BindView(R.id.servings_tv) TextView mServingsTV;

        @BindView(R.id.view_recipe_btn)
        Button mViewRecipeBtn;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mViewRecipeBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = mRecipeList.get(getAdapterPosition()).getId();
            mRecipeClickListener.onRecipeClick(elementId);
        }
    }
}
