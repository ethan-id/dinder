package com.example.dinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The User Profile screen, used to display the user's information and allow them
 * to modify their dietary preferences and other settings affecting the rest of the
 * app.
 */
public class UserProfileActivity extends AppCompatActivity {
    /**
     * The User's profile picture
     */
    ImageView profilePic;
    /**
     * A button allowing the user to return to the home screen
     */
    ImageButton backBtn;
    /**
     * A "Save" button allowing the user to save any changes they make to their profile or preferences
     */
    Button saveBtn;
    /**
     * The user's name
     */
    TextView name;
    /**
     * A Header indicating the checkboxes beneath it represent the user's dietary restrictions
     */
    TextView dietRestrictions;
    /**
     * A checkbox for the user to select if they are vegetarian
     */
    CheckBox vegetarianCheck;
    /**
     * A checkbox for the user to select if they are vegan
     */
    CheckBox veganCheck;
    /**
     * A checkbox for the user to select if they are halal
     */
    CheckBox halalCheck;
    /**
     * A boolean value storing the value of the user's vegetarian status when they open the profile screen.
     * This is used to enable or disable the save button if there are differences or not.
     */
    Boolean ogVegetarian;
    /**
     * A boolean value storing the value of the user's vegan status when they open the profile screen.
     * This is used to enable or disable the save button if there are differences or not.
     */
    Boolean ogVegan;
    /**
     * A boolean value storing the value of the user's halal status when they open the profile screen.
     * This is used to enable or disable the save button if there are differences or not.
     */
    Boolean ogHalal;
    /**
     * A JSONObject containing all the user's information
     */
    JSONObject user;

    /**
     * Fetches the user data from the server based on the given user ID.
     * <p>
     * This method sends a GET request to retrieve the user's JSON data from the server using the provided ID.
     * Upon a successful response, the user's data is stored in the 'user' variable and the updateRestrictions()
     * method is invoked to update the user's settings or restrictions accordingly. If there's an error in
     * the network request, the stack trace is printed.
     * </p>
     *
     * @param id The unique identifier of the user to fetch data for.
     */
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

    /**
     * Checks if any dietary preference checkboxes have been modified.
     * <p>
     * This method compares the current states of the dietary preference checkboxes (vegan, vegetarian, and halal)
     * against their original states. If any checkbox state has changed, it enables the save button to allow
     * the user to save the updated preferences.
     * </p>
     */
    private void checkForPreferenceChanges() {
        if (ogVegan != veganCheck.isChecked() || ogVegetarian != vegetarianCheck.isChecked() || ogHalal != halalCheck.isChecked()) {
            runOnUiThread(() -> {
                saveBtn.setEnabled(true);
            });
        }
    }

    /**
     * Updates the UI to reflect the user's dietary restrictions.
     * <p>
     * This method retrieves the user's dietary preferences (vegan, vegetarian, and halal) from the
     * 'user' JSONObject. It then updates the respective checkboxes on the UI thread to reflect these
     * preferences.
     * </p>
     * @throws RuntimeException if there's an issue parsing the 'user' JSONObject.
     */
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

    /**
     * Saves the user's updated dietary preferences.
     * <p>
     * This method updates the 'user' JSONObject with the current values from the dietary preferences
     * checkboxes (vegan, vegetarian, and halal). A PUT request is then made to the server to save these
     * updated preferences.
     * </p>
     * @throws JSONException if there's an issue accessing or updating the 'user' JSONObject.
     */
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

    /**
     * Initializes the UserProfileActivity screen.
     * <p>
     * This method sets up the user profile screen where users can view and edit their
     * personal details and dietary preferences. It fetches the user details based on
     * the provided user ID, initializes UI components, and sets up event listeners
     * for the buttons and checkboxes on the screen.
     * </p>
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it
     *                           most recently supplied in onSaveInstanceState.
     *                           <b>Note:</b> Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.userprofile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.userprofile) {
                    // You're already on this page, so no need to do anything here.
                    return true;
                } else if (itemId == R.id.home) {
                    // Start the UserHomeActivity
                    Intent intent = new Intent(UserProfileActivity.this, UserHomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.match) {
                    // Start the MatchesScreenActivity
                    Intent intent = new Intent(UserProfileActivity.this, MatchesScreen.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.social) {
                    // Start the SocialActivity
                    Intent intent = new Intent(UserProfileActivity.this, SocialActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                }

                return false;
            }
        });

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
            homeScreen.putExtra("connected", true);
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