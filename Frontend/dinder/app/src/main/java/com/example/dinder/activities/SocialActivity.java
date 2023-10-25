package com.example.dinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dinder.R;
import com.example.dinder.adapters.FriendsAdapter;
import com.example.dinder.websocket.WebSocketListener;

import org.java_websocket.handshake.ServerHandshake;

import java.util.Arrays;
import java.util.List;

public class SocialActivity extends AppCompatActivity implements WebSocketListener {
    SearchView searchBar;
    ImageButton back;
    RecyclerView friendsRecyclerView;

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
     * previously being shut down, this Bundle contains the data it most recently
     * supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        searchBar = findViewById(R.id.search);
        back = findViewById(R.id.backBtn);

        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> friendsList = Arrays.asList("John", "Jane", "Doe", "Smith");
        FriendsAdapter adapter = new FriendsAdapter(friendsList);
        friendsRecyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        back.setOnClickListener(v -> {
            Intent homeScreen = new Intent(SocialActivity.this, UserHomeActivity.class);
            homeScreen.putExtra("id", id);
            startActivity(homeScreen);
        });
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
