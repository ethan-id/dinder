package com.example.dinder;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android.volley.RequestQueue;
import com.example.dinder.activities.LoginActivity;
import com.example.dinder.activities.UserHomeActivity;
import com.example.dinder.activities.UserProfileActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserProfileActivityTest {
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
    public void testNavigatingToUserProfile() {
        try {
            Thread.sleep(4000); // Wait for the screen transition to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click on the user profile menu option
        onView(withId(R.id.userprofile)).perform(click());

        try {
            Thread.sleep(1000); // Wait for the screen transition to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the UserHomeActivity is started
        intended(hasComponent(UserProfileActivity.class.getName()));
    }

    @Test
    public void testCheckingADietaryRestriction() {
        try {
            Thread.sleep(4000); // Wait for the screen transition to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click on the user profile menu option
        onView(withId(R.id.userprofile)).perform(click());

        try {
            Thread.sleep(1000); // Wait for the screen transition to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.vegetarianCheck)).perform(click());
        onView(withId(R.id.veganCheck)).perform(click());
        onView(withId(R.id.halalCheck)).perform(click());
        onView(withId(R.id.saveBtn)).perform(click());
    }
}
