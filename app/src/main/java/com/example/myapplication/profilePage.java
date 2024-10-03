package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;

import com.example.myapplication.Entities.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class profilePage extends DrawerBaseActivity {
    int userId;
    ArrayList<ReviewModel> reviewModels;
    ArrayList<dashboardReviewCard> reviewCardArrayList;
    DatabaseHelper databaseHelper;
    CRUD_Business businessCrud;
    CRUD_User userCrud;
    CRUD_Review reviewCrud;
    CRUD_Image crudImage;
    User activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_base);

        allocateActivityTitle("Electronic Review Platform"); // Set the title for the action bar

        // Log to verify the lifecycle
        Log.d("profilePage", "onCreate called");

        try {
            // Initialize DatabaseHelper and CRUD objects
            databaseHelper = new DatabaseHelper(this);
            businessCrud = new CRUD_Business(databaseHelper);
            userCrud = new CRUD_User(databaseHelper);
            reviewCrud = new CRUD_Review(databaseHelper);
            crudImage = new CRUD_Image(databaseHelper);

            // Log initialization success
            Log.d("profilePage", "Database and CRUD objects initialized successfully.");
        } catch (Exception e) {
            // Log any errors during initialization
            Log.e("profilePage", "Error initializing database or CRUD objects", e);
        }

        // Inflate the profile page layout into the FrameLayout
        FrameLayout contentFrame = findViewById(R.id.activityContainer); // Use activityContainer ID
        getLayoutInflater().inflate(R.layout.activity_profile_page, contentFrame, true);

        // Get userId from the intent
        userId = getIntent().getExtras().getInt("userId");

        // Check if userCrud was initialized correctly
        if (userCrud == null) {
            Log.e("profilePage", "userCrud is null. Cannot proceed.");
            return;  // Exit early if userCrud is null to prevent a crash
        }

        // Initialize views and functionality for the profile page
        setupProfilePage();
    }

    private void setupProfilePage() {
        ImageView imageView = findViewById(R.id.imageView2); // Initialize ImageView
        TextView userName = findViewById(R.id.userProfileUserName);
        TextView userEmail = findViewById(R.id.userProfileEmailAddress); // Initialize EditText
        TextView name = findViewById(R.id.editTextName);
        ImageView restaurantPic = findViewById(R.id.imageView3);
        TextView restaurantName = findViewById(R.id.editTextUserName);

        activeUser = userCrud.getUser(userId); // Get active user once

        if (activeUser != null) {

            // Only set text if the user exists
            userName.setText(activeUser.getUsername());
            userEmail.setText(activeUser.getEmail());
            name.setText(activeUser.getFirstName() + " " + activeUser.getLastName());

            if (activeUser.getProfilePicture() == null) {
                imageView.setImageResource(R.drawable.person_icon);
            }else{
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(activeUser.getProfilePicture(), 200, 200, true);
                Drawable userDrawable = new BitmapDrawable(getResources(), scaledBitmap);
                imageView.setImageDrawable(userDrawable);
            }

            Position position = userCrud.getUserPoistion(userId);
            if(position != null){
                restaurantName.setText(businessCrud.getRestaurant(position.getBusinessID()).getName());
                restaurantPic.setImageBitmap(crudImage.getBusinessImage(position.getBusinessID()));
            }else{
                restaurantName.setVisibility(View.GONE);
                restaurantPic.setVisibility(View.GONE);
            }
        }

        // Setup RecyclerView for displaying user reviews
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewCardArrayList = new ArrayList<>();
        dashboardReviewCardAdapter adapter = new dashboardReviewCardAdapter(this, reviewCardArrayList, userId);
        recyclerView.setAdapter(adapter);


       reviewModels = reviewCrud.getReviewsByUserId(userCrud.getUserID(activeUser));
        for (ReviewModel review : reviewModels) {
            Bitmap profilePic = crudImage.getProfilePictureByUserId(review.getReviewerId());
            ArrayList<Bitmap> reviewImages = crudImage.getReviewImages(review.getReviewId());
            dashboardReviewCard reviewCard = new dashboardReviewCard(review.getReviewTitle(), review.getReviewRating(), reviewImages, review.getReviewText(), review.getReviewerId(), review.getBusinessId(), review.getReviewId(), profilePic, reviewCrud.getReviewDate(review.getReviewId()), userCrud.getFullName(review.getReviewerId()), businessCrud.getBusinessNameById(review.getBusinessId()), review.getLikes(), review.getDislike());
            reviewCardArrayList.add(reviewCard);
        }
    }

}