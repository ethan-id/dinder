package com.example.dinder;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.ImageRequest;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

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

                // Move this block of code into the response callback
                if (!restaurants.isEmpty()) {
                    currentRestaurant = restaurants.get(0);
                    try {
                        // Populate screen with info about current restaurant
                        sendCenterImageRequest(currentRestaurant.getString("_url"), queue);
                        restaurantName.setText(currentRestaurant.getString("_name"));
                        address.setText(currentRestaurant.getString("_address"));
                        chip1.setText(currentRestaurant.getString("_price"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            },
            (Response.ErrorListener) Throwable::printStackTrace
        ));
    }

    private void sendCenterImageRequest(String imageUrl, RequestQueue queue) {
        queue.add(new ImageRequest(
                imageUrl,
                response -> {
                    // Handle the bitmap here. For example, set it to an ImageView.
                    centerImage.setImageBitmap(response);
                    Log.d("ImageRequest", "Width: " + response.getWidth());
                    Log.d("ImageRequest", "Height: " + response.getHeight());
                    Log.d("ImageRequest", "Byte count: " + response.getByteCount());
                },
                0, 0, ImageView.ScaleType.CENTER_CROP, null,
                error -> {
                    // Handle the error here.
                    error.printStackTrace();
                    Log.d("Error", error.toString());
                }
        ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        setContentView(R.layout.activity_userhome);
        centerImage = findViewById(R.id.centerRestaurantImage);
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
//        logo.setImageResource(R.drawable.temporary_logo);

        getRestaurants(queue);
    }
}