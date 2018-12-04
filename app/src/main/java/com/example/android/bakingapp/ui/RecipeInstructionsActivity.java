package com.example.android.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingapp.Constants;
import com.example.android.bakingapp.R;

public class RecipeInstructionsActivity extends AppCompatActivity implements InstructionsFragment.OnFragmentInteractionListener {

    private static final String TAG = RecipeInstructionsActivity.class.getSimpleName();

    //TODO Create Master / Detail Fragments
    //TODO Implement ViewModels & Factories
    //TODO Set up Master / Detail Views based on device screen width
    private RecipeInstructionsViewModel mViewModel;
    private Integer mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_instructions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Integer recipeId = Constants.RECIPE_ID_EXTRA_DEFAULT;
        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getInt(Constants.RECIPE_ID_EXTRA);
            Log.d(TAG, "onCreate: Assigned recipeId from savedInstanceState");
        } else {
            Log.d(TAG, "onCreate: No savedInstanceState, Proceeding to check Intent");
            Intent intent = getIntent();
            if (intent.hasExtra(Constants.RECIPE_ID_EXTRA)) {
                recipeId = intent.getIntExtra(Constants.RECIPE_ID_EXTRA, Constants.RECIPE_ID_EXTRA_DEFAULT);
                Log.d(TAG, "onCreate: Assigned recipeId from Intent");
            } else {
                missingRecipeID();
            }
        }

        if (recipeId.equals(Constants.RECIPE_ID_EXTRA_DEFAULT)) {
            missingRecipeID();
        } else {
            mRecipeId = recipeId;
            initViewModel(recipeId);
            initFragments();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.RECIPE_ID_EXTRA, mRecipeId);
        Log.d(TAG, "onSaveInstanceState: Recipe Id saved");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipeId = savedInstanceState.getInt(Constants.RECIPE_ID_EXTRA);
        Log.d(TAG, "onRestoreInstanceState: Recipe Id assigned");
    }

    private void initViewModel(Integer recipeId) {
        RecipeInstructionsViewModelFactory factory = new RecipeInstructionsViewModelFactory(getApplication(), recipeId);

        mViewModel = ViewModelProviders.of(this, factory).get(RecipeInstructionsViewModel.class);
        //TODO Observers in fragment
    }

    private void initFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        InstructionsFragment instructionsFragment = InstructionsFragment.newInstance();
        instructionsFragment.setRecipeId(mRecipeId);

        fragmentManager.beginTransaction()
                .add(R.id.instructions_container, instructionsFragment)
                .commit();
    }

    private void missingRecipeID() {
        Toast toast = Toast.makeText(this, "No recipe selected", Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
