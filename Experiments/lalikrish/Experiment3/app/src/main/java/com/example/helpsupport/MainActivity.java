package com.yourpackage;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HelpSupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);

        // You can customize the help text here if needed.
        // Example: TextView helpText = findViewById(R.id.helpText);
        // helpText.setText("Custom help and support information");
    }
}
