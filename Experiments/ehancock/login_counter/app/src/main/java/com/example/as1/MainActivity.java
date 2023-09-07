package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;

public class MainActivity extends AppCompatActivity {
    Button button;

    EditText email;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.toCounterBtn);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);

        button.setEnabled(false);

        // TextWatcher to observe changes in the EditText fields
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Enable button only if both email and password have content
                button.setEnabled(!email.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty());
            }
        };

        email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CounterActivity.class);
            startActivity(intent);
            email.setText("");
            password.setText("");
        });
    }
}