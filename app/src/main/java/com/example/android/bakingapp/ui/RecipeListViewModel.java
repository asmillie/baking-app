package com.example.android.bakingapp.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.bakingapp.data.AppRepository;
import com.example.android.bakingapp.data.Recipe;

import java.util.List;

class RecipeListViewModel extends AndroidViewModel {

    private final AppRepository mRepository;
    private LiveData<List<Recipe>> mRecipeList;

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        mRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    LiveData<List<Recipe>> getRecipeList() {
        if (mRecipeList == null) {
            mRecipeList = mRepository.getRecipes(getApplication().getApplicationContext());
        }
        return mRecipeList;
    }
}
