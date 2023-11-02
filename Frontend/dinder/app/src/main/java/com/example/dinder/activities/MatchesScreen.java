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
    ImageView centerRestaurantImage;
    TextView restName;
    TextView address;
    ImageView ratingIcon;
    ArrayList<JSONObject> restaurants = new ArrayList<>();
    JSONObject currentRestaurant;

    private void sendImageRequest(String imageUrl, RequestQueue queue) {
        queue.add(new ImageRequest(
                imageUrl,
                response -> {
                    runOnUiThread(() -> centerRestaurantImage.setImageBitmap(response));
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                Throwable::printStackTrace
        ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_screen);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        ArrayList<String> codes = intent.getStringArrayListExtra("codes");

        if (codes.size() > 0) {
            Log.d("code", codes.get(0));
            getRestaurant(codes.get(0));
        }

        centerRestaurantImage = findViewById(R.id.centerRestaurantImage);
        restName = findViewById(R.id.restName);
        address = findViewById(R.id.address);
        ratingIcon = findViewById(R.id.ratingIcon);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.match);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                // Start the UserHomeActivity without animation
                Intent intent1 = new Intent(MatchesScreen.this, UserHomeActivity.class);
                intent1.putExtra("id", id);
                intent1.putExtra("connected", true);
                intent1.putStringArrayListExtra("codes", codes);
                startActivity(intent1);
                overridePendingTransition(0, 0); // No animation for this transition
                finish();
                return true;
            } else if (itemId == R.id.match) {
                // You're already on this page, so no need to do anything here.
                return true;
            } else if (itemId == R.id.social) {
                // Start the SocialActivity without animation
                Intent intent1 = new Intent(MatchesScreen.this, SocialActivity.class);
                intent1.putExtra("id", id);
                startActivity(intent1);
                overridePendingTransition(0, 0); // No animation for this transition
                finish();
                return true;
            } else if (itemId == R.id.userprofile) {
                // Start the UserProfileActivity without animation
                Intent intent1 = new Intent(MatchesScreen.this, UserProfileActivity.class);
                intent1.putExtra("id", id);
                startActivity(intent1);
                overridePendingTransition(0, 0); // No animation for this transition
                finish();
                return true;
            }

            return false;
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakeData) {
        // WebSocket connection has been opened
    }

    @Override
    public void onWebSocketMessage(String message) {
        // Handle WebSocket messages here
        String restaurantCode = message.substring(message.indexOf("@"));
        getRestaurant(restaurantCode);
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // WebSocket connection has been closed
    }

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