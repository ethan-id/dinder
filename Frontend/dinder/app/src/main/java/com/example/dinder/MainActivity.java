package com.example.dinder;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.text.TextWatcher;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText username;
    EditText password;
    ImageView logo;

    protected boolean validLogin(String username, String password) {
        return !Objects.equals(username, "") && !Objects.equals(password, "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.loginBtn);
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        logo = findViewById(R.id.appLogo);
        logo.setImageResource(R.drawable.temporary_logo);

        // TextWatcher to observe changes in the EditText fields
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Enable button only if both email and password have content
                // update counts of username or password
            }
        };

        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CounterActivity.class);
            if (validLogin(username.getText().toString(), password.getText().toString())) {
                startActivity(intent);
            }
            username.setText("");
            password.setText("");
        });
    }
}