package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.databinding.ActivityDashboardPageBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class dashboardPage extends DrawerBaseActivity {

    ActivityDashboardPageBinding activityDashboardPageBinding;
    ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    int userId;
    private RecyclerView recyclerView;
    private dashboardReviewCardAdapter adapter;
    private ArrayList<dashboardReviewCard> reviewArrayList;
    private ArrayList<ReviewModel> reviewLatestFiveList;


    DatabaseHelper dbHelper;
    CRUD_Business crudBusiness;
    CRUD_User crudUser;
    CRUD_Image crudImage;
    CRUD_Review crudReview;

    ArrayList<Restaurant> top5 = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardPageBinding = ActivityDashboardPageBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardPageBinding.getRoot());
        allocateActivityTitle("Electronic Review Platform");
        userId = getIntent().getExtras().getInt("userId");

        onPostCreate(savedInstanceState);
        viewPager2 = findViewById(R.id.viewPager);
        List<SlideItem> slideItems = new ArrayList<>();
        slideItems.add(new SlideItem(R.drawable.gyg_banner1));
        slideItems.add(new SlideItem(R.drawable.gyg_banner3));
        slideItems.add(new SlideItem(R.drawable.gyg_banner4));
        slideItems.add(new SlideItem(R.drawable.gyg_banner5));

        viewPager2.setAdapter(new SlideAdapter(slideItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(5);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1- Math.abs(position);
                page.setScaleY(0.85f + r*0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);

                slideHandler.removeCallbacks(slideRunnable);
                slideHandler.postDelayed(slideRunnable, 3000);
            }
        });
        initializeDatabase();

        initializeCardView();


    }

    private void initializeCardView() {
        recyclerView = findViewById(R.id.reviewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewArrayList = new ArrayList<>();

        adapter = new dashboardReviewCardAdapter(this, reviewArrayList);
        recyclerView.setAdapter(adapter);

        CreateDataForCards();

    }


    private void CreateDataForCards() {
        reviewLatestFiveList = crudReview.getLatestReviews();
        for (ReviewModel review : reviewLatestFiveList) {
            Bitmap profilePic = crudImage.getProfilePictureByUserId(review.getReviewerId());
            ArrayList<Bitmap> reviewImages = crudImage.getReviewImages(review.getReviewId());
            dashboardReviewCard reviewCard = new dashboardReviewCard(review.getReviewTitle(), review.getReviewRating(), new ArrayList<Bitmap>(), review.getReviewText(), review.getReviewerId(), review.getBusinessId(), review.getReviewId(), profilePic, crudReview.getReviewDate(review.getReviewId()), crudUser.getFullName(review.getReviewerId()));
//            dashboardReviewCard reviewCard = new dashboardReviewCard("review.getReviewTitle()", 3f, new ArrayList<Bitmap>(), "review.getReviewText()", 1, 2, 3, BitmapFactory.decodeResource(getResources(), R.drawable.guzmanygomez), "2020-10-2", "Jamie");
            reviewArrayList.add(reviewCard);
        }
        dashboardReviewCard review = new dashboardReviewCard("Great food", 4.5f, new ArrayList<Bitmap>(), "The food was amazing, I would definitely recommend this place to anyone who loves good food", 1, 1, 1, BitmapFactory.decodeResource(getResources(), R.drawable.guzmanygomez), "2021-10-10", "John Doe");
        reviewArrayList.add(review);
        reviewArrayList.add(review);
    }


    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(slideRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(slideRunnable, 3000);
    }

    public void onClickCompetitionReview(View view){
        Intent reviewCompetition = new Intent(this, reviewCompetitionRecordPage.class);
        reviewCompetition.putExtra("userId", userId);
        startActivity(reviewCompetition);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    private void initializeDatabase() {

        try {
            dbHelper = new DatabaseHelper(this);
            crudBusiness = new CRUD_Business(dbHelper);
            crudUser = new CRUD_User(dbHelper);
            crudImage = new CRUD_Image(dbHelper);
            crudReview = new CRUD_Review(dbHelper);
            ArrayList<Restaurant> dashboardRestaurantsTop5 = crudBusiness.getTop5RatedBusinesses();
            top5.addAll(dashboardRestaurantsTop5);
//            if (crudReview != null) {
//                ArrayList<ReviewModel> crudReviewLatestReviews = crudReview.getLatestReviews();
//            } else {
//                // Log an error or handle the null case
//                Log.e("dashboardPage", "CRUD_Review is null");
//            }

            // Notify success on the main thread
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(this, "Database initialized successfully!", Toast.LENGTH_LONG).show()
            );
        } catch (Exception e) {
            e.printStackTrace();

            // Notify failure on the main thread
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(this, "Database initialization failed!", Toast.LENGTH_LONG).show()
            );
        }
    }




}