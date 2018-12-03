package com.example.android.bakingapp;

public class Constants {

    //Prevent instantiation
    private Constants() {}

    public static final String RECIPE_LIST_JSON_PATH = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    /** Extra for storing Recipe IDs **/
    public static final String RECIPE_ID_EXTRA = "recipe-id";
    public static final Integer RECIPE_ID_EXTRA_DEFAULT = -1;

    /** Extra for storing Recipe Step IDs **/
    public static final String RECIPE_STEP_ID_EXTRA = "recipe-step-id";
    public static final Integer RECIPE_STEP_ID_DEFAULT = -1;

    /** Extra for bundling Recipe Step **/
    public static final String RECIPE_STEP_BUNDLE_EXTRA = "recipe-step";
}
