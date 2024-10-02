package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Entities.User;
import com.google.android.material.navigation.NavigationView;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    boolean isBackDisabled = false;
    int userId;
    Position userPosition;
    DatabaseHelper dbHelper;
    CRUD_User crudUser;
    CRUD_Image crudImage;
    ImageView userPicture;
    TextView userName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_base);  // Use the base layout here

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);


        // Set up the NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            Log.e("DrawerBaseActivity", "NavigationView is found");
            navigationView.setNavigationItemSelectedListener(this);
        } else {
            Log.e("DrawerBaseActivity", "NavigationView is null");
        }

        // Set up the drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void setContentView(View view){
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        userId = getIntent().getExtras().getInt("userId");
        loadData();

        Log.d("TAG", "User ID: " + userId);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        userPicture = drawerLayout.findViewById(R.id.userPicture);
//        userName = drawerLayout.findViewById(R.id.userName);
//        userEmail = drawerLayout.findViewById(R.id.userEmail);

//        User currentUser = crudUser.getUser(userId);
//
//        if(currentUser.getProfilePicture() != null){
//            userPicture.setImageBitmap(currentUser.getProfilePicture());
//        }else{
//            userPicture.setImageResource(R.drawable.person_icon);
//        }
//
//        userName.setText(currentUser.getUsername());
//        userEmail.setText(currentUser.getEmail());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    private void loadData() {
        try{
            dbHelper = new DatabaseHelper(this);
            crudImage = new CRUD_Image(dbHelper);
            crudUser = new CRUD_User(dbHelper);;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToLogin(){
        isBackDisabled = true;
        Intent loginIntent = new Intent(this, loginPage.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void goToFavourite(){
        Intent favIntent = new Intent(this, favouriteRestaurantsPage.class);
        favIntent.putExtra("userId", userId);
        startActivity(favIntent);
    }

    public void goToProfile(View view){
        userPosition = crudUser.getUserPoistion(userId);
        if (userPosition != null && userPosition.getPositionName().equals("Owner")){
            Intent profileIntent = new Intent(this, restaurantOwnerProfilePage.class);
            profileIntent.putExtra("userId", userId);
            profileIntent.putExtra("busId", crudUser.getUserPoistion(userId).getBusinessID());
            startActivity(profileIntent);
        }else{
            Intent profileIntent = new Intent(this, profilePage.class);
            Log.e("TAG","Going to user profile");
            profileIntent.putExtra("userId", userId);
            startActivity(profileIntent);
        }
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        Log.d("TAG", "Item ID: " + id);

        if (id == R.id.item_home) {
            goTaDashBoard();
        }else if(id == R.id.item_setting){

        } else if (id == R.id.item_share) {

        } else if (id == R.id.item_profile) {
            goToProfile(item.getActionView());

        } else if(id == R.id.item_favouriteList){
            goToFavourite();
        }
        else if(id == R.id.item_log_out){
            goToLogin();
        }

        drawerLayout.closeDrawers();
        return true;
    }

    private void goTaDashBoard() {
        Intent dashboardPage = new Intent(getApplicationContext(), dashboardPage.class);
        dashboardPage.putExtra("userid", userId);
    }

    protected void allocateActivityTitle(String titleString){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(titleString);
        }
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }



    protected void onPostCreate(Bundle saveInstanceState){
        super.onPostCreate(saveInstanceState);
        new ActionBarDrawerToggle(this, (DrawerLayout) findViewById(R.id.drawer_layout),
                (Toolbar) findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close){}.syncState();
    }
}
