package com.example.foodbookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Booking extends AppCompatActivity {

    CheckBox cb1,cb2,cb3,cb4;
    Button order;
    TextView Bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        cb1=findViewById(R.id.vegan);
        cb2=findViewById(R.id.nonveg);
        cb3=findViewById(R.id.veg);
        cb4=findViewById(R.id.drinks);
        order=findViewById(R.id.Book);
        Bill=findViewById(R.id.bill);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int total=0;

                if(cb1.isChecked()){
                    total=total+90;
                }
                if(cb2.isChecked()){
                    total=total+30;
                }
                if(cb3.isChecked()){
                    total=total+40;
                }
                if(cb4.isChecked()){
                    total=total+50;
                }

                Bill.setText("Your Bill is $"+ String.valueOf(total));
                Toast.makeText(Booking.this, "Booking Confirmed", Toast.LENGTH_SHORT).show();

            }
        });
    }
}