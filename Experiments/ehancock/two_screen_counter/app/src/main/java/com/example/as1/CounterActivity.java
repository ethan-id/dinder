package com.example.as1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    Button increaseBtn;
    Button backBtn;
    TextView numberTxt;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        increaseBtn = findViewById(R.id.increaseBtn);
        backBtn = findViewById(R.id.backBtn);
        numberTxt = findViewById(R.id.number);

        increaseBtn.setOnClickListener(v -> numberTxt.setText(String.valueOf(++counter)));

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