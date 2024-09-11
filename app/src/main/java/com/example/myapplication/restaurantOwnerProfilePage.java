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

import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Entities.Owner;

import java.util.ArrayList;
import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Review;

public class restaurantOwnerProfilePage extends AppCompatActivity {

    TextView textView;
    RatingBar ratingBar;
    ImageView imageView;
    ArrayList<ReviewModel> reviewModels = new ArrayList<>();

    CRUD_User userCrud = new CRUD_User(new DatabaseHelper(this));
    Owner activeUser;


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
        imageView = (ImageView) findViewById(R.id.restaurantOwnerPicture);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_icon);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
        Drawable userDrawable = new BitmapDrawable(getResources(), scaledBitmap);
        imageView.setImageDrawable(userDrawable);
        //activeUser = userCrud.getOwner("")

        textView = (TextView) findViewById(R.id.restaurantTitle);
        ratingBar = (RatingBar) findViewById(R.id.restaurantAverageRatingBar);
    }

    private void setUpReviewModels () {
        //String[] reviewTitles = getResources().getStringArray(R.array.);
        //String[] reviewRatings = getResources().getStringArray(R.array);

//        for (int i = 0; i < reviewTitles.length; i++){
//            reviewModels.add(new ReviewModel(reviewTitles[i],
//                    reviewRatings[i]));
//        }
    }
}