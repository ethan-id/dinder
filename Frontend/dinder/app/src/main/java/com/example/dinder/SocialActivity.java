package com.example.dinder;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.java_websocket.handshake.ServerHandshake;

import java.util.Arrays;
import java.util.List;

public class SocialActivity extends AppCompatActivity implements WebSocketListener {
    SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchBar = findViewById(R.id.search);

        RecyclerView friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
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
