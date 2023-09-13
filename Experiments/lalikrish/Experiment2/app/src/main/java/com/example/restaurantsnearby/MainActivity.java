package com.example.restaurantsnearby;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView restaurantListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restaurantListView = findViewById(R.id.restaurantList);

        // Create a list of restaurants with sample data
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("Restaurant 1", "123 Main Street"));
        restaurants.add(new Restaurant("Restaurant 2", "456 Elm Street"));
        restaurants.add(new Restaurant("Restaurant 3", "789 Oak Street"));
        // Add more restaurants as needed

        // Create an ArrayAdapter to display the list of restaurants
        ArrayAdapter<Restaurant> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, restaurants
        );

        restaurantListView.setAdapter(adapter);
    }
}
}