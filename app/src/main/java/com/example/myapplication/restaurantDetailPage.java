package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Entities.User;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class restaurantDetailPage extends AppCompatActivity {

    int busId, userId;
    TextView addressTv, phoneTv, OperatingHoursTv, websiteTv, emailTv;
    Restaurant selected_restaurant;
    TextView restaurantName;
    TextView tvCuisineType;
    ImageView restaurantImage;
    TextView averageRatingTv;
    RatingBar averageRatingBar;
    TextView totalRatings;
    RatingReviews ratingReviews;
    commentAdapter commentAdapter;
    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    CRUD_Business crudBusiness;
    CRUD_Review crudReview;
    CRUD_Image crudImage;
    CRUD_User crudUser;
    TextView restaurantTags1, restaurantTags2, restaurantTags3;
    String [] filters = {"5 Stars", "4 Stars", "3 Stars", "2 Stars", "1 Star", "Staff Replied", "Owner Replied"};
    boolean[] selectedFilters;
    ArrayList<Integer> filterElements = new ArrayList<>();
    ArrayList<String[]> allTags = new ArrayList<>();
    String [] sorts = {"Likes", "Reply", "Dislike", "Time: Recent-Old", "Time: Old-Recent", "Ratings: high-low", "Ratings: low-high"};
    ArrayAdapter<String> sortAdapter;
    AutoCompleteTextView autoCompleteTextViewFilter, autoCompleteTextViewSort;
    String sortOn;
    ArrayList<ReviewModel> fullListReview = new ArrayList<>();
//    ArrayList<ReviewModel> sortedListReview = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_detail_page);

        busId = getIntent().getExtras().getInt("busId");
        userId = getIntent().getExtras().getInt("userId");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadData();

        selected_restaurant = crudBusiness.getRestaurant(busId);
        recyclerView = findViewById(R.id.commentRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        commentAdapter = new commentAdapter();
        commentAdapter.context = getApplicationContext();
        commentAdapter.setData(fullListReview);
        commentAdapter.busId = busId;
        commentAdapter.userId = userId;
        recyclerView.setAdapter(commentAdapter);

        restaurantName = findViewById(R.id.detailRestaurantName);
        tvCuisineType = findViewById(R.id.tvCuisineType);
        restaurantImage = findViewById(R.id.detailRestaurantLogo);
        averageRatingTv = findViewById(R.id.averageRating);
        averageRatingBar = findViewById(R.id.averageRatingBar);
        ratingReviews = findViewById(R.id.rating_reviews);
        totalRatings = findViewById(R.id.total_rating);
        addressTv = findViewById(R.id.addressTv);
        phoneTv = findViewById(R.id.phoneTv);
        OperatingHoursTv = findViewById(R.id.operatingHourTv);
        websiteTv = findViewById(R.id.websiteTv);
        emailTv = findViewById(R.id.emailTv);

        restaurantTags1 = findViewById(R.id.restaurantTags1);
        restaurantTags2 = findViewById(R.id.restaurantTags2);
        restaurantTags3 = findViewById(R.id.restaurantTags3);

        if(crudBusiness.getRestaurant(busId).getAddress() != null){
            addressTv.setText(crudBusiness.getRestaurant(busId).getAddress());
        }else{
            addressTv.setText(" - ");
        }

        if(crudBusiness.getRestaurant(busId).getPhone() != null){
            phoneTv.setText(crudBusiness.getRestaurant(busId).getPhone());
        }else{
            phoneTv.setText(" - ");
        }

        if(crudBusiness.getRestaurant(busId).getWebsite() != null){
            websiteTv.setText(crudBusiness.getRestaurant(busId).getWebsite());
        }else{
            websiteTv.setText(" - ");
        }

        if(crudBusiness.getRestaurant(busId).getEmail() != null){
            emailTv.setText(crudBusiness.getRestaurant(busId).getEmail());
        }else{
            emailTv.setText(" - ");
        }

        if(crudBusiness.getRestaurant(busId).getHours() != null){
            OperatingHoursTv.setText(crudBusiness.getRestaurant(busId).getHours());
        }else {
            OperatingHoursTv.setText(" - ");
        }

        if(crudImage.getBusinessImage(busId) != null){
            restaurantImage.setImageBitmap(crudImage.getBusinessImage(busId));
        }

        ArrayList<ReviewModel> reviewModels = crudReview.getReviews(busId);
        if(reviewModels != null){
            for (ReviewModel review: reviewModels) {
                if(review.getTags() != null && review.getTags().length > 0){
                    allTags.add(review.getTags());
                }
            }
        }


        Map<String, Integer> tagCountMap = new HashMap<>();

        // Count the frequency of each tag
        for (String[] tags : allTags) {
            for (String tag : tags) {
                tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
            }
        }

        // Create a list from the map entries and sort by count
        List<Map.Entry<String, Integer>> sortedTags = new ArrayList<>(tagCountMap.entrySet());
        sortedTags.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Sort in descending order

        // Retrieve the top N tags
        List<String> topTags = new ArrayList<>();
        for (int i = 0; i < Math.min(3, sortedTags.size()); i++) {
            topTags.add(sortedTags.get(i).getKey());
        }

        try {
            restaurantTags1.setText(topTags.get(0));
        }catch (Exception e){
            restaurantTags1.setVisibility(View.GONE);
        }

        try{
            restaurantTags2.setText(topTags.get(1));
        }catch (Exception e){
            restaurantTags2.setVisibility(View.GONE);
        }

        try{
            restaurantTags3.setText(topTags.get(2));
        }catch (Exception e){
            restaurantTags3.setVisibility(View.GONE);
        }


        totalRatings.setText(String.valueOf(crudReview.getReviews(busId).size()) + " Reviews");
        averageRatingTv.setText(String.valueOf(Math.round(selected_restaurant.getStars())));
        averageRatingBar.setRating(selected_restaurant.getStars());
        restaurantName.setText(selected_restaurant.getName());
        tvCuisineType.setText(selected_restaurant.getCuisine());

//        filterAdapter = new ArrayAdapter<>(this, R.layout.positions, filters);
        sortAdapter = new ArrayAdapter<>(this, R.layout.positions, sorts);

        autoCompleteTextViewFilter = findViewById(R.id.filter);
        autoCompleteTextViewSort = findViewById(R.id.sort);

//        autoCompleteTextViewFilter.setAdapter(filterAdapter);
        autoCompleteTextViewSort.setAdapter(sortAdapter);

        selectedFilters = new boolean[filters.length];

        autoCompleteTextViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the selected item
                sortOn = adapterView.getItemAtPosition(position).toString();
                filteringSelected(fullListReview);

            }
        });

        autoCompleteTextViewFilter.setOnClickListener(v ->{
            showSortDialog();
        });
