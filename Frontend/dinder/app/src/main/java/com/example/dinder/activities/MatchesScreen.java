package com.example.dinder.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dinder.R;

/**
 * The screen used to display the restaurants the user has been matched with
 */
public class MatchesScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_screen);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.match);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId(); // Get the selected item's ID

            if (itemId == R.id.home) {
                // Start the UserHomeActivity without animation
                Intent intent1 = new Intent(MatchesScreen.this, UserHomeActivity.class);
                intent1.putExtra("id", id);
                startActivity(intent1);
                overridePendingTransition(0, 0); // No animation for this transition
                finish();
                return true;
            } else if (itemId == R.id.match) {
                // You're already on this page, so no need to do anything here.
                return true;
            } else if (itemId == R.id.social) {
                // Start the SocialActivity without animation
                Intent intent1 = new Intent(MatchesScreen.this, SocialActivity.class);
                intent1.putExtra("id", id);
                startActivity(intent1);
                overridePendingTransition(0, 0); // No animation for this transition
                finish();
                return true;
            } else if (itemId == R.id.userprofile) {
                // Start the UserProfileActivity without animation
                Intent intent1 = new Intent(MatchesScreen.this, UserProfileActivity.class);
                intent1.putExtra("id", id);
                startActivity(intent1);
                overridePendingTransition(0, 0); // No animation for this transition
                finish();
                return true;
            }

            return false;
        });

    }

}
