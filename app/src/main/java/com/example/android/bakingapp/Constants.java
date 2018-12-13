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

    /** Recipe Step Video **/
    public static final String PLAYER_POSITION_EXTRA = "video-position";

    /** Widget Extras **/
    private static final String WIDGET_ACTION_PREFIX = ".WIDGET_ACTION.";
    private static final String WIDGET_EXTRA_PREFIX = ".WIDGET_EXTRA.";

    public static final String WIDGET_GET_RECIPES_ACTION = WIDGET_ACTION_PREFIX + "GET_RECIPES";
    public static final String WIDGET_GET_INGREDIENTS_ACTION = WIDGET_ACTION_PREFIX + "GET_INGREDIENTS";

    public static final String WIDGET_RECIPE_ID_EXTRA = WIDGET_EXTRA_PREFIX + "RECIPE_ID";
    public static final String WIDGET_RECIPE_NAME_EXTRA = WIDGET_EXTRA_PREFIX + "RECIPE_NAME";
}
