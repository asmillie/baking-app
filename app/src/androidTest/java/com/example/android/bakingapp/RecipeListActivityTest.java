package com.example.android.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.ui.RecipeListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/*
 * Resources used to learn and implement Espresso Tests
 * Counting Idling Resources:
 * https://medium.com/@wingoku/synchronizing-espresso-with-custom-threads-using-idling-resource-retrofit-70439ad2f07
 * Disabling Animations:
 * https://stackoverflow.com/questions/43751079/espresso-testing-disable-animation
 * Interacting with RecyclerView Child Views:
 * https://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item
 */
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    private IdlingRegistry mIdlingRegistry;
    private CountingIdlingResource mRepositoryIdlingResource;
    private CountingIdlingResource mActivityIdlingResource;

    private static final int RECIPE_POSITION = 1;
    private static final Integer RECIPE_ID = 2;

    private static final int INGREDIENT_POSITION = 0;
    private static final String INGREDIENT_STRING = "350.0 G Bittersweet chocolate (60-70% cacao)";

    @Rule
    public IntentsTestRule<RecipeListActivity> mActivityRule = new IntentsTestRule<>(
            RecipeListActivity.class
    );

    @Before
    public void setupIdlingResources() {
        mRepositoryIdlingResource = mActivityRule.getActivity().getRepositoryCountingIdlingResource();
        mActivityIdlingResource = mActivityRule.getActivity().getActivityCountingIdlingResource();

        mIdlingRegistry = IdlingRegistry.getInstance();
        mIdlingRegistry.register(mRepositoryIdlingResource);
        mIdlingRegistry.register(mActivityIdlingResource);
    }

    @Test
    public void selectRecipe_SendsCorrectIntent() {
        onView(withId(R.id.recipe_list_rv))
            .perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_POSITION, TestUtils.clickChildViewById(R.id.view_recipe_btn)));

        intended(hasExtra(Constants.RECIPE_ID_EXTRA, RECIPE_ID));
    }

    @After
    public void unregisterIdlingResources() {
        mIdlingRegistry.unregister(mRepositoryIdlingResource);
        mIdlingRegistry.unregister(mActivityIdlingResource);
    }
}
