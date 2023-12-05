package com.example.dinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;
import com.example.dinder.activities.utils.NavigationUtils;
import com.example.dinder.adapters.MatchAdapter;
import com.example.dinder.websocket.WebSocketListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The screen used to display the restaurants the user has been matched with
 */
public class MatchesScreen extends AppCompatActivity implements WebSocketListener {
    /** ImageView to display the main restaurant image at the center of the screen. */
    ImageView centerRestaurantImage;
    /** TextView for displaying the name of the restaurant. */
    TextView restName;
    /** TextView for displaying the address of the restaurant. */
    TextView address;
    /** ImageView for displaying the rating icon of the restaurant. */
    ImageView ratingIcon;
    /** ArrayList holding JSONObjects, each representing a restaurant's data. */
    ArrayList<JSONObject> restaurants = new ArrayList<>();
    RecyclerView matchesRecyclerView;
    private MatchAdapter matchAdapter;


    /**
     * Called when the activity is starting. This is where most initialization should go:
     * calling {@code setContentView(int)} to inflate the activity's UI, obtaining references
     * to widgets in the UI, and attaching event handlers.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState.
     *                           <b>Note: Otherwise it is null.</b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_screen);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String username = intent.getStringExtra("username");
        Boolean plus = intent.getBooleanExtra("plus", false);
        Boolean isAdmin = intent.getBooleanExtra("isAdmin", false);
        ArrayList<String> codes = intent.getStringArrayListExtra("codes");

        matchesRecyclerView = findViewById(R.id.matchesRecyclerView);
        matchesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getRestaurants(codes);
        matchAdapter = new MatchAdapter(restaurants,this.getApplicationContext());
        matchesRecyclerView.setAdapter(matchAdapter);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        NavigationUtils.setupBottomNavigation(bottomNavigationView, this, id, codes, username, plus, isAdmin);
        bottomNavigationView.setSelectedItemId(R.id.match);
    }

    /**
     * Callback method to be invoked when the WebSocket connection is established and opened.
     *
     * @param handshakeData The handshake data from the server.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakeData) {
        // WebSocket connection has been opened
    }


    /**
     * Callback method to be invoked when a message has been received from the WebSocket server.
     *
     * @param message The string message received from the server.
     */
    @Override
    public void onWebSocketMessage(String message) {
        // Handle WebSocket messages here
    }

    /**
     * Callback method to be invoked when the WebSocket connection has been closed.
     *
     * @param code The status code indicating the reason for the closure.
     * @param reason The string message which represents additional information about the closure.
     * @param remote Returns true if the closure was initiated by the remote peer.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // WebSocket connection has been closed
    }

    /**
     * Callback method to be invoked when an error occurred on the WebSocket connection.
     *
     * @param ex The exception representing the error that occurred.
     */
    @Override
    public void onWebSocketError(Exception ex) {
        // Handle WebSocket errors
    }

    private void getRestaurants(ArrayList<String> codes) {
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        for (String code : codes) {
            String url = "http://coms-309-055.class.las.iastate.edu:8080/restaurant/find/" + code;
            queue.add(new JsonObjectRequest(
                    Request.Method.GET, url, null,
                    response -> {
                        Log.d("restaurant", response.toString());
                        // Update the adapter's data and notify the adapter of the change
                        restaurants.add(response);
                        matchAdapter.notifyItemInserted(restaurants.size() - 1); // Notify that an item is inserted at the end of the list
                    },
                    Throwable::printStackTrace
            ));
        }
    }
}