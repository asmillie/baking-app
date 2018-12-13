package com.example.android.bakingapp.data;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingapp.AppExecutors;
import com.example.android.bakingapp.Constants;
import com.example.android.bakingapp.utils.NetworkUtils;

import java.util.ArrayList;
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

    private final RecipeService mRecipeService;
    private final AppDatabase mDatabase;

    private final Context mContext;

    private AppRepository(Context context) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.RECIPE_LIST_JSON_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRecipeService = retrofit.create(RecipeService.class);

        mDatabase = AppDatabase.getInstance(context);
        mContext = context;
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
        LiveData<List<Recipe>> recipes = mDatabase.recipeDao().getRecipes();
        //TODO: Trigger refreshRecipes by some metric (ie. last_updated), which will
        //update the database after contacting the server for fresh data. This method
        //will then always return from the database.

        if (recipes == null || recipes.getValue() == null || recipes.getValue().size() == 0) {
            refreshRecipes();
        }

        return recipes;
    }

    private void refreshRecipes() {
        if (!NetworkUtils.isConnected(mContext)) {
            return;
        }

        mRecipeService.listRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<Recipe> recipes = response.body();

                    if (recipes != null && recipes.size() > 0) {
                        //Loop through recipes, assign foreign keys (Recipe ID) and then save
                        List<Ingredient> ingredientsAllRecipes = new ArrayList<>();
                        List<Step> stepsAllRecipes = new ArrayList<>();
                        for (Recipe recipe: recipes) {
                            for (Ingredient ingredient: recipe.getIngredients()) {
                                ingredient.setRecipeId(recipe.getId());
                                ingredientsAllRecipes.add(ingredient);
                            }

                            for (Step step: recipe.getSteps()) {
                                step.setRecipeId(recipe.getId());
                                stepsAllRecipes.add(step);
                            }
                        }

                        saveRecipes(recipes);
                        saveIngredients(ingredientsAllRecipes);
                        saveSteps(stepsAllRecipes);

                        Log.d(TAG, "Recipes successfully retrieved from api and saved in db.");
                    }

                } else {
                    Log.d(TAG, "Retrofit received response but encountered error retrieving data");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "Error contacting server for recipes: " + t.toString());
            }
        });
    }

    private void saveRecipes(final List<Recipe> recipes) {
        if (recipes != null && recipes.size() > 0) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.recipeDao().saveRecipes(recipes);
                }
            });
        }
    }

    private void saveIngredients(final List<Ingredient> ingredients) {
        if (ingredients != null && ingredients.size() > 0) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.ingredientDao().saveAllIngredients(ingredients);
                }
            });
        }
    }

    private void saveSteps(final List<Step> steps) {
        if (steps != null && steps.size() > 0) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.stepDao().saveAllSteps(steps);
                }
            });
        }
    }

    public LiveData<List<Ingredient>> getIngredients() {
        return mDatabase.ingredientDao().getIngredients();
    }

    public LiveData<List<Ingredient>> getIngredientsByRecipeId(Integer recipeId) {
        return mDatabase.ingredientDao().getIngredientsByRecipeId(recipeId);
    }

    public LiveData<List<Step>> getStepsByRecipeId(Integer recipeId) {
        return mDatabase.stepDao().getStepsByRecipeId(recipeId);
    }

    public LiveData<Step> getStepById(Integer stepId) {
        return mDatabase.stepDao().getStepById(stepId);
    }

    /**
     * Returns list of recipes (names and ids only) for ingredients widget
     * @return List of Recipes
     */
    public List<Recipe> getRecipeNames() {
        return mDatabase.recipeDao().getRecipeNames();
    }

    /**
     * Returns list of ingredients for a recipe for ingredients widget
     * @param recipeId The id of the recipe to get ingredients list for
     * @return List of ingredients
     */
    public List<Ingredient> getIngredientListByRecipeId(Integer recipeId) {
        return mDatabase.ingredientDao().getIngredientListByRecipeId(recipeId);
    }
}
