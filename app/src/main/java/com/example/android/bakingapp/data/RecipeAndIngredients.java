package com.example.android.bakingapp.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class RecipeAndIngredients {

    @Embedded
    public Recipe recipe;

    @Relation(parentColumn = "id", entityColumn = "recipe_id", entity = Ingredient.class)
    public List<Ingredient> ingredients;

    RecipeAndIngredients() {}

    public Recipe getRecipe() {
        return recipe;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
