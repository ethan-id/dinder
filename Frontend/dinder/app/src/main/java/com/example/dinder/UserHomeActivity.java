package com.example.dinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.ImageRequest;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserHomeActivity extends AppCompatActivity {

    ImageView centerImage;
    ImageView logo;
    ImageView locationIcon;
    TextView restaurantName;
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

    ArrayList<JSONObject> restaurants = new ArrayList<>();
    JSONObject currentRestaurant;

    private void populateScreen(RequestQueue queue, int index) {
        if (index < restaurants.size()) {
            currentRestaurant = restaurants.get(index);
            try {
                // Populate screen with info about current restaurant
                sendCenterImageRequest(currentRestaurant.getString("_url"), queue);
                restaurantName.setText(currentRestaurant.getString("_name"));
                address.setText(currentRestaurant.getString("_address"));
                chip1.setText(currentRestaurant.getString("_price"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Tell user there are no more restaurants :(
        }
    }

    private void getRestaurants(RequestQueue queue) {
        queue.add(new JsonArrayRequest(
            Request.Method.GET,
            "http://coms-309-055.class.las.iastate.edu:8080/restaurant/all",
            null,
            (Response.Listener<JSON1Array>) response -> {
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

    private void sendCenterImageRequest(String imageUrl, RequestQueue queue) {
        queue.add(new ImageRequest(
            imageUrl,
            response -> {
                centerImage.setImageBitmap(response);
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
            Throwable::printStackTrace
        ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        setContentView(R.layout.activity_userhome);
        centerImage = findViewById(R.id.centerRestaurantImage);
        centerImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        locationIcon = findViewById(R.id.locationIcon);
        restaurantName = findViewById(R.id.restName);
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

        dislike.setOnClickListener(v -> {
            populateScreen(queue,restaurants.indexOf(currentRestaurant) + 1);
        });

        favorite.setOnClickListener(v -> {
            populateScreen(queue,restaurants.indexOf(currentRestaurant) + 1);
        });
    }
}