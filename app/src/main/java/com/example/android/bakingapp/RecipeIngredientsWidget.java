package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {

    private static final String TAG = RecipeIngredientsWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = getRecipeList(context);

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
        if (intent.getAction().equals(Constants.WIDGET_SELECT_RECIPE_ACTION)) {
            Integer recipeId = intent.getIntExtra(Constants.WIDGET_RECIPE_ID_EXTRA, Constants.RECIPE_ID_EXTRA_DEFAULT);
            Log.d(TAG, "Received intent with recipe id #" + recipeId);
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

    /**
     * Creates and returns the list of recipes
     * @param context Application context
     * @return RemoteViews containing recipes for a ListView
     */
    private static RemoteViews getRecipeList(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);

        Intent intent = new Intent(context, RecipeWidgetService.class);
        views.setRemoteAdapter(R.id.recipe_list_lv, intent);
        views.setEmptyView(R.id.recipe_list_lv, R.id.empty_view);

        Intent selectRecipeIntent = new Intent(context, RecipeIngredientsWidget.class);
        selectRecipeIntent.setAction(Constants.WIDGET_SELECT_RECIPE_ACTION);

        PendingIntent selectRecipePendingIntent = PendingIntent.getBroadcast(context, 0, selectRecipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.recipe_list_lv, selectRecipePendingIntent);

        return views;
    }
}

