package com.example.dinder;

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
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserHomeActivity extends AppCompatActivity {

    ImageView centerImage;
    ImageView logo;
    ImageView locationIcon;
    ImageView ratingIcon;
    TextView restaurantName;
    TextView rating;
    TextView ratingCount;
    TextView address;
    TextView username;
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

    // Helper method for populating chip tags dynamically based on number of tags provided
    private void setChip(int index, String tag) {
        switch(index) {
            case 0:
                chip2.setText(tag);
                chip2.setVisibility(View.VISIBLE);
                break;
            case 1:
                chip3.setText(tag);
                chip3.setVisibility(View.VISIBLE);
                break;
            case 2:
                chip4.setText(tag);
                chip4.setVisibility(View.VISIBLE);
                break;
            case 3:
                chip5.setText(tag);
                chip5.setVisibility(View.VISIBLE);
                break;
            case 4:
                chip6.setText(tag);
                chip6.setVisibility(View.VISIBLE);
                break;
            case 5:
                chip7.setText(tag);
                chip7.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void hideEmptyChips() {
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
    }

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

    private void getRestaurants(RequestQueue queue) {
        queue.add(new JsonArrayRequest(
            Request.Method.GET,
            "http://coms-309-055.class.las.iastate.edu:8080/restaurant/all",
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
        ratingIcon = findViewById(R.id.ratingIcon);
        restaurantName = findViewById(R.id.restName);
        rating = findViewById(R.id.rating);
        ratingCount = findViewById(R.id.ratingCount);
        address = findViewById(R.id.address);
        username = findViewById(R.id.username);
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

        Intent intent = getIntent();
        String userName = intent.getStringExtra("Username");

        username.setText(userName);

        dislike.setOnClickListener(v -> {
            populateScreen(queue,restaurants.indexOf(currentRestaurant) + 1);
        });

        favorite.setOnClickListener(v -> {
            populateScreen(queue,restaurants.indexOf(currentRestaurant) + 1);
        });
    }
}