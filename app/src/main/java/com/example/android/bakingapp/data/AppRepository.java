package com.example.android.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.example.android.bakingapp.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppRepository {

    private final static String TAG = AppRepository.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static AppRepository sInstance;

    private RecipeService mRecipeService;
    //TODO: Continue implementing Retrofit to retrieve JSON list of recipes (refer to popular movies project)
    private AppRepository(Context context) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.RECIPE_LIST_JSON_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRecipeService = retrofit.create(RecipeService.class);
    }

    //Singleton instantiation of Repository, modified from AppDatabase.java
    public static AppRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppRepository(context);
            }
        }
        return sInstance;
    }

    public LiveData<List<Recipe>> getRecipes() {
        //TODO: Trigger refreshRecipes by some metric (ie. last_updated), which will
        //update the database after contacting the server for fresh data. This method
        //will then always return from the database.
        return refreshRecipes();
    }

    public LiveData<List<Recipe>> refreshRecipes() {
        final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
        mRecipeService.listRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    recipes.setValue(response.body());
                } else {
                    Log.d(TAG, "Retrofit received response but encountered error retrieving data");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "Error contacting server for recipes: " + t.toString());
            }
        });

        return recipes;
    }
}
