package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.example.android.bakingapp.utils.PreferenceUtils;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {

    private static final String TAG = RecipeIngredientsWidget.class.getSimpleName();

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views;
        Integer recipeId = PreferenceUtils.getWidgetRecipeId(context, appWidgetId);
        if (recipeId.equals(Constants.RECIPE_ID_EXTRA_DEFAULT)) {
            views = getRecipeList(context, appWidgetId);
        } else {
            views = getIngredientList(context, appWidgetId, recipeId);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        final String action = intent.getAction();
        if (action.equals(Constants.WIDGET_GET_INGREDIENTS_ACTION)) {
            final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            final Integer recipeId = intent.getIntExtra(Constants.WIDGET_RECIPE_ID_EXTRA, Constants.RECIPE_ID_EXTRA_DEFAULT);
            final String recipeName = intent.getStringExtra(Constants.WIDGET_RECIPE_NAME_EXTRA);

            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                PreferenceUtils.saveWidgetRecipeId(context, appWidgetId, recipeId);
                PreferenceUtils.saveWidgetRecipeName(context, appWidgetId, recipeName);
                updateAppWidget(context, manager, appWidgetId);
            }
        } else if (action.equals(Constants.WIDGET_GET_RECIPES_ACTION)) {
            final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                PreferenceUtils.deleteWidgetRecipeId(context, appWidgetId);
                PreferenceUtils.deleteWidgetRecipeName(context, appWidgetId);
                updateAppWidget(context, manager, appWidgetId);
            }
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int x=0; x < appWidgetIds.length; x++) {
            PreferenceUtils.deleteWidgetRecipeId(context, appWidgetIds[x]);
            PreferenceUtils.deleteWidgetRecipeName(context, appWidgetIds[x]);
        }
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * Creates and returns the list of recipes
     * @param context Application context
     * @return RemoteViews containing recipes for a ListView
     */
    private static RemoteViews getRecipeList(Context context, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipes);

        Intent intent = new Intent(context, RecipeWidgetService.class);
        views.setRemoteAdapter(R.id.recipe_list_lv, intent);
        views.setEmptyView(R.id.recipe_list_lv, R.id.empty_view);

        Intent selectRecipeIntent = new Intent(context, RecipeIngredientsWidget.class);
        selectRecipeIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        selectRecipeIntent.setAction(Constants.WIDGET_GET_INGREDIENTS_ACTION);

        PendingIntent selectRecipePendingIntent = PendingIntent.getBroadcast(context, 0, selectRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.recipe_list_lv, selectRecipePendingIntent);

        return views;
    }

    private static RemoteViews getIngredientList(Context context, int appWidgetId, Integer recipeId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_ingredients);
        //Get and assign recipe name
        String recipeName = PreferenceUtils.getRecipeNameByWidgetId(context, appWidgetId);
        views.setTextViewText(R.id.recipe_name_tv, recipeName);
        //Setup intent for back button
        Intent backIntent = new Intent(context, RecipeIngredientsWidget.class);
        backIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        backIntent.setAction(Constants.WIDGET_GET_RECIPES_ACTION);
        //Assign back button with pending intent
        PendingIntent backPendingIntent = PendingIntent.getBroadcast(context, 0, backIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.back_btn, backPendingIntent);
        //Setup ingredient list item adapter (RemoteViewsFactory)
        Intent intent = new Intent(context, RecipeIngredientsWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra(Constants.WIDGET_RECIPE_ID_EXTRA, recipeId);
        views.setRemoteAdapter(R.id.ingredients_lv, intent);

        views.setEmptyView(R.id.ingredients_lv, R.id.empty_view);

        return views;
    }
}

