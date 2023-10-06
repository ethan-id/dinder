package com.example.dinder;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button loginBtn;
    Button signUpBtn;
    EditText username;
    EditText password;
    ImageView logo;

    private JsonObjectRequest createUserLoginRequest(String username, String password) {
        String url = String.format("http://10.0.2.2:8080/users/login/%s,%s", username, password);

        // Handle error
        return new JsonObjectRequest(url,
            response -> {
                // Handle response
                startActivity(new Intent(MainActivity.this, UserHomeActivity.class));
            },
            Throwable::printStackTrace
        );
    }

    protected boolean validLogin(String username, String password) {
        return !Objects.equals(username, "") && !Objects.equals(password, "");
    }

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

        // loginBtn
        loginBtn.setOnClickListener(v -> {
            if (validLogin(username.getText().toString(), password.getText().toString())) {
                JsonObjectRequest loginRequest = createUserLoginRequest(username.getText().toString(), password.getText().toString());
                queue.add(loginRequest);

                // Eventually check the user type here and then we can either start the default home page,
                // restaurant home page or the dev home page.
                startActivity(new Intent(MainActivity.this, UserHomeActivity.class));
            }

            // Reset password input after clicking the button
            password.setText("");
        });

        signUpBtn.setOnClickListener(v -> {
            Intent signUpScreen = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(signUpScreen);
        });
    }
}