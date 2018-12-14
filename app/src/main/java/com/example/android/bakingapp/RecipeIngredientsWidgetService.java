package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.data.AppRepository;
import com.example.android.bakingapp.data.Ingredient;

import java.util.List;

public class RecipeIngredientsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class IngredientsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private AppRepository mAppRepository;
    private final Integer mRecipeId;
    private List<Ingredient> mIngredientList;
    private final Context mContext;

    IngredientsListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mRecipeId = intent.getIntExtra(Constants.WIDGET_RECIPE_ID_EXTRA, Constants.RECIPE_ID_EXTRA_DEFAULT);
    }

    @Override
    public void onCreate() {
        mAppRepository = AppRepository.getInstance(mContext);
    }

    @Override
    public void onDataSetChanged() {
        if (!mRecipeId.equals(Constants.RECIPE_ID_EXTRA_DEFAULT)) {
            mIngredientList = mAppRepository.getIngredientListByRecipeId(mRecipeId);
        }
    }

    @Override
    public void onDestroy() {
        mIngredientList = null;
        mAppRepository = null;
    }

    @Override
    public int getCount() {
        return mIngredientList != null ? mIngredientList.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (getCount() == 0) {
            return null;
        }

        Ingredient ingredient = mIngredientList.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_list_item);
        views.setTextViewText(R.id.measure_tv, ingredient.getMeasure());
        views.setTextViewText(R.id.quantity_tv, ingredient.getQuantity() + "");
        views.setTextViewText(R.id.ingredient_tv, ingredient.getIngredient());

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
