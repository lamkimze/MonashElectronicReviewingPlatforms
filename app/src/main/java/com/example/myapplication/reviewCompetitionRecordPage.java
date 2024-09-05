package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.fragment.NavHostFragment;

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
    private RestaurantViewModel viewModel;
    private SearchView searchView;
    private Button sortButton;
    private Button filterButton;
    private LinearLayout sortView;
    private LinearLayout filterView1;
    private LinearLayout filterView2;
    private LinearLayout filterView3;
    boolean sortHidden = true;
    boolean filterHidden = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowCompat.setDecorFitsSystemWindows(this.getWindow(), false);
        super.onCreate(savedInstanceState);
        activityReviewCompetitionRecordPageBinding = ActivityReviewCompetitionRecordPageBinding.inflate(this.getLayoutInflater());
        allocateActivityTitle("Review Competition Records");
        setContentView(activityReviewCompetitionRecordPageBinding.getRoot());

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
        initWidgets();

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new CompetitionRecyclerAdapter();
        recyclerAdapter.setData(listRestaurant);
        recyclerView.setAdapter(recyclerAdapter);

        hideFilter();
        hideSort();

    }

    private void initWidgets(){
        sortButton = (Button) findViewById(R.id.sortButton);
        filterButton = (Button) findViewById(R.id.filterButton);
        filterView1 = (LinearLayout) findViewById(R.id.filterTabsLayout1);
        filterView2 = (LinearLayout) findViewById(R.id.filterTabsLayout2);
        filterView3 = (LinearLayout) findViewById(R.id.filterTabsLayout3);
        sortView = (LinearLayout) findViewById(R.id.sortTabsLayout);
    }

    public void showFilterTapped(View view){
        if(filterHidden == true){
            filterHidden = false;
            showFilter();
        }else{
            filterHidden = true;
            hideFilter();
        }
    }

    public void showSortTapped(View view){
        if(sortHidden == true){
            sortHidden = false;
            showSort();
        }else{
            sortHidden = true;
            hideSort();
        }
    }

    private void hideFilter(){
        filterView1.setVisibility(View.GONE);
        filterView2.setVisibility(View.GONE);
        filterView3.setVisibility(View.GONE);
        filterButton.setText("FILTER");
    }

    private void showFilter(){
        filterView1.setVisibility(View.VISIBLE);
        filterView2.setVisibility(View.VISIBLE);
        filterView3.setVisibility(View.VISIBLE);
        filterButton.setText("HIDE");
    }

    private void hideSort(){
        sortView.setVisibility(View.GONE);
        sortButton.setText("SORT");
    }

    private void showSort(){
        sortView.setVisibility(View.VISIBLE);
        sortButton.setText("HIDE");
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

    public void setFilterData(View view){
    }
}
