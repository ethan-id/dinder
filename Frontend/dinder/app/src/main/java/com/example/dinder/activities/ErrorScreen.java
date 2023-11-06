package com.example.dinder.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dinder.R;

/**
 * ErrorScreen is an activity class that represents an error page in the application.
 * It extends the AppCompatActivity class from the AndroidX library, providing
 * compatibility support for various Android versions and UI components.
 *
 * When this activity is started, it sets the content view to a layout defined in
 * {@code activity_error_screen.xml}, which should contain the UI elements to display
 * in case of an error.
 *
 * Usage:
 * Intent intent = new Intent(context, ErrorScreen.class);
 * startActivity(intent);
 *
 * This activity can be invoked whenever an error needs to be displayed to the user,
 * such as when network requests fail, or other non-recoverable errors occur during
 * the operation of the app.
 */
public class ErrorScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_screen);
    }
}