package com.example.android.bakingapp;

import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.ui.RecipeInstructionsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.selectedDescendantsMatch;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/*
 * Resources used to learn and implement Intent Testing
 * @ http://www.vogella.com/tutorials/AndroidTestingEspresso/article.html#espresso_usageintroduction_viewaction
 */
@RunWith(AndroidJUnit4.class)
public class InstructionsActivityTest {

    private final Integer RECIPE_ID = 1;
    private final int RECIPE_STEP_POSITION = 2;
    private final String RECIPE_STEP_TEXT = "Melt butter and bittersweet chocolate.";

    private IdlingRegistry mIdlingRegistry;
    private CountingIdlingResource mRepositoryIdlingResource;

    public IntentsTestRule<RecipeInstructionsActivity> mActivityRule =
            new IntentsTestRule<>(RecipeInstructionsActivity.class, true, false);

    @Before
    public void startInstructionActivity() {
        Intent activityIntent = new Intent();
        activityIntent.putExtra(Constants.RECIPE_ID_EXTRA, RECIPE_ID);
        mActivityRule.launchActivity(activityIntent);
    }

    @Before
    public void initIdlingResources() {
        mRepositoryIdlingResource = mActivityRule.getActivity().getRepositoryCountingIdlingResource();

        mIdlingRegistry = IdlingRegistry.getInstance();
        mIdlingRegistry.register(mRepositoryIdlingResource);
    }

    @Test
    public void checkStepText() {
        onView(withId(R.id.steps_rv))
                .perform(RecyclerViewActions.scrollToPosition(RECIPE_STEP_POSITION))
                .check(selectedDescendantsMatch(withId(R.id.step_description), withText(RECIPE_STEP_TEXT)));
    }

    @After
    public void releaseIdlingResources() {
        if (mRepositoryIdlingResource != null) {
            mIdlingRegistry.unregister(mRepositoryIdlingResource);
        }
    }
}
