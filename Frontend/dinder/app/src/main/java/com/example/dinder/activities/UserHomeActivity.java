package com.example.dinder.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;
import com.example.dinder.websocket.WebSocketListener;
import com.example.dinder.websocket.WebSocketManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The Home Screen is considered the default location of the app. On this screen the user can swipe
 * through restaurants, liking or disliking them. The user can also tap on any restaurant to take them
 * to the restaurant profile where they can see more information about the restaurant.
 */
public class UserHomeActivity extends AppCompatActivity implements WebSocketListener {
    public static final String MATCH_CHANNEL_ID = "Matches";
    private static final int NOTIFICATION_ID = 1;

    /**
     * Large image displayed in the center of the screen used to show the restaurant's food
     */
    ImageView centerImage;
    /**
     * Logo displayed in the top left of the screen belonging to the restaurant
     */
    ImageView logo;
    /**
     * Icon displayed to provide context to the restaurant's address (displayed next to it)
     */
    ImageView locationIcon;
    /**
     * Star icon displayed next to the restaurnt's x out of 5 star rating
     */
    ImageView ratingIcon;
    /**
     * The name of the restaurant displayed at the top of the screen
     */
    TextView restaurantName;
    /**
     * X out of 5 star rating of the restaurant
     */
    TextView rating;
    /**
     * The number of rating/reviews the restaurant has received
     */
    TextView ratingCount;
    /**
     * The restaurant's address
     */
    TextView address;
    /**
     * A button for the user to dislike the restaurant
     */
    ImageButton dislike;
    /**
     * A button for the user to like/favorite the restaurant
     */
    ImageButton favorite;
    /**
     * A chip used to display price information about the restaurant such as "$", "$$", or "$$$"
     */
    Chip chip1;
    /**
     * A chip used to display a snippet of contextual information about the restaurant
     */
    Chip chip2;
    /**
     * A chip used to display a snippet of contextual information about the restaurant
     */
    Chip chip3;
    /**
     * A chip used to display a snippet of contextual information about the restaurant
     */
    Chip chip4;
    /**
     * A chip used to display a snippet of contextual information about the restaurant
     */
    Chip chip5;
    /**
     * A chip used to display a snippet of contextual information about the restaurant
     */
    Chip chip6;
    /**
     * A chip used to display a snippet of contextual information about the restaurant
     */
    Chip chip7;

    static boolean connected = false;

    /**
     * A GestureDetector used to recognize when the user swipes left or right on the restaurant
     */
    private GestureDetector gestureDetector;
    /**
     * A list of JSONObjects representing restaurants used to display restaurants to the user
     */
    ArrayList<JSONObject> restaurants = new ArrayList<>();
    /**
     * The current restaurant being displayed to the user
     */
    JSONObject currentRestaurant;
    /**
     * Dialog used to display loading symbol while the restaurant's are being fetched
     */
    private Dialog loadingDialog;
    ArrayList<String> matchCodes = new ArrayList<>();
    LinearLayout notificationContainer;

