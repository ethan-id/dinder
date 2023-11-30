package com.example.dinder.activities;

import static java.lang.Float.parseFloat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;
import com.example.dinder.activities.utils.NavigationUtils;
import com.example.dinder.websocket.WebSocketListener;
import com.example.dinder.websocket.WebSocketManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The Home Screen is considered the default location of the app. On this screen the user can swipe
 * through restaurants, liking or disliking them. The user can also tap on any restaurant to take them
 * to the restaurant profile where they can see more information about the restaurant.
 */
public class UserHomeActivity extends AppCompatActivity implements WebSocketListener {
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
    RatingBar rating;
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
    /**
     * Boolean representing if the UserHomeActivity has an active WebSocket connection with the server
     */
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
    /**
     * ArrayList of Strings representing the specific identifiers of restaurants that the user has matched with
     * These are received through WebSocket messages sent from the backend and are used to send to the MatchesScreen
     */
    ArrayList<String> matchCodes = new ArrayList<>();
    /**
     * LinearLayout containing a notification to be displayed when the user has a new match
     */
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
            case 0 -> runOnUiThread(() -> {
                chip2.setText(tag);
                chip2.setVisibility(View.VISIBLE);
            });
            case 1 -> runOnUiThread(() -> {
                chip3.setText(tag);
                chip3.setVisibility(View.VISIBLE);
            });
            case 2 -> runOnUiThread(() -> {
                chip4.setText(tag);
                chip4.setVisibility(View.VISIBLE);
            });
            case 3 -> runOnUiThread(() -> {
                chip5.setText(tag);
                chip5.setVisibility(View.VISIBLE);
            });
            case 4 -> runOnUiThread(() -> {
                chip6.setText(tag);
                chip6.setVisibility(View.VISIBLE);
            });
            case 5 -> runOnUiThread(() -> {
                chip7.setText(tag);
                chip7.setVisibility(View.VISIBLE);
            });
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
                        Log.e("Error", "Error populating restaurant data");
                    }
                    try {
                        JSONObject location = currentRestaurant.getJSONObject("location");
                        address.setText(location.getString("address1"));
                    } catch (JSONException e) {
                        Log.e("Error", "Error populating restaurant data");
                    }
                    try {
                        chip1.setText(currentRestaurant.getString("price"));
                    } catch (JSONException ignored) {}
                    try {
                        rating.setRating(parseFloat(currentRestaurant.getString("rating")));
                    } catch (JSONException e) {
                        Log.e("Error", "Error populating restaurant data");
                    }
                    try {
                        ratingCount.setText(String.format("(%s)", currentRestaurant.getString("review_count")));
                    } catch (JSONException e) {
                        Log.e("Error", "Error populating restaurant data");
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
                startActivity(new Intent(UserHomeActivity.this, ErrorScreen.class));
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
    @SuppressLint("ClickableViewAccessibility")
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

        WebSocketManager.getInstance().setWebSocketListener(UserHomeActivity.this);

        if (!connected) {
            WebSocketManager.getInstance().connectWebSocket("ws://coms-309-055.class.las.iastate.edu:8080/chat/" + username);
            connected = true;
        }

        centerImage = findViewById(R.id.centerRestaurantImage);
        centerImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        locationIcon = findViewById(R.id.locationIcon);
        ratingIcon = findViewById(R.id.ratingIcon);
        restaurantName = findViewById(R.id.restName);
        rating = findViewById(R.id.ratingBar);
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

        if (savedInstanceState != null) {
            // Activity is being recreated, restore the state
            int savedIndex = savedInstanceState.getInt("currentRestaurantIndex", -1);
            if (savedIndex != -1 && savedIndex < restaurants.size()) {
                // Restore the view with the saved restaurant
                populateScreen(queue, savedIndex);
            }
        } else {
            // Normal activity setup
            getRestaurants(queue);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        NavigationUtils.setupBottomNavigation(bottomNavigationView, this, id, matchCodes, username);
        bottomNavigationView.setSelectedItemId(R.id.home);

        dislike.setOnClickListener(v -> dislikeRestaurant());
        favorite.setOnClickListener(v -> likeRestaurant());
        centerImage.setOnClickListener(v -> {
            try {
                Intent restaurant = new Intent(UserHomeActivity.this, RestaurantProfileActivity.class);
                restaurant.putExtra("id", id);
                restaurant.putExtra("username", username);
                restaurant.putExtra("code", currentRestaurant.getString("id"));
                startActivity(restaurant);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        centerImage.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

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
                        likeRestaurant();
                    } else {
                        // Left swipe
                        dislikeRestaurant();
                    }
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current restaurant index and any other relevant data
        int currentRestaurantIndex = restaurants.indexOf(currentRestaurant);
        outState.putInt("currentRestaurantIndex", currentRestaurantIndex);
    }

    /**
     * Sends a "like" action for the current restaurant through a WebSocket connection and
     * prepares the next restaurant's information to be displayed.
     * The method retrieves the unique identifier of the current restaurant, sends it as a "like" action
     * through a WebSocket message, and logs this action. It then attempts to populate the screen with the
     * next restaurant in the list.
     * If the current restaurant's ID cannot be obtained due to a JSON parsing error, the method throws
     * a RuntimeException encapsulating the JSONException.
     *
     * @throws RuntimeException if there is an error parsing the current restaurant's JSON object.
     */
    private void likeRestaurant() {
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        try {
            String code = currentRestaurant.getString("id");
            sendLikeThroughWebSocket(code);
            Log.d("Like", "like@" + code);
            populateScreen(queue, restaurants.indexOf(currentRestaurant) + 1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Sends a "dislike" action for the current restaurant through a WebSocket connection and
     * prepares the next restaurant's information to be displayed.
     * This method performs an operation similar to {@link #likeRestaurant()} but for a "dislike" action.
     * It retrieves the unique identifier of the current restaurant, sends a "dislike" message through
     * the WebSocket, and logs this action. Subsequently, it moves to display the next restaurant's details.
     * If the current restaurant's ID cannot be obtained due to a JSON parsing error, the method throws
     * a RuntimeException encapsulating the JSONException.
     *
     * @throws RuntimeException if there is an error parsing the current restaurant's JSON object.
     */
    private void dislikeRestaurant() {
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        try {
            String code = currentRestaurant.getString("id");
            sendDislikeThroughWebSocket(code);
            Log.d("Dislike", "dislike@" + code);
            populateScreen(queue, restaurants.indexOf(currentRestaurant) + 1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a "like" action for a restaurant to the server via WebSocket.
     * This method checks if the provided restaurant code is not null or empty and sends a "like" action
     * prefixed to the restaurant code via WebSocket. This indicates that the user has liked a particular
     * restaurant.
     *
     * @param code The unique identifier for the restaurant to be liked.
     */
    private void sendLikeThroughWebSocket(String code) {
        if (!Objects.equals(code, "") && code != null) WebSocketManager.getInstance().sendMessage("like@" + code);
    }


    /**
     * Sends a "dislike" action for a restaurant to the server via WebSocket.
     * This method is similar to {@link #sendLikeThroughWebSocket(String)} but for sending a "dislike"
     * action. If the provided restaurant code is valid (not null or empty), it prefixes the code with "dislike@"
     * and sends it via WebSocket to indicate that the user has disliked the restaurant.
     *
     * @param code The unique identifier for the restaurant to be disliked.
     */
    private void sendDislikeThroughWebSocket(String code) {
        if (!Objects.equals(code, "") && code != null) WebSocketManager.getInstance().sendMessage("dislike@" + code);
    }

    /**
     * Shows a temporary notification with a fade-in animation, then fades out after a set time.
     * This method starts an animation that fades in the notification container, makes it visible, and logs
     * the animation start. After a delay of 3 seconds, it initiates a fade-out animation and hides the
     * notification container once the animation ends. This provides a transient visual feedback to the user.
     */
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
        // Be sure to call the superclass implementation
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
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
            runOnUiThread(this::showNotification);
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

    /**
     * Initializes and configures a non-cancellable loading dialog.
     * This method sets up a new dialog intended to indicate that a loading process is ongoing. The dialog is
     * made non-cancellable, meaning the user cannot dismiss it by pressing back or touching outside the dialog.
     * This is often used to prevent user interaction while waiting for a background task to complete.
     * The dialog uses a custom layout defined in `R.layout.loading` and has a transparent background.
     */
    private void setupLoadingDialog() {
        loadingDialog = new Dialog(this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false); // prevents users from cancelling the dialog
    }

    /**
     * Displays a loading dialog on the screen if it is not already showing.
     * This method checks the current state of the loadingDialog instance and
     * ensures that the dialog is shown only if it is not already visible on the screen,
     * avoiding multiple instances of the dialog being displayed over each other.
     */
    private void showLoadingDialog() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    /**
     * Hides the loading dialog if it is currently displayed on the screen.
     * This method checks the current state of the loadingDialog instance and
     * dismisses it if it is visible. Additionally, it checks if the activity
     * is still attached to the window before dismissing the dialog to prevent
     * illegal state exceptions.
     */
    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing() && isActivityAttached()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * Checks if the activity is currently attached to the window.
     * This is useful to ensure that we are not attempting to perform UI
     * operations on a detached activity.
     *
     * @return true if the activity is attached to the window, false otherwise.
     */
    private boolean isActivityAttached() {
        return getWindow() != null && getWindow().getDecorView().getWindowToken() != null;
    }
}
