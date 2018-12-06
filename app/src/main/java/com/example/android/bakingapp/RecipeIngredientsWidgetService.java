package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
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

    private Context mContext;
    private int mAppWidgetId;
    private AppRepository mAppRepository;
    private List<Recipe> mRecipeList;

    public IngredientListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mAppRepository = AppRepository.getInstance(mContext);
    }

    @Override
    public void onDataSetChanged() {

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
        mRecipeList = mAppRepository.getRecipeNames().getValue();

        //TODO Create list item xml file
        //TODO Create remote views that inflates xml layout
        //TODO Build layout with list items
        //TODO Pending intents on list items to launch list of ingredients

        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
