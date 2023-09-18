package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormatSymbols;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
          ListView lv;
          String[] months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.listview);
        months= new DateFormatSymbols().getMonths();
        ArrayAdapter<String> monthadap = new ArrayAdapter<> (this,android.R.layout.simple_list_item_1,months);
        lv.setAdapter(monthadap);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String currentmonth;
        currentmonth=months[position];
        Toast.makeText(this,currentmonth+"selected", Toast.LENGTH_SHORT).show();
    }
}