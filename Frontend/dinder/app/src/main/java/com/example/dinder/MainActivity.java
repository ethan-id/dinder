package com.example.dinder;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button loginBtn;
    EditText username;
    EditText password;
    ImageView logo;

    private JsonObjectRequest createUserLoginRequest(String username, String password) {
        Intent intent = new Intent(MainActivity.this, CounterActivity.class);

        String url = String.format("http://10.0.2.2:8080/login/%s", username);

        return new JsonObjectRequest(url,
                response -> {
                    startActivity(intent);
                },
                error -> {
                    startActivity(intent);
                    // Handle login error/incorrect password or something
                }
        );
    }

    protected boolean validLogin(String username, String password) {
        return !Objects.equals(username, "") && !Objects.equals(password, "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.loginBtn);
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        logo = findViewById(R.id.appLogo);
        logo.setImageResource(R.drawable.temporary_logo);

        // loginBtn
        loginBtn.setOnClickListener(v -> {
            if (validLogin(username.getText().toString(), password.getText().toString())) {
                JsonObjectRequest loginRequest = createUserLoginRequest(username.getText().toString(), password.getText().toString());
                queue.add(loginRequest);
            }

            // Reset password input after clicking the button
            password.setText("");
        });
    }
}