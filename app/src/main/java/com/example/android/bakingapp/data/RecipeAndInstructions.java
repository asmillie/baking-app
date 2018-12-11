package com.example.android.bakingapp.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class RecipeAndInstructions {

    @Embedded
    public Recipe recipe;

    @Relation(parentColumn = "id", entityColumn = "recipe_id", entity = Ingredient.class)
    public List<Ingredient> ingredients;

    @Relation(parentColumn = "id", entityColumn = "recipe_id", entity = Step.class)
    public List<Step> steps;

    RecipeAndInstructions() {}

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
