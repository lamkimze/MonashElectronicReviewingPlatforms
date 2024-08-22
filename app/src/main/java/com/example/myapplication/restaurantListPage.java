package com.example.myapplication;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class restaurantListPage extends AppCompatActivity {


    ListAdapter listAdapter;
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    ListView restaurantListView;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_list_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.restaurantListView), (v, insets) -> {
            Insets insets1 = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets1.left, insets1.top, insets1.right, insets1.bottom);
            return insets;
        });

        // Step 2: Initialize ListView and set the adapter
        restaurantListView = findViewById(R.id.restaurantListView);
        listAdapter = new ListAdapter(this, restaurants);
        restaurantListView.setAdapter(listAdapter);

//        get restaurants from database
        try {
            db = new DatabaseHelper(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Restaurant> dbRestaurants = db.getAllRestaurants();
        restaurants.addAll(dbRestaurants);

        // Notify the adapter that the data has changed
        listAdapter.notifyDataSetChanged();
    }
}