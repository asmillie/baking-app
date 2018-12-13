package com.example.android.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakingapp.Constants;
import com.example.android.bakingapp.R;

public class RecipeStepActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepActivity.class.getSimpleName();

    private RecipeInstructionsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Integer recipeId = intent.getIntExtra(Constants.RECIPE_ID_EXTRA, Constants.RECIPE_ID_EXTRA_DEFAULT);
        Integer stepId = intent.getIntExtra(Constants.RECIPE_STEP_ID_EXTRA, Constants.RECIPE_STEP_ID_DEFAULT);

        if (recipeId.equals(Constants.RECIPE_ID_EXTRA_DEFAULT) || stepId.equals(Constants.RECIPE_STEP_ID_DEFAULT)) {
            finish();
            //TODO Toast message for missing id?
        }

        initViewModel(recipeId, stepId);

        if (savedInstanceState == null) {
            RecipeStepFragment recipeStepFragment = RecipeStepFragment.newInstance(recipeId);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_fragment_container, recipeStepFragment)
                    .commit();
        }
    }

    private void initViewModel(Integer recipeId, Integer stepId) {
        if (recipeId != null && stepId != null) {
            RecipeInstructionsViewModelFactory factory = new RecipeInstructionsViewModelFactory(getApplication(), recipeId);

            mViewModel = ViewModelProviders.of(this, factory).get(RecipeInstructionsViewModel.class);

            mViewModel.setStepId(stepId);
        }
    }
}
