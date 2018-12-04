package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.android.bakingapp.Constants;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Integer recipeId = intent.getIntExtra(Constants.RECIPE_ID_EXTRA, Constants.RECIPE_STEP_ID_DEFAULT);
        Step step = intent.getParcelableExtra(Constants.RECIPE_STEP_BUNDLE_EXTRA);

        if (recipeId.equals(Constants.RECIPE_ID_EXTRA_DEFAULT) || step == null) {
            finish();
            //TODO Toast message for missing id?
        }

        RecipeStepFragment recipeStepFragment = RecipeStepFragment.newInstance(recipeId, step);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_step_fragment_container, recipeStepFragment)
                .commit();

    }
}
