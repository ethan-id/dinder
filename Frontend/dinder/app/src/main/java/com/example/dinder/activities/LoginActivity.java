package com.example.dinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;

import org.json.JSONException;

import java.util.Objects;

/**
 * An activity class for the Login Screen, used to handle logic so the user can enter their login
 * information and attempt to login.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * A Button that sends an HTTP request requesting the user be logged in when it is clicked
     */
    Button loginBtn;
    /**
     * A Button that takes the user to the sign-up screen
     */
    Button signUpBtn;
    /**
     * A text field allowing the user to enter their username
     */
    EditText username;
    /**
     * A text field allowing the user to enter their password
     */
    EditText password;
    /**
     * The logo displayed at the top of the login screen
     */
    ImageView logo;

    /**
     * Dialog for loading icon to be shown after user clicks "Login"
     */
    private Dialog loadingDialog;

    /**
     * Creates a JSON Object request to login a user.
     * <p>
     * This method sends a request to the server to authenticate the provided username and password.
     * Upon a successful response, it retrieves the user's ID and username and starts the UserHomeActivity,
     * passing the retrieved information to it.
     * </p>
     *
     * @param username The username of the user attempting to log in.
     * @param password The password of the user attempting to log in.
     * @return A JsonObjectRequest for user authentication.
     */
    private JsonObjectRequest createUserLoginRequest(String username, String password) {
        String url = String.format("http://coms-309-055.class.las.iastate.edu:8080/users/login/%s,%s", username, password);

        // Handle error
        return new JsonObjectRequest(url,
            response -> {
                // Handle response
                hideLoadingDialog();
                Log.d("Response", response.toString());
                // Eventually check the user type here and then we can either start the default home page,
                // restaurant home page or the dev home page.
                Boolean isAdmin = false;
                try {
                    isAdmin = response.getBoolean("admin");
                } catch (JSONException e) {
                    Log.e("Error getting admin type", e.toString());
//                    throw new RuntimeException(e);
                }
                Intent Homepage = new Intent(LoginActivity.this, UserHomeActivity.class);
                try {
                    Homepage.putExtra("id", String.valueOf(response.getInt("id")));
                    Homepage.putExtra("username", response.getString("username"));
                    Homepage.putExtra("plus", response.getBoolean("plus"));
                    Homepage.putExtra("isAdmin", isAdmin);
                } catch (JSONException e) {
                    Log.e("Error", e.toString());
                }
                startActivity(Homepage);
            },
            error -> {
                Log.e("Login Error", String.valueOf(error));
                hideLoadingDialog();
                showNotification();
            }
        );
    }

    /**
     * Validates the provided login credentials.
     * <p>
     * This method checks if the given username and password are not empty
     * to determine if they are valid for login purposes.
     * </p>
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @return true if both username and password are non-empty, false otherwise.
     */
    protected boolean validLogin(String username, String password) {
        return !Objects.equals(username, "") && !Objects.equals(password, "");
    }

    /**
     * Initializes the LoginActivity and sets up UI event listeners.
     * <p>
     * This method is invoked when the LoginActivity is created. It sets the content view,
     * initializes UI components, and establishes click listeners for the login and sign up buttons.
     * The login button sends a login request after validating the username and password, while
     * the sign up button navigates the user to the SignUpActivity.
     * </p>
     *
     * @param savedInstanceState A bundle containing the most recent saved instance of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        setContentView(R.layout.activity_login);
        setupLoadingDialog();

        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.mainSignUpBtn);
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        logo = findViewById(R.id.appLogo);
        logo.setImageResource(R.drawable.temporary_logo);

        loginBtn.setOnClickListener(v -> {
            if (validLogin(username.getText().toString(), password.getText().toString())) {
                JsonObjectRequest loginRequest = createUserLoginRequest(username.getText().toString(), password.getText().toString());
                showLoadingDialog();
                queue.add(loginRequest);
            }

            // Reset password input after clicking the button
            runOnUiThread(() -> password.setText(""));
        });

        signUpBtn.setOnClickListener(v -> {
            Intent signUpScreen = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(signUpScreen);
        });
    }

    /**
     * Initializes and configures a non-cancellable loading dialog.
     *
     * This method sets up a new dialog intended to indicate that a loading process is ongoing. The dialog is
     * made non-cancellable, meaning the user cannot dismiss it by pressing back or touching outside the dialog.
     * This is often used to prevent user interaction while waiting for a background task to complete.
     * The dialog uses a custom layout defined in `R.layout.loading` and has a transparent background.
     */
    private void setupLoadingDialog() {
        loadingDialog = new Dialog(this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
     * dismisses it if it is visible. This is typically called when an operation
     * that requires a loading indicator is completed or cancelled.
     */
    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * Shows a notification with a fade-in effect, then automatically hides it after a specified time with a fade-out effect.
     * The notification will become visible with a fade-in animation over 500 milliseconds,
     * stay on the screen for 3 seconds, and then fade out with a 500 milliseconds animation.
     *
     * During the fade-in animation, the visibility of the notification container is set to VISIBLE.
     * After the fade-out animation completes, the visibility is switched to GONE.
     */
    private void showNotification() {
        // Fade in the notification
        Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
    }
}