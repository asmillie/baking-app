package com.example.android.bakingapp.ui;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
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

    /**
     * LiveData Transformation created following docs
     * @ https://developer.android.com/reference/android/arch/lifecycle/Transformations
     */
    private final MutableLiveData<Integer> mStepId = new MutableLiveData<>();
    private final LiveData<Step> mStep = Transformations.switchMap(mStepId, new Function<Integer, LiveData<Step>>() {
        @Override
        public LiveData<Step> apply(Integer stepId) {
            if (mAppRepository != null) {
                return mAppRepository.getStepById(stepId);
            }
            return null;
        }
    });

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

    void setStepId(Integer stepId) {
        if (!stepId.equals(mStepId.getValue())) {
            mStepId.setValue(stepId);
        }
    }

    LiveData<Step> getStep() {
        return mStep;
    }
}