//
//
//        restaurantImage.setImageResource(crudImage.get);
//        if(sortOn != null){
//            sortingSelected(sortOn);
//        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        RatingReviews ratingReviews = findViewById(R.id.rating_reviews);

//        create color pairs for rating reviews
        int color[] = new int[]{
                Color.parseColor("#ff580f"),
                Color.parseColor("#ff7e26"),
                Color.parseColor("#ff9c59"),
                Color.parseColor("#ffa472"),
                Color.parseColor("#ffc99d")};

        int oneStar = 0;
        int twoStar = 0;
        int threeStar = 0;
        int fourStar = 0;
        int fiveStar = 0;

        for (ReviewModel reviewModel: fullListReview) {
            switch ((int) reviewModel.getReviewRating()){
                case 1:
                    oneStar += 1;
                    break;
                case 2:
                    twoStar += 1;
                    break;
                case 3:
                    threeStar += 1;
                    break;
                case 4:
                    fourStar += 1;
                    break;
                case 5:
                    fiveStar += 1;
                    break;
            }
        }


// Replace this code block in your onCreate method
        int neWMax[] = new int[]{oneStar, twoStar, threeStar, fourStar, fiveStar};
        int maxRating = Arrays.stream(neWMax).max().orElse(1); // Avoid division by zero
        int raters[] = new int[5]; // Create array for 5 star ratings

// Calculate percentages for each star rating
        for (int i = 0; i < 5; i++) {
            if (neWMax[i] > 0) {
                raters[i] = (neWMax[i] * 100) / maxRating; // Calculate the percentage for each star
            } else {
                raters[i] = 0; // No reviews for this star rating
            }
        }

