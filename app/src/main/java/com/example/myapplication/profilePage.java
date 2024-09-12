package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class profilePage extends AppCompatActivity {
    ImageView imageView;
//    Button favouritesButton;
//    Button editProfileButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        favouritesButton = findViewById(R.id.button7);
//
//        favouritesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(profilePage.this,favouriteRestaurantsPage.class);
//                startActivity(i);
//                finish();
//            }
//        });
//
//        editProfileButton = findViewById(R.id.editPageBtn);
//
//        editProfileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(profilePage.this,profilePageEditor.class);
//                startActivity(i);
//                finish();
//            }
//        });

        imageView = (ImageView) findViewById(R.id.imageView2);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_icon);
        Bitmap scakedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        Drawable userDrawable = new BitmapDrawable(getResources(), scakedBitmap);
        imageView.setImageDrawable(userDrawable);
    }
}