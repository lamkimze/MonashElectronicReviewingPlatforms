package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class restaurantListPage extends AppCompatActivity {


    private RestaurantListAdapter restaurantListAdapter;
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<Restaurant> filteredRestaurants = new ArrayList<>();
    private ListView restaurantListView;
    private Spinner filterSpinner;
    private SearchView searchView;


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

        //  Retrieve user_id from intent
        int userID = getIntent().getIntExtra("userID", 5);

        //  Initialize DatabaseHelper and CRUD_Business
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        CRUD_Business crudBusiness = new CRUD_Business(dbHelper);

        //  Initialize ListView and set the adapter
        restaurantListView = findViewById(R.id.restaurantListView);

        // Initialize Spinner
        filterSpinner = findViewById(R.id.filterSpinner);

        // populate the spinner with the filter options
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.filter_options,
                android.R.layout.simple_spinner_item
        );

        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        // Initialize SearchView
        searchView = findViewById(R.id.searchView);


        // Set the item click listener for the ListView
        setListViewItemClickListener(userID);

        // Set the item selected listener for the Spinner
        setSpinnerItemSelectedListener();

        // Set the query text listener for the SearchView
        setSearchViewQueryTextListener();

        // Initialize the RestaurantListAdapter
        restaurantListAdapter = new RestaurantListAdapter(this, filteredRestaurants);
        restaurantListView.setAdapter(restaurantListAdapter);

        // Get all restaurants from the database
        restaurants.addAll(crudBusiness.getAllRestaurants());
        filteredRestaurants.addAll(restaurants);
        restaurantListAdapter.notifyDataSetChanged();

    }

    private void setListViewItemClickListener(int userID) {
        restaurantListView.setOnItemClickListener((parent, view, position, id) -> {
            Restaurant restaurant = restaurants.get(position);
            int businessID = restaurant.getId();
            Intent restuarantIntent = new Intent(restaurantListPage.this, restaurantDetailPage.class);
            restuarantIntent.putExtra("userID", userID);
            restuarantIntent.putExtra("busId", businessID);
            startActivity(restuarantIntent);
        });
    }

    private void setSpinnerItemSelectedListener() {
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = parent.getItemAtPosition(position).toString();
                // Apply filter based on selectedFilter
                filterRestaurants(selectedFilter);
                // Notify the adapter that the data has changed
                restaurantListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void filterRestaurants(String filter) {
        List<Restaurant> listToFilter = new ArrayList<>(filteredRestaurants.isEmpty() ? restaurants : filteredRestaurants);
        switch (filter) {
            case "Top Rated":
                // Sort restaurants by stars
                listToFilter.sort((r1, r2) -> Float.compare(r2.getStars(), r1.getStars()));
                break;
            case "A-Z":
                // Sort restaurants by name
                listToFilter.sort(Comparator.comparing(Restaurant::getName));
                break;
            case "Z-A":
                // Sort restaurants by name in reverse order
                listToFilter.sort((r1, r2) -> r2.getName().compareTo(r1.getName()));
                break;
            default:
                // Do nothing
                break;
        }
        filteredRestaurants.clear();
        filteredRestaurants.addAll(listToFilter);
        restaurantListAdapter.notifyDataSetChanged();
    }

    private void setSearchViewQueryTextListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                restaurantSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                restaurantSearch(newText);
                return true;
            }
        });
    }

    private void restaurantSearch(String query) {
        filteredRestaurants.clear();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredRestaurants.add(restaurant);
            }
        }
        String selectedFilter = filterSpinner.getSelectedItem().toString();
        filterRestaurants(selectedFilter);
    }
}