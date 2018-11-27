package com.example.android.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.data.RecipeAndInstructions;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private List<RecipeAndInstructions> mRecipeList;
    private Context mContext;
    private final RecipeClickListener mRecipeClickListener;

    public interface RecipeClickListener {
        void onRecipeClick(int recipeId);
    }

    RecipeListAdapter(List<RecipeAndInstructions> recipeList, RecipeClickListener clickListener) {
        this.mRecipeList = recipeList;
        this.mRecipeClickListener = clickListener;
    }

    void setRecipeList(List<RecipeAndInstructions> recipeList) {
        this.mRecipeList = recipeList;
    }

    RecipeAndInstructions getRecipeAndInstructions(int position) {
        return mRecipeList.get(position);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        public RecipeViewHolder(View itemView) {
            super(itemView);
        }
    }
}
