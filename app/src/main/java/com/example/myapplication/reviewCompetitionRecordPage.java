package com.example.myapplication;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ActivityReviewCompetitionRecordPageBinding;

import java.util.ArrayList;
import java.util.List;

public class reviewCompetitionRecordPage extends DrawerBaseActivity {

    ActivityReviewCompetitionRecordPageBinding activityReviewCompetitionRecordPageBinding;

    ArrayList<Restaurant> listRestaurant = new ArrayList<>();
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

        // Sample data for demonstration
        Restaurant r1 = new Restaurant("Guzman Y Gomez", "https://drive.google.com/uc?export=download&id=16DiM24O7ysYcDxlCIEnr1oVHLkfAowVt", "");
        Restaurant r2 = new Restaurant("PapaRich", "R.drawable.default_icon", "");
        Restaurant r3 = new Restaurant("Roll'd", "R.drawable.default_icon", "");
        addRestaurantToRecyclerView(r1);
        addRestaurantToRecyclerView(r2);
        addRestaurantToRecyclerView(r3);
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
