package com.example.dinder.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;
import com.example.dinder.adapters.ReviewAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
     * The number of reviews the restaurant has received
     */
    TextView revCount;
    /**
     * The number of likes the restaurant has received
     */
    TextView phone;
    /**
     * Restaurant's star rating out of 5
     */
    TextView rating;
    /**
     * A JSONObject containing all the restaurant's information
     */
    JSONObject restaurant;
    /**
     * List of reviews of the restaurant as JSON Objects
     */
    ArrayList<JSONObject> reviews = new ArrayList<>();

    private RecyclerView reviewRecyclerView;
    private RecyclerView.Adapter reviewAdapter;
    private RecyclerView.LayoutManager reviewLayoutManager;

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

    /**
     * Fetches and processes restaurant reviews from a remote server.
     *
     * This method makes a GET request to a specified URL to retrieve restaurant reviews.
     * Each review is expected to be in JSON format. The method adds all received reviews to
     * a local collection, `reviews`. In case of a JSON parsing error, it throws a runtime exception.
     *
     * @param code The unique code or identifier for the restaurant whose reviews are to be fetched.
     *             This code is appended to the URL endpoint to specify the restaurant.
     * @throws RuntimeException if there is a JSONException while parsing the response.
     */
    private void getRestaurantReviews(String code) {
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://coms-309-055.class.las.iastate.edu:8080/restaurant/reviews/" + code;

        queue.add(new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    JSONArray rev;
                    try {
                        rev = response.getJSONArray("reviews");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < rev.length(); i++) {
                        try {
                            reviews.add(rev.getJSONObject(i));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    // Specify an adapter and populate reviews list
                    reviewAdapter = new ReviewAdapter(reviews);
                    Log.d("reviews", reviews.toString());
                    reviewRecyclerView.setAdapter(reviewAdapter);
                },
                Throwable::printStackTrace
        ));
    }

    /**
     * Populates the restaurant profile UI components with data from a JSON object representing a restaurant.
     * This method must be called on the UI thread because it updates UI components.
     *
     * The restaurant profile includes the restaurant's name, rating, address, phone number, and review count.
     * It assumes that the JSON object `restaurant` has been previously set with the necessary information.
     *
     * If there is an issue with JSON parsing, the method throws a RuntimeException encapsulating the JSONException.
     */
    private void populateRestaurantProfile() {
        runOnUiThread(() -> {
            try {
                name.setText(restaurant.getString("name"));
                rating.setText(restaurant.getString("rating"));
                JSONObject location = restaurant.getJSONObject("location");
                address.setText(location.getString("address1"));
                phone.setText(restaurant.getString("display_phone"));
                revCount.setText(String.format("(%s)", restaurant.getString("review_count")));

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        // Add markers here
                        try {
                            JSONObject coords = restaurant.getJSONObject("coordinates");
                            LatLng restaurantLocation = new LatLng(coords.getDouble("latitude"), coords.getDouble("longitude"));
                            googleMap.addMarker(new MarkerOptions().position(restaurantLocation).title(restaurant.getString("name")));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 20)); // 15 here is the zoom level
                        } catch (JSONException e) {
                            Log.e("Maps Error", e.toString());
                        }
                    }
                });
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
        phone = findViewById(R.id.phone);
        rating = findViewById(R.id.restRating);
        revCount = findViewById(R.id.reviewCount);

        Intent sentIntent = getIntent();
        String code = sentIntent.getStringExtra("code");
        String username = sentIntent.getStringExtra("username");
        String id = sentIntent.getStringExtra("id");
        Boolean plus = sentIntent.getBooleanExtra("plus", false);

        getRestaurant(code);
        getRestaurantReviews(code);

        reviewRecyclerView = findViewById(R.id.reviewList);

        // Use this setting to improve performance
        reviewRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        reviewLayoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);

        backBtn.setOnClickListener(v -> {
            Intent homeScreen = new Intent(RestaurantProfileActivity.this, UserHomeActivity.class);
            homeScreen.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            homeScreen.putExtra("id", id);
            homeScreen.putExtra("username", username);
            homeScreen.putExtra("plus", plus);
            homeScreen.putExtra("connected", true);
            startActivity(homeScreen);
        });
    }
}