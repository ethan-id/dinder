package com.example.dinder.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dinder.R;

public class OutOfLikesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_more_likes_screen);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Do nothing here and it will disable the back button.
    }
}
