package com.example.android.bakingapp.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface RecipeService {
    @GET("baking.json")
    Call<List<Recipe>> listRecipes();
}
