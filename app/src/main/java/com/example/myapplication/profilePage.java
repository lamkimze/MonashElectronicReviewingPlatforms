package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;

import com.example.myapplication.Entities.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class profilePage extends DrawerBaseActivity {
    ImageView imageView;
    TextView textView;
    TextView email;
    int userId;
    ArrayList<ReviewModel> reviewModels = new ArrayList<>();
    CRUD_Business businessCrud = new CRUD_Business(new DatabaseHelper(this));
    CRUD_User userCrud = new CRUD_User(new DatabaseHelper(this));
    CRUD_Review reviewCrud = new CRUD_Review(new DatabaseHelper(this));
    User activeUser;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_profile_page);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        userId = getIntent().getExtras().getInt("userId");
//        RecyclerView recyclerView = findViewById(R.id.userRecyclerView);
//        imageView = (ImageView) findViewById(R.id.imageView2);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_icon);
//        Bitmap scakedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
//        Drawable userDrawable = new BitmapDrawable(getResources(), scakedBitmap);
//        imageView.setImageDrawable(userDrawable);
//
//        activeUser = userCrud.getUser(userId);
//
//
//        textView = (TextView) findViewById(R.id.textView19);
//        reviewModels = reviewCrud.getReviews(userCrud.getUserID(activeUser));
//        commentAdapter adapter = new commentAdapter();
//        adapter.context = getApplicationContext();
//        adapter.setData(reviewModels);
//        adapter.userId = userId;
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        textView.setText(activeUser.getUsername());
//
//        email = (TextView) findViewById(R.id.userProfileEmailAddress);
//        email.setText(activeUser.getEmail());
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view of the DrawerBaseActivity
        setContentView(R.layout.activity_drawer_base);

        // Inflate the profile page layout into the FrameLayout
        FrameLayout contentFrame = findViewById(R.id.activityContainer); // Use activityContainer ID
        getLayoutInflater().inflate(R.layout.activity_profile_page, contentFrame, true);

        // Initialize views after the layout has been set
        imageView = findViewById(R.id.imageView2); // Initialize ImageView
        textView = findViewById(R.id.textView19);  // Initialize TextView
        email = findViewById(R.id.userProfileEmailAddress); // Initialize EditText
        userId = getIntent().getExtras().getInt("userId");

        // Initialize views and functionality for profile page
        setupProfilePage();
    }

    private void setupProfilePage() {
        imageView = findViewById(R.id.imageView2); // This should not be null
        if (imageView == null) {
            Log.e("ProfilePage", "imageView is null");
        }
        activeUser = userCrud.getUser(userId); // Get active user once
        Bitmap bitmap = activeUser.getProfilePicture();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        Drawable userDrawable = new BitmapDrawable(getResources(), scaledBitmap);
        imageView.setImageDrawable(userDrawable);

        if (activeUser != null) {
            // Only set text if the user exists
            textView = findViewById(R.id.textView19); // Use class variable
            textView.setText(activeUser.getUsername());

            email = findViewById(R.id.userProfileEmailAddress); // Use class variable
            email.setText(activeUser.getEmail());
        } else {
            // Handle the case where the user is not found
            textView = findViewById(R.id.textView19); // Use class variable
            textView.setText("User not found");

            email = findViewById(R.id.userProfileEmailAddress); // Use class variable
            email.setText("No email available");
        }

        // Setup RecyclerView for displaying user reviews
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.userRecyclerView);
        reviewModels = reviewCrud.getReviews(userId);
        commentAdapter adapter = new commentAdapter();
        adapter.context = getApplicationContext();
        adapter.setData(reviewModels);
        adapter.userId = userId;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        R_RecyclerViewAdapter adapter = new R_RecyclerViewAdapter(this, reviewModels);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}