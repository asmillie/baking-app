package com.example.android.bakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.bakingapp.Constants;

public class PreferenceUtils {

    private PreferenceUtils() {}

    /*
    Widget Methods for Recipe ID
     */
    public static Integer getWidgetRecipeId(Context context, int widgetId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(getRecipeIdWidgetPrefKey(widgetId), Constants.RECIPE_ID_EXTRA_DEFAULT);
    }

    public static void saveWidgetRecipeId(Context context, int widgetId, Integer recipeId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(getRecipeIdWidgetPrefKey(widgetId), recipeId);
        editor.apply();
    }

    public static void deleteWidgetRecipeId(Context context, int widgetId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(getRecipeIdWidgetPrefKey(widgetId));
        editor.apply();
    }

    private static String getRecipeIdWidgetPrefKey(int widgetId) {
        return Constants.WIDGET_RECIPE_ID_EXTRA + "." + widgetId;
    }

    /*
    Widget Methods for Recipe Name
     */

    public static String getRecipeNameByWidgetId(Context context, int widgetId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(getRecipeNameWidgetPrefKey(widgetId), "");
    }

    public static void saveWidgetRecipeName(Context context, int widgetId, String recipeName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getRecipeNameWidgetPrefKey(widgetId), recipeName);
        editor.apply();
    }

    public static void deleteWidgetRecipeName(Context context, int widgetId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(getRecipeNameWidgetPrefKey(widgetId));
        editor.apply();
    }

    private static String getRecipeNameWidgetPrefKey(int widgetId) {
        return Constants.WIDGET_RECIPE_NAME_EXTRA + "." + widgetId;
    }
}
