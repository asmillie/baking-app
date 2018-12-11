package com.example.android.bakingapp;

public class Constants {

    //Prevent instantiation
    private Constants() {}

    /** App Paths **/
    private static final String APP_BASE_URI = "com.example.android.bakingapp";

    /** Base Url for Recipe List **/
    public static final String RECIPE_LIST_JSON_PATH = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    /** Extra for storing Recipe IDs **/
    public static final String RECIPE_ID_EXTRA = "recipe-id";
    public static final Integer RECIPE_ID_EXTRA_DEFAULT = -1;

    /** Extra for storing Recipe Step IDs **/
    public static final String RECIPE_STEP_ID_EXTRA = "recipe-step-id";
    public static final Integer RECIPE_STEP_ID_DEFAULT = -1;

    /** Default step Id **/
    public static final Integer SELECTED_STEP_ID_DEFAULT = 0;

    /** Widget Extras **/
    static final String WIDGET_SELECT_RECIPE_ACTION = APP_BASE_URI + ".SELECT_RECIPE_ACTION";
    public static final String WIDGET_RECIPE_ID_EXTRA = APP_BASE_URI + ".RECIPE_ID";
}
