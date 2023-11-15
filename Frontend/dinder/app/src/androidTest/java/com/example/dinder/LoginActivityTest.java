package com.example.dinder;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android.volley.RequestQueue;
import com.example.dinder.activities.LoginActivity;
import com.example.dinder.activities.UserHomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {w
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
    }

    @Rule
    public ActivityScenarioRule<LoginActivity> loginScenario = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testLoginSuccess() {
        // Enter valid username and password
        onView(withId(R.id.editTextUsername)).perform(typeText("MrEthan"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("johndeere"), closeSoftKeyboard());

        // Click on the login button
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(1000); // Wait for the screen transition to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the UserHomeActivity is started
        intended(hasComponent(UserHomeActivity.class.getName()));
    }

    @Test
    public void testLoginFailure() {
        // Enter invalid username and password
        onView(withId(R.id.editTextUsername)).perform(typeText("MrEthan"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("notjohndeere"), closeSoftKeyboard());

        // Click on the login button
        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(1000); // Wait for the fade-in animation to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the error notification is displayed
        onView(withId(R.id.notification_layout)).check(matches(isDisplayed()));
    }
}
