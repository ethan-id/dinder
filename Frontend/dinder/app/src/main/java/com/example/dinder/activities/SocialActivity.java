package com.example.dinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import java.util.List;

/**
 * The Social Screen displays the user's friends list, allows the user to invite friends to their group, and allows
 * the user to send friend requests to other users.
 */
public class SocialActivity extends AppCompatActivity implements IncomingAdapter.AdapterCallback, FriendsAdapter.AdapterCallback {
    /**
     * RecyclerView used to dynamically display the user's friends
     */
    RecyclerView friendsRecyclerView, incomingRequestsRecyclerView, groupRequestsRecyclerView;
    EditText usernameInput;
    ImageView sendRequestButton;
    TextView friendRequestsHeader;
    TextView friendsHeader;
    TextView groupsHeader;
    /**
     * Private field representing the View displaying the bottom navigation menu on the screen
     */
    private BottomNavigationView bottomNavigationView;
    List<String> friendsList = new ArrayList<>();
    List<JSONObject> incoming = new ArrayList<>();
    List<JSONObject> groupReqs = new ArrayList<>();

    public void updateHeaders() {
        runOnUiThread(() -> {
            if (incoming.isEmpty()) {
                friendRequestsHeader.setVisibility(View.GONE);
            } else {
                friendRequestsHeader.setVisibility(View.VISIBLE);
            }
            if (friendsList.isEmpty()) {
                friendsHeader.setVisibility(View.GONE);
            } else {
                friendsHeader.setVisibility(View.VISIBLE);
            }
            if (groupReqs.isEmpty()) {
                groupsHeader.setVisibility(View.GONE);
            } else {
                groupsHeader.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getUsersFriendsAndRequests(String id, String username) {
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://coms-309-055.class.las.iastate.edu:8080/users/" + id;

        queue.add(new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray requestsArray = response.getJSONArray("requests");
                        for (int i = 0; i < requestsArray.length(); i++) {
                            JSONObject requestObject = requestsArray.getJSONObject(i);
                            String type = requestObject.getString("parameter");
                            if (type.equals("friend")) incoming.add(requestObject); // Add each JSONObject to the incoming list
                            if (type.equals("group")) groupReqs.add(requestObject); // Add each JSONObject to the groupReqs list
                        }
                        updateHeaders();

                        // Set the adapter for the RecyclerView with the updated incoming list
                        IncomingAdapter incAdapter = new IncomingAdapter(incoming, this.getApplicationContext(), this);
                        incomingRequestsRecyclerView.setAdapter(incAdapter);

                        // Set the adapter for the RecyclerView with the updated incoming list
                        IncomingAdapter groupAdapter = new IncomingAdapter(groupReqs, this.getApplicationContext(), this);
                        groupRequestsRecyclerView.setAdapter(groupAdapter);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        JSONArray iter = response.getJSONArray("allFriends");
                        for (int i = 0; i < iter.length(); i++) {
                            friendsList.add(iter.get(i).toString());
                        }
                        updateHeaders();

                        FriendsAdapter adapter = new FriendsAdapter(friendsList, this.getApplicationContext(), username, this);
                        friendsRecyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                Throwable::printStackTrace
        ));
    }

    private void sendFriendRequest(String username, String friend) {
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://coms-309-055.class.las.iastate.edu:8080/request/create/" + username + "/friend/" + friend;

        queue.add(new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Friend Request: ", response);
                    Toast.makeText(this, "Friend request sent", Toast.LENGTH_SHORT).show();
                }, Throwable::printStackTrace));
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
        friendRequestsHeader = findViewById(R.id.friendRequestsHeader);
        friendsHeader = findViewById(R.id.friendsHeader);
        groupsHeader = findViewById(R.id.groupRequestsHeader);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String username = intent.getStringExtra("username");
        Boolean plus = intent.getBooleanExtra("plus", false);
        Boolean isAdmin = intent.getBooleanExtra("isAdmin", false);
        ArrayList<String> codes = intent.getStringArrayListExtra("codes");

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        NavigationUtils.setupBottomNavigation(bottomNavigationView, this, id, codes, username, plus, isAdmin);
        bottomNavigationView.setSelectedItemId(R.id.social);

        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        incomingRequestsRecyclerView = findViewById(R.id.incomingRequestsRecyclerView);
        incomingRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        groupRequestsRecyclerView = findViewById(R.id.incomingGroupRequestsRecyclerView);
        groupRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        sendRequestButton.setOnClickListener(v -> {
            String friendToAdd = usernameInput.getText().toString();
            sendFriendRequest(username, friendToAdd);
            usernameInput.setText("");
        });

        getUsersFriendsAndRequests(id, username);
    }

    @Override
    public void onListChanged() {
        updateHeaders();
    }

    @Override
    public void onFriendsListChanged() {
        updateHeaders();
    }
}
