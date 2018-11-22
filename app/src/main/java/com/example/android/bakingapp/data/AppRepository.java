package com.example.android.bakingapp.data;

import com.example.android.bakingapp.Constants;

import retrofit2.Retrofit;

public class AppRepository {

    private RecipeListService mRecipeListService;
    //TODO: Continue implementing Retrofit to retrieve JSON list of recipes (refer to popular movies project)
    public AppRepository() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.RECIPE_LIST_JSON_PATH)
                .build();

        mRecipeListService = retrofit.create(RecipeListService.class);
    }
}
