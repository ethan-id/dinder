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

public class SignUpActivity extends AppCompatActivity {
    Button signUpBtn;
    Button backBtn;
    EditText email;
    EditText username;
    EditText password;
    EditText confirmPassword;
    CheckBox terms;
    CheckBox data;
    ImageView logo;

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

    private void sendRequestToMakeUser(RequestQueue queue, JSONObject userJson) {
        // Handle error
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://10.0.2.2:8080/users",
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