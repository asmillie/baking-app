package com.example.android.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM ingredients")
    LiveData<List<Ingredient>> getIngredients();

    @Query("SELECT * FROM ingredients WHERE recipe_id = :recipeId")
    LiveData<List<Ingredient>> getIngredientsByRecipeId(Integer recipeId);

    @Query("SELECT * FROM ingredients WHERE recipe_id = :recipeId")
    List<Ingredient> getIngredientListByRecipeId(Integer recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAllIngredients(List<Ingredient> ingredients);
}
