package com.example.dinder.activities;

import android.os.Bundle;

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
    }
}