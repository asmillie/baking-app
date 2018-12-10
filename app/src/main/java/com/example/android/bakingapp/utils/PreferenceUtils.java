package com.example.android.bakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.bakingapp.Constants;

public class PreferenceUtils {

    private PreferenceUtils() {}

    public static Integer getWidgetRecipeId(Context context, int widgetId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(getWidgetPrefKey(widgetId), Constants.RECIPE_ID_EXTRA_DEFAULT);
    }

    public static void saveWidgetRecipeId(Context context, int widgetId, Integer recipeId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(getWidgetPrefKey(widgetId), recipeId);
        editor.apply();
    }

    private static String getWidgetPrefKey(int widgetId) {
        return Constants.WIDGET_RECIPE_ID_EXTRA + "." + widgetId;
    }
}
