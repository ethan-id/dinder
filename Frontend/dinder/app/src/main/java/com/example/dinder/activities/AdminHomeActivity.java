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
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;
import com.example.dinder.activities.utils.NavigationUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Sign-Up screen; The user can enter their account information here and request to sign-up to Dinder
 */
public class AdminHomeActivity extends AppCompatActivity {
    private Dialog loadingDialog;

    private void getStats(RequestQueue queue) {
        showLoadingDialog(); // Show loading dialog before starting requests
        getStat("stats/get/Likes", R.id.tvTotalLikes, queue);
        getStat("stats/get/Users", R.id.tvTotalUsers, queue);
        getStat("stats/get/Favorites", R.id.tvTotalFavorites, queue);
        getStat("stats/get/SwipesBeforeMatch", R.id.tvAvgSwipesBeforeMatch, queue);
        getStat("stats/get/LikesPerMatch", R.id.tvAvgLikesPerMatch, queue);
        getStat("stats/get/SwipesBeforeLike", R.id.tvAvgSwipesBeforeLike, queue);
        getStat("stats/get/LikesPerUser", R.id.tvAvgLikesPerUser, queue);
        getStat("stats/get/MatchesPerUser", R.id.tvAvgMatchesPerUser, queue);
    }

    private void getStat(String endpoint, int textViewId, RequestQueue queue) {
        String url = "http://coms-309-055.class.las.iastate.edu:8080/" + endpoint; // Replace with your server address

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    updateTextView(textViewId, response);
                    hideLoadingDialog(); // Hide loading dialog when all requests are completed
                }, error -> {
                    Log.e("Volley", "Error: " + error.getMessage());
                    hideLoadingDialog(); // Hide loading dialog in case of error
                });

        // Set retry policy to handle timeouts and retries in case of failure
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, // Timeout in ms
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Number of retries
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    private void updateTextView(int id, String value) {
        TextView textView = findViewById(id);
        String currentValue = textView.getText().toString();
        textView.setText(MessageFormat.format("{0}{1}", currentValue, value));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        setContentView(R.layout.activity_admin_home);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String username = intent.getStringExtra("username");
        Boolean plus = intent.getBooleanExtra("plus", false);
        Boolean isAdmin = intent.getBooleanExtra("isAdmin", false);
        ArrayList<String> codes = intent.getStringArrayListExtra("codes");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        NavigationUtils.setupBottomNavigation(bottomNavigationView, this, id, codes, username, plus, isAdmin);

        setupLoadingDialog();

        getStats(queue);
    }

    private void setupLoadingDialog() {
        loadingDialog = new Dialog(this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false); // prevents users from cancelling the dialog
    }

    private void showLoadingDialog() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing() && isActivityAttached()) {
            loadingDialog.dismiss();
        }
    }

    private boolean isActivityAttached() {
        return getWindow() != null && getWindow().getDecorView().getWindowToken() != null;
    }
}