    /**
     * Sets the text content for a specific chip based on its index and ensures its visibility.
     * The method takes in an index corresponding to a chip position and a tag to set as the
     * chip's text content. UI operations are executed on the main thread to ensure
     * that the user interface updates smoothly.
     *
     * @param index the position of the chip to update, starting from 0
     * @param tag   the text content to set for the specified chip
     */
    private void setChip(int index, String tag) {
        switch (index) {
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
                sendCenterImageRequest(currentRestaurant.getString("image_url"), queue);
                runOnUiThread(() -> {
                    try {
                        restaurantName.setText(currentRestaurant.getString("name"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
//                    try {
//                        address.setText(currentRestaurant.getString("address"));
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
                    try {
                        chip1.setText(currentRestaurant.getString("price"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        rating.setText(currentRestaurant.getString("rating"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        ratingCount.setText(String.format("(%s)", currentRestaurant.getString("review_count")));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
//                JSONArray titles = currentRestaurant.getJSONArray("_titles");
//                for (int i = 0; i < titles.length(); i++) {
//                    setChip(i, titles.get(i).toString());
//                }
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
        showLoadingDialog();

        JsonArrayRequest request = new JsonArrayRequest(
            Request.Method.GET,
            "http://coms-309-055.class.las.iastate.edu:8080/restaurant/Ames/all",
            null,
            response -> {
                hideLoadingDialog();
                Log.d("Response", response.toString());
                JSONArray receivedRestaurants;
                try {
                    receivedRestaurants = response.getJSONObject(0).getJSONArray("businesses");
                    Log.d("rest", receivedRestaurants.toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // Handle the JSON array response
                for (int i = 0; i < receivedRestaurants.length(); i++) {
                    try {
                        JSONObject restaurantObject = receivedRestaurants.getJSONObject(i);
                        restaurants.add(restaurantObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (!restaurants.isEmpty()) {
                    populateScreen(queue, 0);
                }
            },
            error -> {
                Log.e("Error", String.valueOf(error));
                hideLoadingDialog();
            }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
            5000, 3,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
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
     *                           previously being shut down, this Bundle contains the data it most recently
     *                           supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String username = intent.getStringExtra("username");
        ArrayList<String> receivedCodes = intent.getStringArrayListExtra("codes");
        if (receivedCodes != null) {
            matchCodes = receivedCodes;
        }

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        setContentView(R.layout.activity_userhome);
        setupLoadingDialog();

        if (!connected) {
            WebSocketManager.getInstance().connectWebSocket("ws://coms-309-055.class.las.iastate.edu:8080/chat/" + username);
            WebSocketManager.getInstance().setWebSocketListener(UserHomeActivity.this);
            connected = true;
        }

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
        chip1 = findViewById(R.id.chip);
        chip2 = findViewById(R.id.chip2);
        chip3 = findViewById(R.id.chip3);
        chip4 = findViewById(R.id.chip4);
        chip5 = findViewById(R.id.chip5);
        chip6 = findViewById(R.id.chip6);
        chip7 = findViewById(R.id.chip7);
        logo = findViewById(R.id.restLogo);
        notificationContainer = findViewById(R.id.match_notif_layout);

        getRestaurants(queue);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    // You are already on UserHomeActivity, no need to do anything here.
                    return true;
                } else if (itemId == R.id.match) {
                    startMatchesScreen(); // Start the MatchesScreen activity
                    return true;
                } else if (itemId == R.id.social) {
                    startSocialActivity(); // Start the SocialActivity
                    return true;
                } else if (itemId == R.id.userprofile) {
                    startUserProfileActivity(); // Start the UserProfileActivity
                    return true;
                }
                return false;
            }

            private void startMatchesScreen() {
                Intent intent = new Intent(UserHomeActivity.this, MatchesScreen.class);
                intent.putExtra("id", id);
                intent.putStringArrayListExtra("codes", matchCodes);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation for this transition
            }

            private void startSocialActivity() {
                Intent intent = new Intent(UserHomeActivity.this, SocialActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation for this transition
            }

            private void startUserProfileActivity() {
                Intent intent = new Intent(UserHomeActivity.this, UserProfileActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation for this transition
            }
        });

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
                            String code = currentRestaurant.getString("id");
                            WebSocketManager.getInstance().sendMessage("like@" + code);
                            populateScreen(queue, restaurants.indexOf(currentRestaurant) + 1);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    } else {
                        // Left swipe
                        try {
                            String code = currentRestaurant.getString("id");
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
                String code = currentRestaurant.getString("id");
                WebSocketManager.getInstance().sendMessage("dislike@" + code);
                Log.d("Dislike", "dislike@" + code);
                populateScreen(queue, restaurants.indexOf(currentRestaurant) + 1);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        favorite.setOnClickListener(v -> {
            try {
                String code = currentRestaurant.getString("id");
                WebSocketManager.getInstance().sendMessage("like@" + code);
                Log.d("Like", "like@" + code);
                populateScreen(queue, restaurants.indexOf(currentRestaurant) + 1);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        centerImage.setOnClickListener(v -> {
            try {
                Intent restaurant = new Intent(UserHomeActivity.this, RestaurantProfileActivity.class);
                restaurant.putExtra("id", id);
                restaurant.putExtra("code", currentRestaurant.getString("id"));
                startActivity(restaurant);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void showNotification() {
        Log.d("Animation", "playing animation");
        // Fade in the notification
        notificationContainer.animate()
                .alpha(1f)
                .setDuration(500) // 500ms
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        notificationContainer.setVisibility(View.VISIBLE);
                    }
                });

        // Hide the notification after 3 seconds
        notificationContainer.postDelayed(() -> {
            // Fade out the notification
            notificationContainer.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            notificationContainer.setVisibility(View.GONE);
                        }
                    });
        }, 3000); // 3 seconds
    }

    /**
     * Set's the gestureDetector defined in onCreate to call it's onTouchEvent()
     *
     * @param event The touch screen event being processed.
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
        if (message.contains("Match")) {
            runOnUiThread(() -> {
                showNotification();
            });
            String code = message.split("@")[1];
            matchCodes.add(code);
            Log.d("Code", code);
        }
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

    // Initialize the dialog in onCreate or wherever appropriate
    private void setupLoadingDialog() {
        loadingDialog = new Dialog(this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false); // prevents users from cancelling the dialog
    }

    // Show the dialog
    private void showLoadingDialog() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    // Hide the dialog
    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void displayMatchNotification(String matchInfo) {
        Intent intent = new Intent(UserHomeActivity.this, MatchesScreen.class);
        intent.putExtra("match_info", matchInfo);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MATCH_CHANNEL_ID)
                .setSmallIcon(R.drawable.favorite)
                .setContentTitle("New Match!")
                .setContentText("You have a new match!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


    }
}
