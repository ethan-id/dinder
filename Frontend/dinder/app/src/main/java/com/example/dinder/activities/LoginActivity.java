package com.example.dinder.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
     * A Button that send's an HTTP request requesting the user be logged in when it is clicked
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
        String url = String.format("http://10.0.2.2:8080/users/login/%s,%s", username, password);

        // Handle error
        return new JsonObjectRequest(url,
            response -> {
                // Handle response
                Log.d("Response", response.toString());

                // Eventually check the user type here and then we can either start the default home page,
                // restaurant home page or the dev home page.
                Intent Homepage = new Intent(LoginActivity.this, UserHomeActivity.class);
                try {
                    Homepage.putExtra("id", String.valueOf(response.getInt("id")));
                    Homepage.putExtra("username", response.getString("username"));
                    Homepage.putExtra("connected", false);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                startActivity(Homepage);
            },
            Throwable::printStackTrace
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

        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.signUpBtn);
        signUpBtn = findViewById(R.id.backToLoginBtn);
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        logo = findViewById(R.id.appLogo);
        logo.setImageResource(R.drawable.temporary_logo);

        loginBtn.setOnClickListener(v -> {
            if (validLogin(username.getText().toString(), password.getText().toString())) {
                JsonObjectRequest loginRequest = createUserLoginRequest(username.getText().toString(), password.getText().toString());
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
}