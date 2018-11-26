package com.example.android.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
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

    @Query("SELECT * FROM ingredients WHERE id = :id")
    LiveData<Ingredient> getIngredientById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveIngredient(Ingredient ingredient);

    @Delete
    void deleteIngredient(Ingredient ingredient);
}
