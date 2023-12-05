package com.example.dinder;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
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
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android.volley.RequestQueue;
import com.example.dinder.activities.AdminHomeActivity;
import com.example.dinder.activities.LoginActivity;
import com.example.dinder.activities.UserHomeActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AdminHomeActivityTest {
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
        scenario.getScenario().onActivity(Activity::finish);
    }

    @Rule
    public ActivityScenarioRule<AdminHomeActivity> scenario = new ActivityScenarioRule<>(AdminHomeActivity.class);

    @Test
    public void testAdminDashboard() {
        awaitTransition(1000);

        onView(withId(R.id.tvTotalLikes)).check(matches(isDisplayed()));
        onView(withId(R.id.tvTotalUsers)).check(matches(isDisplayed()));
        onView(withId(R.id.tvTotalFavorites)).check(matches(isDisplayed()));
        onView(withId(R.id.tvAvgSwipesBeforeMatch)).check(matches(isDisplayed()));
        onView(withId(R.id.tvAvgLikesPerMatch)).check(matches(isDisplayed()));
        onView(withId(R.id.tvAvgSwipesBeforeLike)).check(matches(isDisplayed()));
        onView(withId(R.id.tvAvgLikesPerUser)).check(matches(isDisplayed()));
        onView(withId(R.id.tvAvgMatchesPerUser)).check(matches(isDisplayed()));
    }
}
