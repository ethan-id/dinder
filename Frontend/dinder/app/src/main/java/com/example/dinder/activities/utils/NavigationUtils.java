package com.example.dinder.activities.utils;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dinder.R;
import com.example.dinder.activities.MatchesScreen;
import com.example.dinder.activities.SocialActivity;
import com.example.dinder.activities.UserHomeActivity;
import com.example.dinder.activities.UserProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class NavigationUtils extends AppCompatActivity {
    public static void setupBottomNavigation(BottomNavigationView bottomNavigationView, Context context, String id, ArrayList<String> matchCodes, String username, Boolean plus) {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                startHomeScreen(context, id, matchCodes, username, plus);
            } else if (itemId == R.id.match) {
                startMatchesScreen(context, id, matchCodes, username, plus);
            } else if (itemId == R.id.social) {
                startSocialActivity(context, id, matchCodes, username, plus);
            } else if (itemId == R.id.userprofile) {
                startUserProfileActivity(context, id, matchCodes, username, plus);
            }
            return true;
        });
    }

    private static void startHomeScreen(Context context, String id, ArrayList<String> matchCodes, String username, Boolean plus) {
        if (!(context instanceof UserHomeActivity)) {
            Intent intent = new Intent(context, UserHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("id", id);
            intent.putStringArrayListExtra("codes", matchCodes);
            intent.putExtra("username", username);
            intent.putExtra("plus", plus);
            context.startActivity(intent);
        }
    }

    private static void startMatchesScreen(Context context, String id, ArrayList<String> matchCodes, String username, Boolean plus) {
        if (!(context instanceof MatchesScreen)) {
            Intent intent = new Intent(context, MatchesScreen.class);
            intent.putExtra("id", id);
            intent.putStringArrayListExtra("codes", matchCodes);
            intent.putExtra("username", username);
            intent.putExtra("plus", plus);
            context.startActivity(intent);
        }
    }

    private static void startSocialActivity(Context context, String id, ArrayList<String> matchCodes, String username, Boolean plus) {
        if (!(context instanceof SocialActivity)) {
            Intent intent = new Intent(context, SocialActivity.class);
            intent.putExtra("id", id);
            intent.putStringArrayListExtra("codes", matchCodes);
            intent.putExtra("username", username);
            intent.putExtra("plus", plus);
            context.startActivity(intent);
        }
    }

    private static void startUserProfileActivity(Context context, String id, ArrayList<String> matchCodes, String username, Boolean plus) {
        if (!(context instanceof UserProfileActivity)) {
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra("id", id);
            intent.putStringArrayListExtra("codes", matchCodes);
            intent.putExtra("username", username);
            intent.putExtra("plus", plus);
            context.startActivity(intent);
        }
    }
}
