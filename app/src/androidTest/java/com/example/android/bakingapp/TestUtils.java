package com.example.android.bakingapp;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

/*
 * Solution to custom View Action implemented using resource
 * @ https://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item
 */
public class TestUtils {

    private TestUtils() {}

    public static ViewAction clickChildViewById(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on child view by id";
            }

            @Override
            public void perform(UiController uiController, View parentView) {
                View childView = parentView.findViewById(id);
                childView.performClick();
            }
        };
    }
}
