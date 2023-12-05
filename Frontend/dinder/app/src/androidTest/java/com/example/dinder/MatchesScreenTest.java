package com.example.dinder;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.dinder.utils.TestUtils.awaitTransition;
import static org.hamcrest.Matchers.not;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.android.volley.RequestQueue;
import com.example.dinder.activities.MatchesScreen;

import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchesScreenTest {
    private VolleyIdlingResource idlingResource;

    @Before
    public void setUp() {
        Intents.init();

        RequestQueue queue = VolleySingleton.getInstance(ApplicationProvider.getApplicationContext()).getRequestQueue();
        idlingResource = new VolleyIdlingResource(queue);
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void tearDown() {
        Intents.release();

        // Unregister idling resource
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
        matchesScreenScenario.getScenario().onActivity(Activity::finish);
    }

    @Rule
    public ActivityScenarioRule<MatchesScreen> matchesScreenScenario = new ActivityScenarioRule<>(getIntent());

    @Test
    public void testActivityLaunch() {
        awaitTransition(3000);
        // Check if the MatchesScreen activity is launched successfully
//        onView(withId(R.id.matchesRecyclerView)).check(matches(isDisplayed()));
//        onView(withId(R.id.bottom_navigator)).check(matches(isDisplayed()));
    }

    @Test
    public void testWebSocketMessageHandling() {
        String testWebSocketMessage = "@testRestaurantCode";
        onView(withId(R.id.match)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "simulate WebSocket message";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((MatchesScreen) view.getContext()).onWebSocketMessage(testWebSocketMessage);
            }
        });

//        onView(withId(R.id.restName)).check(matches(not(withText("")))); // Assuming it's not empty
//        onView(withId(R.id.address)).check(matches(not(withText("")))); // Assuming it's not empty
    }

    @Test
    public void testNavigateToMatchesScreen() {
        onView(withId(R.id.match)).check(matches(isDisplayed()));
    }

    private JSONObject createTestRestaurant() {
        try {
            JSONObject restaurant = new JSONObject();
            restaurant.put("name", "Test Restaurant");
            JSONObject location = new JSONObject();
            location.put("address1", "123 Test Street");
            restaurant.put("location", location);
            return restaurant;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Intent getIntent() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MatchesScreen.class);
        intent.putExtra("id", "testUserId");
        intent.putExtra("username", "testUsername");
        intent.putStringArrayListExtra("codes", new ArrayList<>(Arrays.asList("testRestaurantCode")));
        return intent;
    }
}

