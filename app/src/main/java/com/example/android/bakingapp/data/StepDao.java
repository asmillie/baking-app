package com.example.android.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StepDao {

    @Query("SELECT * FROM steps WHERE recipe_id = :recipeId")
    LiveData<List<Step>> getStepsByRecipeId(Integer recipeId);

    @Query("SELECT * FROM steps WHERE id = :id")
    LiveData<Step> getStepById(Integer id);

    @Insert
    void saveAllSteps(List<Step> steps);
}
