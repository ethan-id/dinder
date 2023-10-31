package com.example.dinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dinder.R;
import com.example.dinder.adapters.FriendsAdapter;
import com.example.dinder.websocket.WebSocketListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.java_websocket.handshake.ServerHandshake;

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
    private View bottomNavigationView;

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.social);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId(); // Get the selected item's ID

            if (itemId == R.id.home) {
                // Start the UserHomeActivity
                Intent intent1 = new Intent(SocialActivity.this, UserHomeActivity.class);
                intent1.putExtra("id", id);
                startActivity(intent1);
                overridePendingTransition(0, 0); // No animation for this transition
                finish();
                return true;
            } else if (itemId == R.id.match) {
                // Start the MatchesScreen
                Intent intent1 = new Intent(SocialActivity.this, MatchesScreen.class);
                intent1.putExtra("id", id);
                startActivity(intent1);
                overridePendingTransition(0, 0); // No animation for this transition
                finish();
                return true;
            } else if (itemId == R.id.social) {
                // You're already on this page, so no need to do anything here.
                return true;
            } else if (itemId == R.id.userprofile) {
                // Start the UserProfileActivity
                Intent intent1 = new Intent(SocialActivity.this, UserProfileActivity.class);
                intent1.putExtra("id", id);
                startActivity(intent1);
                overridePendingTransition(0, 0); // No animation for this transition
                finish();
                return true;
            }

            return false;
        });

        searchBar = findViewById(R.id.search);
        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> friendsList = Arrays.asList("John", "Jane", "Doe", "Smith");
        FriendsAdapter adapter = new FriendsAdapter(friendsList);
        friendsRecyclerView.setAdapter(adapter);
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
