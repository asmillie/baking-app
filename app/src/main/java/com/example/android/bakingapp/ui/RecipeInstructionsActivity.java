package com.example.android.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.android.bakingapp.Constants;
import com.example.android.bakingapp.R;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeInstructionsActivity extends AppCompatActivity implements InstructionsFragment.OnStepSelectedListener {

    private RecipeInstructionsViewModel mViewModel;
    private Integer mRecipeId;

    @Nullable @BindView(R.id.step_fragment_container)
    FrameLayout stepFragmentContainer;

    @BindBool(R.bool.two_pane_mode_enabled) boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_instructions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        Integer recipeId = Constants.RECIPE_ID_EXTRA_DEFAULT;
        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getInt(Constants.RECIPE_ID_EXTRA);
        } else {
            Intent intent = getIntent();
            if (intent.hasExtra(Constants.RECIPE_ID_EXTRA)) {
                recipeId = intent.getIntExtra(Constants.RECIPE_ID_EXTRA, Constants.RECIPE_ID_EXTRA_DEFAULT);
            } else {
                missingRecipeID();
            }
        }

        if (recipeId.equals(Constants.RECIPE_ID_EXTRA_DEFAULT)) {
            missingRecipeID();
        } else {
            mRecipeId = recipeId;

            if (stepFragmentContainer != null) {
                mTwoPane = true;
            }

            initViewModel(recipeId);
            if (savedInstanceState == null) {
                initFragments();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.RECIPE_ID_EXTRA, mRecipeId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipeId = savedInstanceState.getInt(Constants.RECIPE_ID_EXTRA);
    }

    private void initViewModel(Integer recipeId) {
        RecipeInstructionsViewModelFactory factory = new RecipeInstructionsViewModelFactory(getApplication(), recipeId);

        mViewModel = ViewModelProviders.of(this, factory).get(RecipeInstructionsViewModel.class);

        if (mTwoPane) {
            mViewModel.setStepId(Constants.SELECTED_STEP_ID_DEFAULT);
        }
    }

    private void initFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        InstructionsFragment instructionsFragment = InstructionsFragment.newInstance();
        instructionsFragment.setRecipeId(mRecipeId);

        fragmentManager.beginTransaction()
                .add(R.id.instructions_fragment_container, instructionsFragment)
                .commit();

        if (mTwoPane) {
            RecipeStepFragment recipeStepFragment = RecipeStepFragment.newInstance(mRecipeId);

            fragmentManager.beginTransaction()
                    .add(R.id.step_fragment_container, recipeStepFragment)
                    .commit();
        }
    }

    private void missingRecipeID() {
        Toast toast = Toast.makeText(this, getString(R.string.no_recipe_id), Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }

    @Override
    public void onStepSelected(Integer stepId) {
        if (!mTwoPane) {
            Intent intent = new Intent(this, RecipeStepActivity.class);
            intent.putExtra(Constants.RECIPE_ID_EXTRA, mRecipeId);
            intent.putExtra(Constants.RECIPE_STEP_ID_EXTRA, stepId);
            startActivity(intent);
        } else if (mViewModel != null) {
            mViewModel.setStepId(stepId);
        }
    }
}
