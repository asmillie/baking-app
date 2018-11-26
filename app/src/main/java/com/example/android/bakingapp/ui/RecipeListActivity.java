package com.example.android.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.RecipeAndInstructions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity {

    private RecipeListViewModel mViewModel;
    private List<RecipeAndInstructions> mRecipeList;

    @BindView(R.id.recipe_list_tv) public TextView mRecipeListTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        initViewModel();

        ButterKnife.bind(this);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        mViewModel.getRecipeList().observe(this, new Observer<List<RecipeAndInstructions>>() {
            @Override
            public void onChanged(@Nullable List<RecipeAndInstructions> recipeAndInstructions) {
                mRecipeList = recipeAndInstructions;
                clearUI();
                populateUI();
            }
        });


    }

    private void clearUI() {
        mRecipeListTV.setText("");
    }

    private void populateUI() {
        if (mRecipeList != null) {
            for (RecipeAndInstructions recipe: mRecipeList) {
                mRecipeListTV.append(recipe.recipe.getName() + " \n");

                List<Ingredient> ingredients = recipe.getIngredients();
                if (ingredients != null && ingredients.size() > 0) {
                    mRecipeListTV.append("--Ingredients:");
                    for (Ingredient ingredient: ingredients) {
                        mRecipeListTV.append("--" + ingredient.getIngredient());
                    }
                }
            }
        }
    }
}
