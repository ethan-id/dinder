package com.example.dinder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.CheckBox;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.dinder.R;
import com.example.dinder.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The Sign-Up screen; The user can enter their account information here and request to sign-up to Dinder
 */
public class SignUpActivity extends AppCompatActivity {
    /**
     * Button used to send a POST request to sign-up the user
     */
    Button signUpBtn;
    /**
     * Button used to take the user back to the login screen
     */
    Button backBtn;
    /**
     * Text field where the user can enter their email
     */
    EditText email;
    /**
     * Text field where the user can enter their username
     */
    EditText username;
    /**
     * Text field where the user can enter their password
     */
    EditText password;
    /**
     * Text field where the user has to confirm their entered password
     */
    EditText confirmPassword;
    /**
     * Checkbox for the user to accept Dinder's terms of service
     */
    CheckBox terms;
    /**
     * Checkbox for the user to voluntarily accept Dinder's usage of their data
     */
    CheckBox data;
    /**
     * ImageView for the Dinder logo to be displayed
     */
    ImageView logo;

    /**
     * Constructs a JSONObject representing a user with the given parameters.
     * <p>
     * This method creates a JSONObject with fields for name, username, and password
     * using the provided arguments. If any JSON errors occur during the object creation,
     * they are caught and printed to the standard error stream.
     * </p>
     *
     * @param name     The name of the user.
     * @param username The username for the user.
     * @param password The password for the user.
     * @return A {@link JSONObject} representing a user with the specified details.
     */
    private JSONObject makeUserJson(String name, String username, String password) {
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("name", name);
            userJson.put("username", username);
            userJson.put("passkey", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userJson;
    }

    /**
     * Sends a POST request to create a new user using the provided user information.
     * <p>
     * This method sends a POST request to a specified endpoint with the provided user information
     * in JSON format. On successful request, it logs the server's response. If there's an error,
     * the error details are printed to the standard error stream.
     * </p>
     *
     * @param queue    The {@link RequestQueue} to which the request will be added.
     * @param userJson The {@link JSONObject} containing user details to be sent in the request body.
     */
    private void sendRequestToMakeUser(RequestQueue queue, JSONObject userJson) {
        // Handle error
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://coms-309-055.class.las.iastate.edu:8080/users",
                (Response.Listener<String>) response -> {
                    // Handle response
                    Log.d("Response", response);
                },
                (Response.ErrorListener) Throwable::printStackTrace
        ) {
            @Override
            public byte[] getBody() {
                return userJson.toString().getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        queue.add(postRequest);
    }

    /**
     * Initializes the SignUpActivity screen, setting up UI components and user interaction.
     * <p>
     * This method sets the content view for the activity, initializes all necessary UI elements,
     * and sets up their corresponding behaviors. It makes use of the Volley library to handle
     * network requests. When the "Sign Up" button is clicked, it checks if the terms checkbox is
     * ticked and if the passwords entered match. If these conditions are met, a new user is created
     * by sending a POST request with the user's details. After successful registration, the user is
     * navigated back to the login screen. Additionally, there's a "Back" button which simply navigates
     * the user back to the login screen.
     * </p>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        setContentView(R.layout.activity_signup);

        signUpBtn = findViewById(R.id.signUpBtn);
        backBtn = findViewById(R.id.backToLoginBtn);
        email = findViewById(R.id.editTextEmail);
        terms = findViewById(R.id.termsCheckbox);
        data = findViewById(R.id.dataConsentCheckbox);
        username = findViewById(R.id.editTextUsername);
        confirmPassword = findViewById(R.id.editTextConfirmPassword);
        password = findViewById(R.id.editTextPassword);
        logo = findViewById(R.id.appLogo);
        logo.setImageResource(R.drawable.temporary_logo);

        signUpBtn.setOnClickListener(v -> {
            if(terms.isChecked() && Objects.equals(password.getText().toString(), confirmPassword.getText().toString())) {
                // send post request to create user, then send request to login user based on response.
                JSONObject user = makeUserJson(email.getText().toString(), username.getText().toString(), password.getText().toString());
                sendRequestToMakeUser(queue, user);
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });
    }
}