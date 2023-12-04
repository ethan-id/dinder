package com.example.dinder.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.CheckBox;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;
import com.example.dinder.activities.utils.NavigationUtils;
import com.example.dinder.adapters.AdminRestaurantAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The Sign-Up screen; The user can enter their account information here and request to sign-up to Dinder
 */
public class AdminHomeActivity extends AppCompatActivity {

    List<JSONObject> list;
    RecyclerView restaurantList;
    AdminRestaurantAdapter restaurantListAdapter;
    private Dialog loadingDialog;

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
                            list.add(restaurantObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    restaurantList = findViewById(R.id.listOfCards);
                    restaurantListAdapter = new AdminRestaurantAdapter(list);
                },
                error -> {
                    Log.e("Error", String.valueOf(error));
                    hideLoadingDialog();
                    startActivity(new Intent(AdminHomeActivity.this, ErrorScreen.class));
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000, 3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        setContentView(R.layout.activity_admin_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        NavigationUtils.setupBottomNavigation(bottomNavigationView, this, id, codes, username);
        bottomNavigationView.setSelectedItemId(R.id.userprofile);

        setupLoadingDialog();

        getRestaurants(queue);
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