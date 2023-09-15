package com.example.dinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class CounterActivity extends AppCompatActivity {

    Button increaseBtn;
    Button decreaseBtn;
    Button backBtn;
    TextView numberTxt;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        increaseBtn = findViewById(R.id.increaseBtn);
        decreaseBtn = findViewById(R.id.decreaseBtn);
        backBtn = findViewById(R.id.backBtn);
        numberTxt = findViewById(R.id.number);
        numberTxt.setText(String.valueOf(counter));

        increaseBtn.setOnClickListener(v -> numberTxt.setText(String.valueOf(++counter)));
        decreaseBtn.setOnClickListener(v -> numberTxt.setText(String.valueOf(--counter)));

        // Get a RequestQueue
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        // ...
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://bb2f1cae-347d-4a40-8941-11471d81db18.mock.pstmn.io/wasabi", null,
            response -> {
                numberTxt.setText("Response: " + response.toString());
            },
            error -> {
                // TODO: Handle error
            }
        );

        // Add a request (in this example, called stringRequest) to your RequestQueue.
        queue.add(jsonObjectRequest);

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