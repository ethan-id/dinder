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
import com.example.dinder.activities.SignUpActivity;
import com.example.dinder.activities.UserHomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SignupActivityTest {
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
    public void testNavigateToSignUpScreen() {
        // Click on the sign-up button to go to the sign-up screen
        onView(withId(R.id.mainSignUpBtn)).perform(click());

        try {
            Thread.sleep(500); // Wait for the screen transition to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the SignUpActivity has started
        intended(hasComponent(SignUpActivity.class.getName()));
    }

    @Test
    public void testSigningUp() {
        // Click on the sign-up button to go to the sign-up screen
        onView(withId(R.id.mainSignUpBtn)).perform(click());

        try {
            Thread.sleep(500); // Wait for the screen transition to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Fill out sign-up screen with user info
        onView(withId(R.id.editTextEmail)).perform(typeText("John"), closeSoftKeyboard());
        onView(withId(R.id.editTextUsername)).perform(typeText("John123"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.termsCheckbox)).perform(click());
        onView(withId(R.id.dataConsentCheckbox)).perform(click());

        // Click sign-up button
        onView(withId(R.id.signUpBtn)).perform(click());

        try {
            Thread.sleep(500); // Wait for the screen transition to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Assert we've been taken back to the login screen
        intended(hasComponent(LoginActivity.class.getName()));
    }
}
