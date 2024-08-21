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

public class restaurantOwnerProfilePage extends AppCompatActivity {

    TextView textView;
    RatingBar ratingBar;
    ImageView imageView;

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
        Bitmap scakedBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
        Drawable userDrawable = new BitmapDrawable(getResources(), scakedBitmap);
        imageView.setImageDrawable(userDrawable);

        textView = (TextView) findViewById(R.id.restaurantTitle);
        ratingBar = (RatingBar) findViewById(R.id.restaurantAverageRatingBar);
    }
}