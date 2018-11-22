package com.example.android.bakingapp.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeListService {
    @GET()
    Call<List<Recipe>> listRecipes();
}
