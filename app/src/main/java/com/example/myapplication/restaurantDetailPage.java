package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.Range;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.DatabaseHelper;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class restaurantDetailPage extends AppCompatActivity {

    int busId, userId;
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
    String [] filters = {"5 Stars", "4 Stars", "3 Stars", "2 Stars", "1 Star", "Staff Replied"};
    String [] sorts = {"Likes", "Reply", "Dislike", "Time: Recent-Old", "Time: Old-Recent", "Ratings: high-low", "Ratings: low-high"};
    ArrayAdapter<String> filterAdapter;
    ArrayAdapter<String> sortAdapter;
    AutoCompleteTextView autoCompleteTextViewFilter, autoCompleteTextViewSort;
    String sortOn;
    ArrayList listReview = new ArrayList();
    ArrayList<String> filterOn = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_detail_page);

        busId = getIntent().getExtras().getInt("busId");
        userId = getIntent().getExtras().getInt("userID");


        loadData();
        selected_restaurant = crudBusiness.getRestaurant(busId);
        recyclerView = findViewById(R.id.commentRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        commentAdapter = new commentAdapter();
        commentAdapter.context = getApplicationContext();
        commentAdapter.setData(listReview);
        commentAdapter.busId = busId;
        recyclerView.setAdapter(commentAdapter);

        restaurantName = findViewById(R.id.detailRestaurantName);
        tvCuisineType = findViewById(R.id.tvCuisineType);
        restaurantImage = findViewById(R.id.detailRestaurantLogo);
        averageRatingTv = findViewById(R.id.averageRating);
        averageRatingBar = findViewById(R.id.averageRatingBar);
        ratingReviews = findViewById(R.id.rating_reviews);
        totalRatings = findViewById(R.id.total_rating);

        totalRatings.setText(String.valueOf(crudReview.getReviews(busId).size()));
        averageRatingTv.setText(String.valueOf(selected_restaurant.getStars()));
        averageRatingBar.setRating(selected_restaurant.getStars());
        restaurantName.setText(selected_restaurant.getName());
        tvCuisineType.setText(selected_restaurant.getCuisine());

        filterAdapter = new ArrayAdapter<>(this, R.layout.positions, filters);
        sortAdapter = new ArrayAdapter<>(this, R.layout.positions, sorts);

        autoCompleteTextViewFilter = findViewById(R.id.filter);
        autoCompleteTextViewSort = findViewById(R.id.sort);

        autoCompleteTextViewFilter.setAdapter(filterAdapter);
        autoCompleteTextViewSort.setAdapter(sortAdapter);

        autoCompleteTextViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the selected item
                sortOn = adapterView.getItemAtPosition(position).toString();
                sortingSelected(sortOn);
                Log.e("Sort On", sortOn);
            }
        });

//        autoCompleteTextViewFilter.setOnClickListener(new AdapterView.OnItemClickListener(){
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                filterOn =
//            }
//        });
//
//
//        restaurantImage.setImageResource(crudImage.get);
        if(sortOn != null){
            sortingSelected(sortOn);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        RatingReviews ratingReviews = findViewById(R.id.rating_reviews);

//        create color pairs for rating reviews
        Pair color[] = new Pair[]{
                new Pair<>(Color.parseColor("#0e9d58"), Color.parseColor("#1e88e5")),
                new Pair<>(Color.parseColor("#bfd047"), Color.parseColor("#5c6bc0")),
                new Pair<>(Color.parseColor("#ffc105"), Color.parseColor("#d81b68")),
                new Pair<>(Color.parseColor("#ef7e14"), Color.parseColor("#8bc34a")),
                new Pair<>(Color.parseColor("#d36259"), Color.parseColor("#ea80fc")),
        };

        int minValue = 30;

//        create raters array
        int raters[] = new int[]{
                minValue + new Random().nextInt(100 - minValue + 1),
                minValue + new Random().nextInt(100 - minValue + 1),
                minValue + new Random().nextInt(100 - minValue + 1),
                minValue + new Random().nextInt(100 - minValue + 1),
                minValue + new Random().nextInt(100 - minValue + 1)
        };

        ratingReviews.createRatingBars(100, BarLabels.STYPE1, color, raters);
    }

    private void loadData() {
        try{
            dbHelper = new DatabaseHelper(this);
            crudReview = new CRUD_Review(dbHelper);
            crudBusiness = new CRUD_Business(dbHelper);
            crudImage = new CRUD_Image(dbHelper);
            listReview.addAll(crudReview.getReviews(busId));
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

    private void sortingSelected(String selectedSort){
        switch(selectedSort){
            case "Likes":
                Collections.sort(listReview, ReviewModel.likesAscending);
                commentAdapter.notifyDataSetChanged();
                break;

            case "Reply":
                Collections.sort(listReview, ReviewModel.replyAscending);
                commentAdapter.notifyDataSetChanged();
                break;

            case "Dislike":
                Collections.sort(listReview, ReviewModel.dislikesAscending);
                commentAdapter.notifyDataSetChanged();
                break;

            case "Time: Recent-Old":
                Collections.sort(listReview, ReviewModel.timeAscending);
                commentAdapter.notifyDataSetChanged();
                break;

            case "Time: Old-Recent":
                Collections.sort(listReview, ReviewModel.timeAscending);
                Collections.reverse(listReview);
                commentAdapter.notifyDataSetChanged();
                break;

            case "Ratings: high-low":
                Collections.sort(listReview, ReviewModel.ratingAscending);
                commentAdapter.notifyDataSetChanged();
                break;

            case "Ratings: low-high":
                Collections.sort(listReview, ReviewModel.ratingAscending);
                Collections.reverse(listReview);
                commentAdapter.notifyDataSetChanged();
                break;
        }
    }


}