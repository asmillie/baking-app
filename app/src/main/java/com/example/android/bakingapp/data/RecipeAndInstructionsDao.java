package com.example.android.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

/**
 * Implemented with reference to https://stackoverflow.com/questions/44330452/android-persistence-room-cannot-figure-out-how-to-read-this-field-from-a-curso/44424148#44424148
 */
@Dao
public interface RecipeAndInstructionsDao {
    @Transaction
    @Query("SELECT * FROM recipes")
    LiveData<List<RecipeAndInstructions>> getRecipesAndInstructions();

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    LiveData<List<RecipeAndInstructions>> getRecipeAndInstructionsById(Integer id);
}
