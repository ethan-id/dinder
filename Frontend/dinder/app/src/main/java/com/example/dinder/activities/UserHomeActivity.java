package com.example.dinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.ImageRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;
import com.example.dinder.websocket.WebSocketListener;
import com.example.dinder.websocket.WebSocketManager;
import com.google.android.material.chip.Chip;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class UserHomeActivity extends AppCompatActivity implements WebSocketListener {

    ImageView centerImage;
    ImageView logo;
    ImageView locationIcon;
    ImageView ratingIcon;
    TextView restaurantName;
    TextView rating;
    TextView ratingCount;
    TextView address;
    ImageButton dislike;
    ImageButton favorite;
    ImageButton profile;
    Chip chip1;
    Chip chip2;
    Chip chip3;
    Chip chip4;
    Chip chip5;
    Chip chip6;
    Chip chip7;

    private GestureDetector gestureDetector;
    ArrayList<JSONObject> restaurants = new ArrayList<>();
    JSONObject currentRestaurant;

    /**
     * Sets the text content for a specific chip based on its index and ensures its visibility.
     * The method takes in an index corresponding to a chip position and a tag to set as the
     * chip's text content. UI operations are executed on the main thread to ensure
     * that the user interface updates smoothly.
     *
     * @param index the position of the chip to update, starting from 0
     * @param tag the text content to set for the specified chip
     */
    private void setChip(int index, String tag) {
        switch(index) {
            case 0:
                runOnUiThread(() -> {
                    chip2.setText(tag);
                    chip2.setVisibility(View.VISIBLE);
                });
                break;
            case 1:
                runOnUiThread(() -> {
                    chip3.setText(tag);
                    chip3.setVisibility(View.VISIBLE);
                });
                break;
            case 2:
                runOnUiThread(() -> {
                    chip4.setText(tag);
                    chip4.setVisibility(View.VISIBLE);
                });
                break;
            case 3:
                runOnUiThread(() -> {
                    chip5.setText(tag);
                    chip5.setVisibility(View.VISIBLE);
                });
                break;
            case 4:
                runOnUiThread(() -> {
                    chip6.setText(tag);
                    chip6.setVisibility(View.VISIBLE);
                });
                break;
            case 5:
                runOnUiThread(() -> {
                    chip7.setText(tag);
                    chip7.setVisibility(View.VISIBLE);
                });
                break;
        }
    }

    /**
     * Checks each chip's text content and hides those that are empty. This method ensures
     * that only chips with valid content are displayed to the user. UI operations are
     * performed on the main thread to ensure smooth user experience.
     */

    private void hideEmptyChips() {
        runOnUiThread(() -> {
            if (chip1.getText().toString().equals("")) {
                chip1.setVisibility(View.GONE);
            }
            if (chip2.getText().toString().equals("")) {
                chip2.setVisibility(View.GONE);
            }
            if (chip3.getText().toString().equals("")) {
                chip3.setVisibility(View.GONE);
            }
            if (chip4.getText().toString().equals("")) {
                chip4.setVisibility(View.GONE);
            }
            if (chip5.getText().toString().equals("")) {
                chip5.setVisibility(View.GONE);
            }
            if (chip6.getText().toString().equals("")) {
                chip6.setVisibility(View.GONE);
            }
            if (chip7.getText().toString().equals("")) {
                chip7.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Populates the user interface with information about a restaurant at a specified index
     * from the `restaurants` list. This method fetches the restaurant data, sets the UI elements
     * with the appropriate values, and handles any JSON exceptions that might arise. If there
     * are no restaurants at the given index, the user is informed that there are no more restaurants.
     *
     * @param queue The request queue to which any necessary requests (e.g., image fetching) will be added.
     * @param index The index of the restaurant in the `restaurants` list that should be displayed on the screen.
     */
    private void populateScreen(RequestQueue queue, int index) {
        if (index < restaurants.size()) {
            currentRestaurant = restaurants.get(index);
            try {
                // Populate screen with info about current restaurant
                sendCenterImageRequest(currentRestaurant.getString("_url"), queue);
                restaurantName.setText(currentRestaurant.getString("_name"));
                address.setText(currentRestaurant.getString("_address"));
                chip1.setText(currentRestaurant.getString("_price"));
                rating.setText(currentRestaurant.getString("_rating"));
                ratingCount.setText(String.format("(%s)", currentRestaurant.getString("_review_count")));
                runOnUiThread(() -> {
                    try {
                        restaurantName.setText(currentRestaurant.getString("_name"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        address.setText(currentRestaurant.getString("_address"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        chip1.setText(currentRestaurant.getString("_price"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        rating.setText(currentRestaurant.getString("_rating"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        ratingCount.setText(String.format("(%s)", currentRestaurant.getString("_review_count")));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
                JSONArray titles = currentRestaurant.getJSONArray("_titles");
                for (int i = 0; i < titles.length(); i++) {
                    setChip(i, titles.get(i).toString());
                }
                hideEmptyChips();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Tell user there are no more restaurants :(
        }
    }

    /**
     * Fetches the list of restaurants from the server and populates the `restaurants` list.
     * If the list is not empty, it triggers the `populateScreen` method to display the content.
     *
     * @param queue The request queue to which the JSON array request will be added.
     */
    private void getRestaurants(RequestQueue queue) {
        queue.add(new JsonArrayRequest(
            Request.Method.GET,
            "http://10.0.2.2:8080/restaurant/all",
            null,
            (Response.Listener<JSONArray>) response -> {
                // Handle the JSON array response
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject restaurantObject = response.getJSONObject(i);
                        restaurants.add(restaurantObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (!restaurants.isEmpty()) {
                    populateScreen(queue, 0);
                }
            },
            (Response.ErrorListener) Throwable::printStackTrace
        ));
    }

    /**
     * Sends a request to fetch the image to be displayed in the center of the screen on the home screen.
     *
     * @param imageUrl The URL of the image to be fetched.
     * @param queue    The request queue to which the image request will be added.
     */
    private void sendCenterImageRequest(String imageUrl, RequestQueue queue) {
        queue.add(new ImageRequest(
            imageUrl,
            response -> {
                runOnUiThread(() -> centerImage.setImageBitmap(response));
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
            Throwable::printStackTrace
        ));
    }

    /**
     * Initializes the UserHomeActivity. This method:
     * <ul>
     *     <li>Sets the content view layout.</li>
     *     <li>Retrieves user ID and username from the intent.</li>
     *     <li>Establishes a WebSocket connection for real-time chat.</li>
     *     <li>Initializes UI components and sets their properties.</li>
     *     <li>Fetches restaurants and populates the UI.</li>
     *     <li>Handles swipe gestures for liking and disliking restaurants.</li>
     *     <li>Handles button click events for liking, disliking, and accessing the user profile.</li>
     * </ul>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down, this Bundle contains the data it most recently
     * supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String username = intent.getStringExtra("username");

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        setContentView(R.layout.activity_userhome);

        WebSocketManager.getInstance().connectWebSocket("ws://10.0.2.2:8080/chat/" + username);
        WebSocketManager.getInstance().setWebSocketListener(UserHomeActivity.this);

        centerImage = findViewById(R.id.centerRestaurantImage);
        centerImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        locationIcon = findViewById(R.id.locationIcon);
        ratingIcon = findViewById(R.id.ratingIcon);
        restaurantName = findViewById(R.id.restName);
        rating = findViewById(R.id.rating);
        ratingCount = findViewById(R.id.ratingCount);
        address = findViewById(R.id.address);
        dislike = findViewById(R.id.dislikeBtn);
        favorite = findViewById(R.id.heartBtn);
        profile = findViewById(R.id.profileBtn);
        chip1 = findViewById(R.id.chip);
        chip2 = findViewById(R.id.chip2);
        chip3 = findViewById(R.id.chip3);
        chip4 = findViewById(R.id.chip4);
        chip5 = findViewById(R.id.chip5);
        chip6 = findViewById(R.id.chip6);
        chip7 = findViewById(R.id.chip7);
        logo = findViewById(R.id.restLogo);

        getRestaurants(queue);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // This method will be called when a swipe gesture is detected
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    // Horizontal swipe detected
                    if (diffX > 0) {
                        // Right swipe
                        try {
                            String code = currentRestaurant.getString("_code");
                            WebSocketManager.getInstance().sendMessage("like@" + code);
                            populateScreen(queue, restaurants.indexOf(currentRestaurant) + 1);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    } else {
                        // Left swipe
                        try {
                            String code = currentRestaurant.getString("_code");
                            WebSocketManager.getInstance().sendMessage("dislike@" + code);
                            populateScreen(queue, restaurants.indexOf(currentRestaurant) + 1);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        dislike.setOnClickListener(v -> {
            try {
                String code = currentRestaurant.getString("_code");
                WebSocketManager.getInstance().sendMessage("dislike@" + code);
                populateScreen(queue, restaurants.indexOf(currentRestaurant) + 1);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        favorite.setOnClickListener(v -> {
            try {
                String code = currentRestaurant.getString("_code");
                WebSocketManager.getInstance().sendMessage("like@" + code);
                populateScreen(queue, restaurants.indexOf(currentRestaurant) + 1);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        profile.setOnClickListener(v -> {
            Intent profile = new Intent(UserHomeActivity.this, SocialActivity.class);
            profile.putExtra("id", id);
            startActivity(profile);
        });
    }

    /**
     * Set's the gestureDetector defined in onCreate to call it's onTouchEvent()
     *
     * @param event The touch screen event being processed.
     *
     * @return event The touch screen event being processed.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakeData) {
        // Show user notification that they have joined a group!
        Log.d("WebSocketOpen", handshakeData.toString());
    }

    @Override
    public void onWebSocketMessage(String message) {
        // If the message is an invitation show notification on screen, telling user who invited them to a group
        Log.d("Message", message);
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // Show message telling the user who left the group
        Log.d("WebSocketClose", reason);
    }

    @Override
    public void onWebSocketError(Exception ex) {
        // Show notification to user informing them of the error
        Log.e("WebSocketError", ex.toString());
    }
}