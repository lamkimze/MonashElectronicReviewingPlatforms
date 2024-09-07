package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.databinding.ActivityReviewCompetitionRecordPageBinding;

import java.util.ArrayList;

public class reviewCompetitionRecordPage extends DrawerBaseActivity {

    ActivityReviewCompetitionRecordPageBinding activityReviewCompetitionRecordPageBinding;

    ArrayList<Restaurant> listRestaurant = new ArrayList<>();

    DatabaseHelper dbHelper;
    CompetitionRecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private RestaurantViewModel viewModel;
    private Button sortButton;
    private Button filterButton;
    private LinearLayout sortView;
    private LinearLayout filterView1;
    private LinearLayout filterView2;
    private LinearLayout filterView3;
    boolean sortHidden = true;
    boolean filterHidden = true;
    private final ArrayList<String> selectedFilter = new ArrayList<>();
    String currentSearchText = "";

    private boolean westernBool, southEastAsianBool, asianBool, cafe_beverageBool, otherBool = false;
    private Button all, western, southEastAsian, asian, cafe_beverage, others;
    private Button nameAsc, medalAsc, ratingAsc;
    private int blue, red, white, brown;

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

        SearchView searchView = findViewById(R.id.searchView);
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
        initColors();
        lookSelected(nameAsc);
        lookSelected(all);
        selectedFilter.add("All");
    }

    private void initColors(){
        blue = ContextCompat.getColor(getApplicationContext(), R.color.blue);
        red = ContextCompat.getColor(getApplicationContext(), R.color.red);
        white = ContextCompat.getColor(getApplicationContext(), R.color.white);
        brown = ContextCompat.getColor(getApplicationContext(), R.color.brown);

    }

    private void lookSelected(Button parsedButton){
        parsedButton.setTextColor(blue);
        parsedButton.setBackgroundColor(red);
    }

    private void unselectedAllSorts(){
        lookSortUnSelected(nameAsc);
        lookSortUnSelected(medalAsc);
        lookSortUnSelected(ratingAsc);
    }


    private void unselectedAllFilters(){
        lookFilterUnSelected(all);
        lookFilterUnSelected(western);
        lookFilterUnSelected(southEastAsian);
        lookFilterUnSelected(asian);
        lookFilterUnSelected(cafe_beverage);
        lookFilterUnSelected(others);

        selectedFilter.clear();
        westernBool = asianBool = southEastAsianBool = cafe_beverageBool = otherBool = false;
    }

    private void lookSortUnSelected(Button parsedButton){
        parsedButton.setTextColor(white);
        parsedButton.setBackgroundColor(blue);
    }

    private void lookFilterUnSelected(Button parsedButton){
        parsedButton.setTextColor(white);
        parsedButton.setBackgroundColor(brown);
    }

    private void initWidgets(){
        sortButton = (Button) findViewById(R.id.sortButton);
        filterButton = (Button) findViewById(R.id.filterButton);
        filterView1 = (LinearLayout) findViewById(R.id.filterTabsLayout1);
        filterView2 = (LinearLayout) findViewById(R.id.filterTabsLayout2);
        sortView = (LinearLayout) findViewById(R.id.sortTabsLayout);

        all = findViewById(R.id.all);
        western = findViewById(R.id.western);
        southEastAsian = findViewById(R.id.southeastasian);
        asian = findViewById(R.id.asian);
        cafe_beverage = findViewById(R.id.cafe_beverage);
        others = findViewById(R.id.others);

        nameAsc = findViewById(R.id.NameSort);
        medalAsc = findViewById(R.id.MedalSort);
        ratingAsc = findViewById(R.id.RatingSort);
    }

    public void showFilterTapped(View view){
        if(filterHidden){
            filterHidden = false;
            showFilter();
        }else{
            filterHidden = true;
            hideFilter();
        }
    }

    public void showSortTapped(View view){
        if(sortHidden){
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
        filterButton.setText("FILTER");
    }

    private void showFilter(){
        filterView1.setVisibility(View.VISIBLE);
        filterView2.setVisibility(View.VISIBLE);
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
        Log.e("query filter", selectedFilter.toString());
        currentSearchText = text;
        ArrayList<Restaurant> cuisineSelected = new ArrayList<>();
        for (Restaurant restaurant : listRestaurant) {
            Log.e("Filter Selected", selectedFilter.toString());
            if (restaurant.getName().toLowerCase().contains(text.toLowerCase())) {
                if (selectedFilter.contains("All")) {
                    cuisineSelected.add(restaurant);
                } else {
                    if(selectedFilter.contains(restaurant.getCuisine())){
                        cuisineSelected.add(restaurant);
                    }
                }
            }
        }
        recyclerAdapter.setFilteredList(cuisineSelected);
        if(cuisineSelected.isEmpty()){
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkForFilter() {
        if(selectedFilter.contains("All")){
            if(currentSearchText.isEmpty()){
                recyclerAdapter.setFilteredList(listRestaurant);
            }
            else{
                ArrayList<Restaurant> filteredRestaurant = new ArrayList<>();
                for(Restaurant restaurant: listRestaurant){
                    if(restaurant.getName().toLowerCase().contains(currentSearchText.toLowerCase())){
                        filteredRestaurant.add(restaurant);
                    }
                }
                recyclerAdapter.setFilteredList(filteredRestaurant);
            }
        }
        else{
            filterCuisine(null);
        }
    }

    private void filterCuisine(String status) {

        if(status != null && !selectedFilter.contains(status)){
            selectedFilter.add(status);

            ArrayList<Restaurant> filteredRestaurants = new ArrayList<>();
            for(Restaurant restaurant: listRestaurant){
                for(String filter: selectedFilter){
                    if(restaurant.getCuisine().equalsIgnoreCase(filter) || selectedFilter.contains("All")){
                        if(currentSearchText.isEmpty()){
                            filteredRestaurants.add(restaurant);
                        }
                        else{
                            if(restaurant.getName().toLowerCase().contains(currentSearchText.toLowerCase())){
                                filteredRestaurants.add(restaurant);
                            }
                        }
                    }
                }
            }
            recyclerAdapter.setFilteredList(filteredRestaurants);
        }

    }

    public void removeCuisine(){
        ArrayList<Restaurant> filteredRestaurants = new ArrayList<>();
        for(Restaurant restaurant: listRestaurant){
            for(String filter: selectedFilter){
                if(restaurant.getCuisine().equalsIgnoreCase(filter)){
                    if(currentSearchText.isEmpty()){
                        filteredRestaurants.add(restaurant);
                    }
                    else{
                        if(restaurant.getName().toLowerCase().contains(currentSearchText.toLowerCase())){
                            filteredRestaurants.add(restaurant);
                        }
                    }
                }
            }
        }
        recyclerAdapter.setFilteredList(filteredRestaurants);
    }

    public void medalASCTapped(View view){
        listRestaurant.sort(Restaurant.medalAscending);
        checkForFilter();
        unselectedAllSorts();
        lookSelected(medalAsc);
    }

    public void nameASCTapped(View view){
        listRestaurant.sort(Restaurant.nameAscending);
        checkForFilter();
        unselectedAllSorts();
        lookSelected(nameAsc);
    }

    public void ratingASCTapped(View view){
        listRestaurant.sort(Restaurant.ratingAscending);
        checkForFilter();
        unselectedAllSorts();
        lookSelected(ratingAsc);
    }


    public void allFilterTap(View view){
        unselectedAllFilters();
        filterCuisine("All");
        lookSelected(all);
    }

    public void westernFilterTap(View view){
//        Australian, Italian, British, German, American
        if(!westernBool){
            selectedFilter.remove("All");
            filterCuisine("Australian");
            filterCuisine("Italian");
            filterCuisine("British");
            filterCuisine("German");
            filterCuisine("American");
            lookSelected(western);
            lookFilterUnSelected(all);
            westernBool = true;
        }
        else{
            westernBool = false;
            selectedFilter.remove("Australian");
            selectedFilter.remove("Italian");
            selectedFilter.remove("British");
            selectedFilter.remove("German");
            selectedFilter.remove("American");
            lookFilterUnSelected(western);
            removeCuisine();
            checkNothingSelected();
        }


    }
    public void southeastAsianFilterTap(View view){
//        Malasian, Vietnamese
        if(!southEastAsianBool){
            selectedFilter.remove("All");
            filterCuisine("Malaysian");
            filterCuisine("Vietnamese");
            lookSelected(southEastAsian);
            lookFilterUnSelected(all);
            southEastAsianBool = true;
        }
        else{
            southEastAsianBool = false;
            selectedFilter.remove("Malaysian");
            selectedFilter.remove("Vietnamese");
            lookFilterUnSelected(southEastAsian);
            removeCuisine();
            checkNothingSelected();
        }

    }
    public void asianFilterTap(View view){
//        Chinese, Japanese
        if(!asianBool) {
            selectedFilter.remove("All");
            filterCuisine("Chinese");
            filterCuisine("Japanese");
            lookSelected(asian);
            lookFilterUnSelected(all);
            asianBool = true;
        }
        else{
            asianBool = false;
            selectedFilter.remove("Chinese");
            selectedFilter.remove("Japanese");
            lookFilterUnSelected(asian);
            removeCuisine();
            checkNothingSelected();
        }
    }
    public void cafeBeverageFilterTap(View view){
//        Beverage, Cafe, Bar
        if(!cafe_beverageBool){
            selectedFilter.remove("All");
            filterCuisine("Beverage");
            filterCuisine("Cafe");
            filterCuisine("Bar");
            lookSelected(cafe_beverage);
            lookFilterUnSelected(all);
            cafe_beverageBool = true;

        }else{
            cafe_beverageBool = false;
            selectedFilter.remove("Beverage");
            selectedFilter.remove("Cafe");
            selectedFilter.remove("Bar");
            lookFilterUnSelected(cafe_beverage);
            removeCuisine();
            checkNothingSelected();
        }
    }
    public void otherFilterTap(View view){
//        International, Mexican, Portuguese, Vegetarian, Convenience Store
        if(!otherBool){
            selectedFilter.remove("All");
            filterCuisine("International");
            filterCuisine("Mexican");
            filterCuisine("Portuguese");
            filterCuisine("Vegetarian");
            filterCuisine("Convenience Store");
            lookSelected(others);
            lookFilterUnSelected(all);
            otherBool = true;
        }else{
            otherBool = false;
            selectedFilter.remove("International");
            selectedFilter.remove("Mexican");
            selectedFilter.remove("Portuguese");
            selectedFilter.remove("Vegetarian");
            selectedFilter.remove("Convenience Store");
            lookFilterUnSelected(others);
            removeCuisine();
            checkNothingSelected();
        }

    }

    public void checkNothingSelected(){
        if(selectedFilter.isEmpty()){
            unselectedAllFilters();
            filterCuisine("All");
            lookSelected(all);
        }

    }
}
