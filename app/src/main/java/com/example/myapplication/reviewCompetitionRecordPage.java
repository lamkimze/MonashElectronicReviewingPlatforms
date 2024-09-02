package com.example.myapplication;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.databinding.ActivityReviewCompetitionRecordPageBinding;

import java.util.ArrayList;
import java.util.List;

public class reviewCompetitionRecordPage extends DrawerBaseActivity {

    ActivityReviewCompetitionRecordPageBinding activityReviewCompetitionRecordPageBinding;

    ArrayList<Restaurant> listRestaurant = new ArrayList<>();
    DatabaseHelper dbHelper;
    CompetitionRecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReviewCompetitionRecordPageBinding = ActivityReviewCompetitionRecordPageBinding.inflate(getLayoutInflater());
        allocateActivityTitle("Review Competition Records");
        setContentView(activityReviewCompetitionRecordPageBinding.getRoot());

        onPostCreate(savedInstanceState);

        try {
            dbHelper = new DatabaseHelper(this);
            CRUD_Business crudBusiness = new CRUD_Business(dbHelper);
            ArrayList<Restaurant> dbRestaurants = crudBusiness.getAllRestaurants();
            listRestaurant.addAll(dbRestaurants);
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                filterList(newText);
                return true;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new CompetitionRecyclerAdapter();
        recyclerAdapter.setData(listRestaurant);
        recyclerView.setAdapter(recyclerAdapter);

    }

    private void filterList(String text) {
        List<Restaurant> filteredList = new ArrayList<>();
        for(Restaurant restaurant: listRestaurant){
            if(restaurant.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(restaurant);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }else{
            recyclerAdapter.setFilteredList(filteredList);
        }
    }

    private void addRestaurantToRecyclerView(Restaurant restaurant){
        listRestaurant.add(restaurant);
        recyclerAdapter.notifyDataSetChanged();
    }

}
