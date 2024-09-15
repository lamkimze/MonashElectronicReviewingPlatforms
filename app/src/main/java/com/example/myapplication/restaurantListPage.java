package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.RestaurantListAdapter;
import java.util.ArrayList;
import com.example.myapplication.Database.DatabaseHelper;

public class restaurantListPage extends AppCompatActivity {


    RestaurantListAdapter restaurantListAdapter;
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    ListView restaurantListView;
    DatabaseHelper dbHelper;

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

    // Retrieve user_id from intent
        int user_id = getIntent().getIntExtra("user_id", -1);

    // Initialize ListView and set the adapter
        restaurantListView = findViewById(R.id.restaurantListView);
        restaurantListAdapter = new RestaurantListAdapter(this, restaurants);
        restaurantListView.setAdapter(restaurantListAdapter);

//        get restaurants from database
        try {
            dbHelper = new DatabaseHelper(this);
            CRUD_Business crudBusiness = new CRUD_Business(dbHelper);
            ArrayList<Restaurant> dbRestaurants = crudBusiness.getAllRestaurants();
            restaurants.addAll(dbRestaurants);
        } catch (Exception e) {
            e.printStackTrace();
        }



        // Notify the adapter that the data has changed
        restaurantListAdapter.notifyDataSetChanged();

        // Set OnItemClickListener for the ListView
        restaurantListView.setOnItemClickListener((parent, view, position, id) -> {
            Restaurant restaurant = restaurants.get(position);
            int businessID = restaurant.getId();
            Intent restuarantIntent = new Intent(restaurantListPage.this, restaurantDetailPage.class);
            restuarantIntent.putExtra("bus_id", businessID);
            startActivity(restuarantIntent);
        });
    }
}