package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
public class RecipeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class RecipeListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = RecipeListRemoteViewsFactory.class.getSimpleName();

    private Context mContext;
    private int mAppWidgetId;
    private AppRepository mAppRepository;
    private List<Recipe> mRecipeList;

    RecipeListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mAppRepository = AppRepository.getInstance(mContext);
    }

    @Override
    public void onDataSetChanged() {
        mRecipeList = mAppRepository.getRecipeNames();
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
        if (getCount() == 0) {
            return null;
        }

        Recipe recipe = mRecipeList.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_list_item);
        views.setTextViewText(R.id.recipe_name_tv, recipe.getName());

        Bundle extras = new Bundle();
        extras.putInt(Constants.WIDGET_RECIPE_ID_EXTRA, recipe.getId());
        extras.putString(Constants.WIDGET_RECIPE_NAME_EXTRA, recipe.getName());

        Intent intent = new Intent();
        intent.putExtras(extras);

        views.setOnClickFillInIntent(R.id.recipe_list_item, intent);

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
