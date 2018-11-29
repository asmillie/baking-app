package com.example.android.bakingapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.bakingapp.data.AppRepository;
import com.example.android.bakingapp.data.RecipeAndInstructions;

import java.util.List;

class RecipeInstructionsViewModel extends ViewModel {

    private final AppRepository mAppRepository;
    private final Integer mRecipeId;
    private LiveData<List<RecipeAndInstructions>> mRecipeAndInstructions;

    RecipeInstructionsViewModel(AppRepository appRepository, Integer recipeId) {
        this.mAppRepository = appRepository;
        this.mRecipeId = recipeId;
    }

    public LiveData<List<RecipeAndInstructions>> getRecipeAndInstructions() {
        if (mRecipeAndInstructions == null) {
            mRecipeAndInstructions = mAppRepository.getRecipesAndInstructionsById(mRecipeId);
        }
        return mRecipeAndInstructions;
    }
}
