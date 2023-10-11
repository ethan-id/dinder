package com.example.dinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

public class UserProfileActivity extends AppCompatActivity {

    ImageView profilePic;
    ImageButton backBtn;
    TextView name;
    TextView dietRestrictions;
    CheckBox vegetarianCheck;
    CheckBox veganCheck;
    CheckBox halalCheck;

//    private void sendCenterImageRequest(String imageUrl, RequestQueue queue) {
//        queue.add(new ImageRequest(
//                imageUrl,
//                response -> {
//                    centerImage.setImageBitmap(response);
//                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
//                Throwable::printStackTrace
//        ));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        setContentView(R.layout.activity_userprofile);

        profilePic = findViewById(R.id.profilePicture);
        backBtn = findViewById(R.id.backBtn);
        name = findViewById(R.id.name);
        dietRestrictions = findViewById(R.id.dietRestrict);
        vegetarianCheck = findViewById(R.id.vegetarianCheck);
        veganCheck = findViewById(R.id.veganCheck);
        halalCheck = findViewById(R.id.halalCheck);

        Intent sentIntent = getIntent();
        String username = sentIntent.getStringExtra("name");
        boolean vegetarian = sentIntent.getBooleanExtra("vegetarian", false);
        boolean vegan = sentIntent.getBooleanExtra("vegan", false);
        boolean halal = sentIntent.getBooleanExtra("halal", false);

        runOnUiThread(() -> {
            name.setText(username);
            vegetarianCheck.setActivated(vegetarian);
            veganCheck.setActivated(vegan);
            halalCheck.setActivated(halal);
        });

        backBtn.setOnClickListener(v -> {
            Intent homeScreen = new Intent(UserProfileActivity.this, UserHomeActivity.class);
            homeScreen.putExtra("Username", username);
            homeScreen.putExtra("vegetarian", vegetarianCheck.isChecked());
            homeScreen.putExtra("vegan", veganCheck.isChecked());
            homeScreen.putExtra("halal", halalCheck.isChecked());
            startActivity(homeScreen);
        });
    }
}