package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.bakingapp.Constants;
import com.example.android.bakingapp.R;

public class RecipeInstructionsActivity extends AppCompatActivity {

    //TODO Create Master / Detail Fragments
    //TODO Implement ViewModels & Factories
    //TODO Set up Master / Detail Views based on device screen width
    //TODO Create intent to launch this activity in RecipeListActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_instructions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.RECIPE_ID_EXTRA)) {
            Integer recipeId = intent.getIntExtra(Constants.RECIPE_ID_EXTRA, Constants.RECIPE_ID_EXTRA_DEFAULT);
            if (recipeId.equals(Constants.RECIPE_ID_EXTRA_DEFAULT)) {
                missingRecipeID();
            } else {
                //TODO Create ViewModelFactory
                //TODO Create ViewModel with Factory
            }
        } else {
            missingRecipeID();
        }
    }

    private void missingRecipeID() {
        Toast toast = Toast.makeText(this, "No recipe selected", Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }
}
