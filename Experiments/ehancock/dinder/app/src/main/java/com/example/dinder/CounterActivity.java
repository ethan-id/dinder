package com.example.dinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

public class CounterActivity extends AppCompatActivity {

    Button sendBtn;
    Button backBtn;
    TextView numberTxt;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        // ...
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://ip.jsontest.com/", null,
                response -> {
                    numberTxt.setText("Response: " + response.toString());
                },
                error -> {
                    // TODO: Handle error
                    numberTxt.setText("Error: " + error.toString());
                }
        );

        sendBtn = findViewById(R.id.sendGet);
        backBtn = findViewById(R.id.backBtn);
        numberTxt = findViewById(R.id.number);
        numberTxt.setText(String.valueOf(counter));

        sendBtn.setOnClickListener(v -> {
            queue.add(jsonObjectRequest);
        });

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CounterActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    // Save the counter when the activity is destroyed
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("COUNTER_VALUE", counter);

    }

    // Restore the counter when the activity is recreated
    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int oldCount = savedInstanceState.getInt("COUNTER_VALUE");
        numberTxt.setText(String.valueOf(oldCount));
    }
}