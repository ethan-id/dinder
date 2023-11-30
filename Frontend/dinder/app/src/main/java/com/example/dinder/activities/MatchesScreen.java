package com.example.dinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;
import com.example.dinder.activities.utils.NavigationUtils;
import com.example.dinder.websocket.WebSocketListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
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
    /** JSONObject representing the currently selected or displayed restaurant. */
    JSONObject currentRestaurant;

    /**
     * Initiates an HTTP request to load an image from a URL and sets it to the central ImageView of the restaurant.
     *
     * @param imageUrl the URL of the image to be loaded.
     * @param queue the RequestQueue used for sending the image request.
     */
    private void sendImageRequest(String imageUrl, RequestQueue queue) {
        queue.add(new ImageRequest(
                imageUrl,
                response -> {
                    runOnUiThread(() -> centerRestaurantImage.setImageBitmap(response));
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                Throwable::printStackTrace
        ));
    }

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
        ArrayList<String> codes = intent.getStringArrayListExtra("codes");

        if (codes.size() > 0) {
            try {
                Log.d("code", codes.get(0));
                getRestaurant(codes.get(0));
            } catch (Exception e) {
                Log.e("Error:", String.valueOf(e));
            }
        }

        centerRestaurantImage = findViewById(R.id.centerRestaurantImage);
        restName = findViewById(R.id.restName);
        address = findViewById(R.id.address);
        ratingIcon = findViewById(R.id.ratingIcon);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        NavigationUtils.setupBottomNavigation(bottomNavigationView, this, id, codes, username);
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
        String restaurantCode = message.substring(message.indexOf("@"));
        getRestaurant(restaurantCode);
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

    /**
     * Fetches the restaurant data from the server based on the given user ID.
     * <p>
     * This method sends a GET request to retrieve the restaurant's JSON data from the server using the provided code.
     * Upon a successful response, the restaurant's data is stored in the 'restaurant' variable. If there's an error in
     * the network request, the stack trace is printed.
     * </p>
     *
     * @param code The unique identifier of the restaurant to fetch data for.
     */
    private void getRestaurant(String code) {
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://coms-309-055.class.las.iastate.edu:8080/restaurant/find/" + code;
        queue.add(new JsonObjectRequest(
            Request.Method.GET, url, null,
            response -> {
                Log.d("restaurant", response.toString());
                currentRestaurant = response;
                try {
                    restName.setText(currentRestaurant.getString("name"));
                    JSONObject location = currentRestaurant.getJSONObject("location");
                    address.setText(location.getString("address1"));
                    sendImageRequest(currentRestaurant.getString("image_url"), queue);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            },
            Throwable::printStackTrace
        ));
    }
}