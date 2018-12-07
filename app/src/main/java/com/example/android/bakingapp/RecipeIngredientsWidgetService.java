package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.data.AppRepository;
import com.example.android.bakingapp.data.Recipe;

import java.util.List;

/**
 * Implemented following example
 * @ https://android.googlesource.com/platform/development/+/master/samples/StackWidget/src/com/example/android/stackwidget/StackWidgetService.java
 */
public class RecipeIngredientsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class IngredientListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = IngredientListRemoteViewsFactory.class.getSimpleName();

    private Context mContext;
    private int mAppWidgetId;
    private AppRepository mAppRepository;
    private List<Recipe> mRecipeList;

    IngredientListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mAppRepository = AppRepository.getInstance(mContext);
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged called, getting recipe list");
        mRecipeList = mAppRepository.getRecipeNames();
        Log.d(TAG, "Recipe List contains " + getCount() + " items");
    }

    @Override
    public void onDestroy() {
        mAppRepository = null;
        mRecipeList = null;
    }

    @Override
    public int getCount() {
        return mRecipeList == null ? 0 : mRecipeList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mRecipeList == null || mRecipeList.size() == 0) {
            Log.d(TAG, "Recipe List Empty, returning a null RemoteViews object");
            return null;
        }
        Log.d(TAG, "Building remote view from recipe list item @ position #" + position);
        Recipe recipe = mRecipeList.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_list_item);
        views.setTextViewText(R.id.recipe_name_tv, recipe.getName());

        Bundle extras = new Bundle();
        extras.putInt(Constants.RECIPE_ID_EXTRA, recipe.getId());

        Intent intent = new Intent();
        intent.putExtras(extras);

        views.setOnClickFillInIntent(R.id.recipe_name_tv, intent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
