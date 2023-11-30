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

import com.android.volley.RequestQueue;
import com.example.dinder.activities.LoginActivity;
import com.example.dinder.activities.RestaurantProfileActivity;
import com.example.dinder.activities.UserHomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RestaurantProfileActivityTest {
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
        loginScenario.getScenario().onActivity(Activity::finish);
    }

    @Rule
    public ActivityScenarioRule<LoginActivity> loginScenario = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testNavigateToRestaurantProfile() {
        // Enter valid username and password
        onView(withId(R.id.editTextUsername)).perform(typeText("MrEthan"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("johndeere"), closeSoftKeyboard());

        // Click on the login button
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(5000); // Wait for getRestaurants()
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click on the center image to navigate to the restaurant profile screen
        onView(withId(R.id.centerRestaurantImage)).perform(click());

        try {
            Thread.sleep(1000); // Wait for the screen transition
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(RestaurantProfileActivity.class.getName()));
    }
}
