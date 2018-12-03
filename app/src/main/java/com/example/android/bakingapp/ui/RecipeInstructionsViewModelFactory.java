package com.example.android.bakingapp.ui;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.bakingapp.data.AppRepository;

public class RecipeInstructionsViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final AppRepository mAppRepository;
    private final Integer mRecipeId;

    RecipeInstructionsViewModelFactory(@NonNull Application application, Integer recipeId) {
        super(application);
        mAppRepository = AppRepository.getInstance(application.getApplicationContext());
        mRecipeId = recipeId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeInstructionsViewModel(mAppRepository, mRecipeId);
    }
}