// Ensure the ratingReviews is correctly set up to only show 5 bars
        ratingReviews.createRatingBars(100, BarLabels.STYPE1, color, raters);


    }

    private void showSortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(restaurantDetailPage.this);
        builder.setTitle("Select Filter");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(filters, selectedFilters, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b) {
                    // If the item is selected, add the index of the filter
                    filterElements.add(i);
                } else {
                    // If the item is deselected, remove the corresponding index from filterList
                    // Check for the index that matches the selected filter's index
                    Integer filterIndex = Integer.valueOf(i); // convert the index to Integer for comparison
                    if (filterElements.contains(filterIndex)) {
                        filterElements.remove(filterIndex); // remove by object, not by index
                    }
                }
            }
        }).setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Apply</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < filterElements.size(); j++) {
                    stringBuilder.append(filters[filterElements.get(j)]);

                    if(j != filterElements.size()-1){
                        stringBuilder.append(", ");
                    }

                    filteringSelected(fullListReview);
                    autoCompleteTextViewFilter.setText(stringBuilder.toString());
                }
            }
        }).setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setNeutralButton(Html.fromHtml("<font color='#FF7F27'>Clear All</font>") , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < selectedFilters.length; j++) {
                    selectedFilters[j] = false;

                    filterElements.clear();
                    autoCompleteTextViewFilter.setText("Filter");
                }
                filteringSelected(fullListReview);
            }

        });
        builder.show();
    }

    private void loadData() {
        try{
            dbHelper = new DatabaseHelper(this);
            crudReview = new CRUD_Review(dbHelper);
            crudBusiness = new CRUD_Business(dbHelper);
            crudImage = new CRUD_Image(dbHelper);
            crudUser = new CRUD_User(dbHelper);
            fullListReview.addAll(crudReview.getReviews(busId));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void onClickCreateReview(View view){
        Intent createReviewIntent = new Intent(this, userReviewCreationPage.class);
        createReviewIntent.putExtra("busId", busId);
        createReviewIntent.putExtra("userId", userId);

        startActivity(createReviewIntent);
    }

    public void onClickBookmark(View view){
    }


    public void onResume() {

        super.onResume();
        reloadRestaurantDetails();
    }

    private void reloadRestaurantDetails() {
        try {
            fullListReview.clear();  // Clear the current list to avoid duplication
            fullListReview.addAll(crudReview.getReviews(busId));  // Reload the reviews

            // Re-fetch other data related to the restaurant if necessary
            selected_restaurant = crudBusiness.getRestaurant(busId);

            // Update the adapter with new data
            commentAdapter.setData(fullListReview);
            commentAdapter.notifyDataSetChanged();  // Notify adapter that the data has changed

            // Update other UI elements like rating bars, total ratings, etc.
            updateRatingDetails();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateRatingDetails() {
        // Update the rating and review information on the page
        totalRatings.setText(String.valueOf(crudReview.getReviews(busId).size()) + " Reviews");
        averageRatingTv.setText(String.valueOf(Math.round(selected_restaurant.getStars())));
        averageRatingBar.setRating(selected_restaurant.getStars());

        // Calculate rating distribution again
        int oneStar = 0;
        int twoStar = 0;
        int threeStar = 0;
        int fourStar = 0;
        int fiveStar = 0;

        for (ReviewModel reviewModel : fullListReview) {
            switch ((int) reviewModel.getReviewRating()) {
                case 1:
                    oneStar += 1;
                    break;
                case 2:
                    twoStar += 1;
                    break;
                case 3:
                    threeStar += 1;
                    break;
                case 4:
                    fourStar += 1;
                    break;
                case 5:
                    fiveStar += 1;
                    break;
            }
        }

        // Update the rating bars with the new data
        int color[] = new int[]{
                Color.parseColor("#ff580f"),
                Color.parseColor("#ff7e26"),
                Color.parseColor("#ff9c59"),
                Color.parseColor("#ffa472"),
                Color.parseColor("#ffc99d")
        };

        int neWMax[] = new int[]{oneStar, twoStar, threeStar, fourStar, fiveStar};
        int maxRating = Arrays.stream(neWMax).max().orElse(1); // Avoid division by zero
        int raters[] = new int[5];

        for (int i = 0; i < 5; i++) {
            raters[i] = (neWMax[i] * 100) / maxRating;
        }

        // Update the rating bars in the UI
        ratingReviews.createRatingBars(100, BarLabels.STYPE1, color, raters);
    }


    private void filteringSelected(ArrayList<ReviewModel> myList) {
        // Use a HashSet to avoid duplicate reviews
        HashSet<ReviewModel> filteredSet = new HashSet<>();

        if (!filterElements.isEmpty()) {
            for (int i = 0; i < selectedFilters.length; i++) {
                if (selectedFilters[i]) {
                    switch (filters[i]) {
                        case ("5 Stars"):
                            for (ReviewModel review : myList) {
                                if (review.getReviewRating() == 5) {
                                    filteredSet.add(review); // HashSet will automatically handle duplicates
                                }
                            }
                            break;

                        case ("4 Stars"):
                            for (ReviewModel review : myList) {
                                if (review.getReviewRating() == 4) {
                                    filteredSet.add(review);
                                }
                            }
                            break;

                        case ("3 Stars"):
                            for (ReviewModel review : myList) {
                                if (review.getReviewRating() == 3) {
                                    filteredSet.add(review);
                                }
                            }
                            break;

                        case ("2 Stars"):
                            for (ReviewModel review : myList) {
                                if (review.getReviewRating() == 2) {
                                    filteredSet.add(review);
                                }
                            }
                            break;

                        case ("1 Star"):
                            for (ReviewModel review : myList) {
                                if (review.getReviewRating() == 1) {
                                    filteredSet.add(review);
                                }
                            }
                            break;

                        case ("Staff Replied"):
                            for (ReviewModel review : myList) {
                                ArrayList<Response> responses = crudReview.getResponses(review.getReviewId());
                                for (Response response : responses) {
                                    Position userPosition = crudUser.getUserPoistion(response.getUserID());
                                    if (userPosition != null) {
                                        filteredSet.add(review);
                                        break;
                                    }
                                }
                            }
                            break;

                        case ("Owner Replied"):
                            for (ReviewModel review : myList) {
                                ArrayList<Response> responses = crudReview.getResponses(review.getReviewId());
                                for (Response response : responses) {
                                    Position userPosition = crudUser.getUserPoistion(response.getUserID());
                                    if (userPosition != null && userPosition.getBusinessID() == busId && userPosition.getPositionName().equalsIgnoreCase("Owner")) {
                                        filteredSet.add(review);
                                        break;
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        } else {
            filteredSet.addAll(fullListReview); // If no filters are selected, show the full list
        }

        ArrayList<ReviewModel> filteredList = new ArrayList<>(filteredSet); // Convert HashSet to ArrayList

        if (sortOn != null) {
            sortingSelected(sortOn, filteredList);
        } else {
            commentAdapter.setFilteredList(filteredList); // Update adapter with the filtered list
        }
    }


    private void sortingSelected(String selectedSort, ArrayList<ReviewModel> myList){
        switch(selectedSort){
            case "Likes":
                Collections.sort(myList, ReviewModel.likesAscending);
                commentAdapter.setFilteredList(myList);
                break;

            case "Reply":
                Collections.sort(myList, ReviewModel.replyAscending);
                commentAdapter.setFilteredList(myList);
                break;

            case "Dislike":
                Collections.sort(myList, ReviewModel.dislikesAscending);
                commentAdapter.setFilteredList(myList);
                break;

            case "Time: Recent-Old":
                Collections.sort(myList, ReviewModel.timeAscending);
                commentAdapter.setFilteredList(myList);
                break;

            case "Time: Old-Recent":
                Collections.sort(myList, ReviewModel.timeAscending);
                Collections.reverse(myList);
                commentAdapter.setFilteredList(myList);
                break;

            case "Ratings: high-low":
                Collections.sort(myList, ReviewModel.ratingAscending);
                commentAdapter.setFilteredList(myList);
                break;

            case "Ratings: low-high":
                Collections.sort(myList, ReviewModel.ratingAscending);
                Collections.reverse(myList);
                commentAdapter.setFilteredList(myList);
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}