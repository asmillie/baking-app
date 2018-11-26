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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity {

    private RecipeListViewModel mViewModel;
    private List<Recipe> mRecipeList;
    private List<Ingredient> mIngredientList;

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

        mViewModel.getRecipeList().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                mRecipeList = recipes;
                populateUI();
            }
        });

        mViewModel.getIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                mIngredientList = ingredients;
                populateUI();
            }
        });
    }

    private void populateUI() {
        if (mRecipeList != null) {
            for (Recipe recipe: mRecipeList) {
                mRecipeListTV.append(recipe.getName() + " \n");

            }
        }

        if (mIngredientList != null) {
            mRecipeListTV.append("\n\n");
            for (Ingredient ingredient: mIngredientList) {
                mRecipeListTV.append(ingredient.getIngredient() + "\n");
            }
        }
    }
}
