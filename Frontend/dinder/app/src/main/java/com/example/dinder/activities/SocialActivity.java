package com.example.dinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

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
import com.example.dinder.websocket.WebSocketListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Social Screen displays the user's friends list, allows the user to invite friends to their group, and allows
 * the user to send friend requests to other users.
 */
public class SocialActivity extends AppCompatActivity implements WebSocketListener {
    /**
     * Search bar used for the user to send friend requests
     */
    SearchView searchBar;
    /**
     * RecyclerView used to dynamically display the user's friends
     */
    RecyclerView friendsRecyclerView;
    RecyclerView incomingRequestsRecyclerView;
    /**
     * Private field representing the View displaying the bottom navigation menu on the screen
     */
    private BottomNavigationView bottomNavigationView;

    List<String> friendsList = new ArrayList<>();

    private void getUsersFriends(String id) {
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://coms-309-055.class.las.iastate.edu:8080/users/" + id;

        queue.add(new JsonObjectRequest(
            Request.Method.GET, url, null,
            response -> {
                try {
                    List<String> list = Arrays.asList(String.valueOf(response.getJSONArray("allFriends")));
                    for (String friend : list) {
                        friendsList.add(friend.substring(friend.indexOf("\"")+1, friend.lastIndexOf("\"")));
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

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        ArrayList<String> codes = intent.getStringArrayListExtra("codes");

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        NavigationUtils.setupBottomNavigation(bottomNavigationView, this, id, codes);
        bottomNavigationView.setSelectedItemId(R.id.social);

        searchBar = findViewById(R.id.search);

        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        incomingRequestsRecyclerView = findViewById(R.id.incomingRequestsRecyclerView);
        incomingRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<JSONObject> incoming = new ArrayList<>();
        JSONObject testIncoming = new JSONObject();
        try {
            testIncoming.put("creatorID", 21);
            testIncoming.put("creator", "Jesse");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        incoming.add(testIncoming);

        IncomingAdapter incAdapter = new IncomingAdapter(incoming);
        incomingRequestsRecyclerView.setAdapter(incAdapter);

        getUsersFriends(id);
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakeData) {

    }

    @Override
    public void onWebSocketMessage(String message) {

    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {

    }
}
