package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;

import java.util.ArrayList;
import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Entities.User;

public class restaurantOwnerProfilePage extends AppCompatActivity {

    TextView textView;
    RatingBar ratingBar;
    TextView email;
    ImageView imageView;
    Restaurant restaurant;


    ArrayList<ReviewModel> reviewModels = new ArrayList<>();
    CRUD_Business businessCrud = new CRUD_Business(new DatabaseHelper(this));
    CRUD_User userCrud = new CRUD_User(new DatabaseHelper(this));
    CRUD_Review reviewCrud = new CRUD_Review(new DatabaseHelper(this));
    User activeUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_owner_profile_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView recyclerView = findViewById(R.id.ownerRecyclerView);
        imageView = (ImageView) findViewById(R.id.restaurantOwnerPicture);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_icon);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
        Drawable userDrawable = new BitmapDrawable(getResources(), scaledBitmap);
        imageView.setImageDrawable(userDrawable);
        activeUser = userCrud.getUser(10011111);

        textView = (TextView) findViewById(R.id.restaurantTitle);
        restaurant = businessCrud.getRestaurant(userCrud.getUserID(activeUser));
        reviewModels = reviewCrud.getReviews(userCrud.getUserID(activeUser));
        R_RecyclerViewAdapter adapter = new R_RecyclerViewAdapter(this,reviewModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textView.setText(restaurant.getName());


        ratingBar = (RatingBar) findViewById(R.id.restaurantAverageRatingBar);
        ratingBar.setRating(restaurant.getStars());

        email = (TextView) findViewById(R.id.ownerEmail);
        email.setText(activeUser.getEmail());
    }


}