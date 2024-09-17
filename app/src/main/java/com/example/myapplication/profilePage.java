package com.example.myapplication;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Entities.User;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class profilePage extends AppCompatActivity {
    ImageView imageView;
    int userid;
    User user;
    TextView username, userEmail;

    CRUD_User crudUser;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);

        try {
            dbHelper = new DatabaseHelper(this);
            crudUser = new CRUD_User(dbHelper);
            Toast.makeText(this, "Database initialized successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Database initialization failed!", Toast.LENGTH_LONG).show();
        }

        userid = getIntent().getExtras().getInt("userId");
        user = crudUser.getUser(userid);

        username = findViewById(R.id.textView19);
        userEmail = findViewById(R.id.userProfileEmailAddress);

        username.setText(user.getUsername());
        userEmail.setText(user.getEmail());

        imageView = (ImageView) findViewById(R.id.imageView2);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_icon);
        Bitmap scakedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        Drawable userDrawable = new BitmapDrawable(getResources(), scakedBitmap);
        imageView.setImageDrawable(userDrawable);
    }


}