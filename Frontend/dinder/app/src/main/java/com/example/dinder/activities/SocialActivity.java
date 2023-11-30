package com.example.dinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;
import com.example.dinder.activities.utils.NavigationUtils;
import com.example.dinder.adapters.FriendsAdapter;
import com.example.dinder.adapters.IncomingAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Social Screen displays the user's friends list, allows the user to invite friends to their group, and allows
 * the user to send friend requests to other users.
 */
public class SocialActivity extends AppCompatActivity {
    /**
     * RecyclerView used to dynamically display the user's friends
     */
    RecyclerView friendsRecyclerView;
    RecyclerView incomingRequestsRecyclerView;

    EditText usernameInput;
    ImageView sendRequestButton;
    /**
     * Private field representing the View displaying the bottom navigation menu on the screen
     */
    private BottomNavigationView bottomNavigationView;

    List<String> friendsList = new ArrayList<>();
    List<JSONObject> incoming = new ArrayList<>();

    private void getUsersFriendsAndRequests(String id) {
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://coms-309-055.class.las.iastate.edu:8080/users/" + id;

        queue.add(new JsonObjectRequest(
            Request.Method.GET, url, null,
            response -> {
                try {
                    JSONArray requestsArray = response.getJSONArray("requests");
                    for (int i = 0; i < requestsArray.length(); i++) {
                        JSONObject requestObject = requestsArray.getJSONObject(i);
                        incoming.add(requestObject); // Add each JSONObject to the incoming list
                    }

                    // Set the adapter for the RecyclerView with the updated incoming list
                    IncomingAdapter incAdapter = new IncomingAdapter(incoming, this.getApplicationContext());
                    incomingRequestsRecyclerView.setAdapter(incAdapter);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    JSONArray iter = response.getJSONArray("allFriends");
                    for (int i = 0; i < iter.length(); i++) {
                        friendsList.add(iter.get(i).toString());
                    }

                    FriendsAdapter adapter = new FriendsAdapter(friendsList);
                    friendsRecyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            },
            Throwable::printStackTrace
        ));
    }

    /**
     * Initializes the SocialActivity. This method:
     * <ul>
     *     <li>Sets the content view layout.</li>
     *     <li>Initializes UI components including the search bar and friends' RecyclerView.</li>
     *     <li>Populates the friends' RecyclerView with a static list of friend names.</li>
     *     <li>Retrieves user ID from the intent.</li>
     *     <li>Handles the back button click event, which navigates to the UserHomeActivity and passes the user ID to it.</li>
     * </ul>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this Bundle contains the data it most recently
     *                           supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        usernameInput = findViewById(R.id.usernameInput);
        sendRequestButton = findViewById(R.id.sendRequestButton);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        ArrayList<String> codes = intent.getStringArrayListExtra("codes");

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        NavigationUtils.setupBottomNavigation(bottomNavigationView, this, id, codes);
        bottomNavigationView.setSelectedItemId(R.id.social);

        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        incomingRequestsRecyclerView = findViewById(R.id.incomingRequestsRecyclerView);
        incomingRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        sendRequestButton.setOnClickListener(v -> {
            String friendToAdd = usernameInput.getText().toString();
            // send friend request...
            usernameInput.setText("");
        });

        getUsersFriendsAndRequests(id);
    }
}
