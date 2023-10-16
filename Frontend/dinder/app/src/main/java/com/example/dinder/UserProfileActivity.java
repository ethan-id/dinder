package com.example.dinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileActivity extends AppCompatActivity {

    ImageView profilePic;
    ImageButton backBtn;
    Button saveBtn;
    TextView name;
    TextView dietRestrictions;
    CheckBox vegetarianCheck;
    CheckBox veganCheck;
    CheckBox halalCheck;

    Boolean ogVegetarian;
    Boolean ogVegan;
    Boolean ogHalal;

    JSONObject user;

    private void getUser(String id) {
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://10.0.2.2:8080/users/" + id;

        queue.add(new JsonObjectRequest(
            Request.Method.GET, url, null,
            (Response.Listener<JSONObject>) response -> {
                user = response;
                updateRestrictions();
            },
            (Response.ErrorListener) Throwable::printStackTrace
        ));
    }

    private void checkForPreferenceChanges() {
        if (ogVegan != veganCheck.isChecked() || ogVegetarian != vegetarianCheck.isChecked() || ogHalal != halalCheck.isChecked()) {
            runOnUiThread(() -> {
                saveBtn.setEnabled(true);
            });
        }
    }

    private void updateRestrictions() {
        runOnUiThread(() -> {
            try {
                ogVegetarian = user.getBoolean("vegitarian");
                ogHalal = user.getBoolean("halal");
                ogVegan = user.getBoolean("vegan");
                vegetarianCheck.setChecked(ogVegetarian);
                halalCheck.setChecked(ogHalal);
                veganCheck.setChecked(ogVegan);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void saveUserPreferences() throws JSONException {
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://10.0.2.2:8080/users/" + user.getInt("id");

        user.put("vegitarian", vegetarianCheck.isChecked());
        user.put("vegan", veganCheck.isChecked());
        user.put("halal", halalCheck.isChecked());

        queue.add(new JsonObjectRequest(
            Request.Method.PUT, url, user,
            (Response.Listener<JSONObject>) response -> {
                user = response;
            },
            (Response.ErrorListener) Throwable::printStackTrace
        ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userprofile);

        profilePic = findViewById(R.id.profilePicture);
        backBtn = findViewById(R.id.backBtn);
        saveBtn = findViewById(R.id.saveBtn);
        name = findViewById(R.id.name);
        dietRestrictions = findViewById(R.id.dietRestrict);

        vegetarianCheck = findViewById(R.id.vegetarianCheck);
        veganCheck = findViewById(R.id.veganCheck);
        halalCheck = findViewById(R.id.halalCheck);

        Intent sentIntent = getIntent();
        String id = sentIntent.getStringExtra("id");

        getUser(id);

        saveBtn.setEnabled(false);

        backBtn.setOnClickListener(v -> {
            Intent homeScreen = new Intent(UserProfileActivity.this, UserHomeActivity.class);
            homeScreen.putExtra("id", id);
            startActivity(homeScreen);
        });

        saveBtn.setOnClickListener(v -> {
            try {
                saveUserPreferences();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        halalCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkForPreferenceChanges();
        });

        vegetarianCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkForPreferenceChanges();
        });

        veganCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkForPreferenceChanges();
        });
    }
}