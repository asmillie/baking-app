package com.example.android.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> getRecipes();

    @Query("SELECT id, name FROM recipes")
    List<Recipe> getRecipeNames();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveRecipes(List<Recipe> recipes);
}
