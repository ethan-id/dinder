package com.example.dinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Restaurant Profile screen, used to display a specific Restaurant's information
 */
public class RestaurantProfileActivity extends AppCompatActivity {
    /**
     * The logo of the restaurant
     */
    ImageView logo;
    /**
     * A button allowing the user to return to the home screen
     */
    ImageButton backBtn;
    /**
     * The restaurant's name
     */
    TextView name;
    /**
     * The restaurant's address
     */
    TextView address;
    /**
     * The number of likes the restaurant has received
     */
    TextView likeCount;
    /**
     * Restaurant's star rating out of 5
     */
    TextView rating;
    /**
     * A JSONObject containing all the restaurant's information
     */
    JSONObject restaurant;

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
                restaurant = response;
                populateRestaurantProfile();
                Log.d("restaurant", restaurant.toString());
            },
            Throwable::printStackTrace
        ));
    }

    private void populateRestaurantProfile() {
        runOnUiThread(() -> {
            try {
                name.setText(restaurant.getString("name"));
//                address.setText(restaurant.getString("address"));
                rating.setText(restaurant.getString("rating"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Initializes the Restaurant Profile screen.
     * <p>
     * This method sets up the restaurant profile screen where users can view details about a specific
     * restaurant.
     * </p>
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it
     *                           most recently supplied in onSaveInstanceState.
     *                           <b>Note:</b> Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurantprofile);

        logo = findViewById(R.id.restaurantLogo);
        backBtn = findViewById(R.id.backBtn);
        name = findViewById(R.id.restaurantName);
        address = findViewById(R.id.restaurantAddress);
        likeCount = findViewById(R.id.likeCount);
        rating = findViewById(R.id.restRating);

        Intent sentIntent = getIntent();
        String code = sentIntent.getStringExtra("code");
        String id = sentIntent.getStringExtra("id");

        getRestaurant(code);

        backBtn.setOnClickListener(v -> {
            Intent homeScreen = new Intent(RestaurantProfileActivity.this, UserHomeActivity.class);
            homeScreen.putExtra("id", id);
            homeScreen.putExtra("connected", true);
            startActivity(homeScreen);
        });
    }
}