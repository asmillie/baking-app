package com.example.android.bakingapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

@Dao
public interface RecipeAndIngredientsDao {
    @Transaction
    @Query("SELECT * FROM recipes WHERE recipes.id = :id")
    List<RecipeAndIngredients> getRecipeAndIngredientsById(Integer id);
}
