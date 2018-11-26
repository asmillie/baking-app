package com.example.android.bakingapp.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.bakingapp.data.AppRepository;
import com.example.android.bakingapp.data.RecipeAndInstructions;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    private final AppRepository mRepository;
    private LiveData<List<RecipeAndInstructions>> mRecipeList;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    LiveData<List<RecipeAndInstructions>> getRecipeList() {
        if (mRecipeList == null) {
            mRecipeList = mRepository.getRecipesAndInstructions();
        }
        return mRecipeList;
    }
}
