package com.example.android.bakingapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.bakingapp.data.AppRepository;
import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Step;

import java.util.List;

class RecipeInstructionsViewModel extends ViewModel {

    private final AppRepository mAppRepository;
    private final Integer mRecipeId;
    private LiveData<List<Ingredient>> mIngredients;
    private LiveData<List<Step>> mSteps;
    private LiveData<Step> mStep;

    RecipeInstructionsViewModel(AppRepository appRepository, Integer recipeId) {
        this.mAppRepository = appRepository;
        this.mRecipeId = recipeId;
    }

    LiveData<List<Ingredient>> getIngredients() {
        if (mIngredients == null) {
            mIngredients = mAppRepository.getIngredientsByRecipeId(mRecipeId);
        }
        return mIngredients;
    }

    LiveData<List<Step>> getSteps() {
        if (mSteps == null) {
            mSteps = mAppRepository.getStepsByRecipeId(mRecipeId);
        }
        return mSteps;
    }
}
