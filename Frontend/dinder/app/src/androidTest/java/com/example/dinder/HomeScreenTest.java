package com.example.dinder;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.dinder.utils.TestUtils.awaitTransition;
import static org.hamcrest.CoreMatchers.not;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.android.volley.RequestQueue;
import com.example.dinder.activities.UserHomeActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HomeScreenTest {
    private VolleyIdlingResource idlingResource;

    @Before
    public void setUp() {
        // Initialize Espresso Intents before each test
        Intents.init();

        // Register Idling Resource
        RequestQueue queue = VolleySingleton.getInstance(ApplicationProvider.getApplicationContext()).getRequestQueue();
        idlingResource = new VolleyIdlingResource(queue);
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void tearDown() {
        // Release Espresso Intents after each test
        Intents.release();

        // Unregister idling resource
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
        homeScreenScenario.getScenario().onActivity(Activity::finish);
    }

    @Rule
    public ActivityScenarioRule<UserHomeActivity> homeScreenScenario = new ActivityScenarioRule<>(UserHomeActivity.class);

    @Test
    public void testLikingRestaurantWithButtonUpdatesScreen() {
        awaitTransition(5000); // Wait for getAllRestaurants()

        // Capture initial text
        final String[] initialText = new String[1];
        onView(withId(R.id.address)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "capture text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = (TextView) view;
                initialText[0] = textView.getText().toString();
            }
        });

        // Perform the action that changes the text
        onView(withId(R.id.heartBtn)).perform(click());

        awaitTransition(3500);

        // Assert that the text has changed
        onView(withId(R.id.address)).check(matches(not(withText(initialText[0]))));
    }

    @Test
    public void testLikingBySwipingRestaurantWithButtonUpdatesScreen() {
        awaitTransition(3500); // Wait for getAllRestaurants()

        // Capture initial text
        final String[] initialText = new String[1];
        onView(withId(R.id.address)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "capture text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = (TextView) view;
                initialText[0] = textView.getText().toString();
            }
        });

        // Perform the action that changes the text
        onView(withId(R.id.centerRestaurantImage)).perform(swipeRight());

        awaitTransition(3500);

        // Assert that the text has changed
        onView(withId(R.id.address)).check(matches(not(withText(initialText[0]))));
    }

    @Test
    public void testDislikingRestaurantWithButtonUpdatesScreen() {
        awaitTransition(3500); // Wait for getAllRestaurants()

        // Capture initial text
        final String[] initialText = new String[1];
        onView(withId(R.id.address)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "capture text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = (TextView) view;
                initialText[0] = textView.getText().toString();
            }
        });

        // Perform the action that changes the text
        onView(withId(R.id.dislikeBtn)).perform(click());

        awaitTransition(3500);

        // Assert that the text has changed
        onView(withId(R.id.address)).check(matches(not(withText(initialText[0]))));
    }
}
