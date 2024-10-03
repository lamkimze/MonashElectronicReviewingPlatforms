package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setContentView(View view){
        super.setContentView(R.layout.activity_drawer_base); // Set base layout which includes the drawer and toolbar
        FrameLayout container = findViewById(R.id.activityContainer);
        container.addView(view);

        userId = getIntent().getExtras().getInt("userId");
        loadData();

        Log.d("TAG", "User ID: " + userId);

        Toolbar toolbar = findViewById(R.id.toolbar); // Get the toolbar from the layout
        setSupportActionBar(toolbar); // Set the toolbar as the action bar

        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        userPicture = headerView.findViewById(R.id.userPicture);
        userName = headerView.findViewById(R.id.userName);
        userEmail = headerView.findViewById(R.id.userEmail);

        User currentUser = crudUser.getUser(userId);

        if(currentUser.getProfilePicture() != null){
            userPicture.setImageBitmap(Bitmap.createScaledBitmap(currentUser.getProfilePicture(), 120, 120, false));
        }else{
            userPicture.setImageResource(R.drawable.person_icon);
        }

        userName.setText(currentUser.getUsername());
        userEmail.setText(currentUser.getEmail());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void loadData() {
        try {
            dbHelper = new DatabaseHelper(this);
            crudImage = new CRUD_Image(dbHelper);
            crudUser = new CRUD_User(dbHelper);
        } catch (Exception e) {
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
        } else {
            Intent profileIntent = new Intent(this, profilePage.class);
            profileIntent.putExtra("userId", userId);
            startActivity(profileIntent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId(); // get the item ID

        // Use if-else instead of switch to avoid constant expression requirement
        if (id == R.id.item_home) {
            goTaDashBoard();
        } else if (id == R.id.item_setting) {
            Log.d("TAG", "Settings item selected");
        } else if (id == R.id.item_share) {
            Log.d("TAG", "Share item selected");
        } else if (id == R.id.item_profile) {
            goToProfile(item.getActionView());
        } else if (id == R.id.item_favouriteList) {
            goToFavourite();
        } else if (id == R.id.item_log_out) {
            goToLogin();
        } else {
            Log.d("TAG", "Unknown item selected");
        }

        drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer after selection
        return true;
    }


    private void goTaDashBoard() {
        Intent dashboardPage = new Intent(getApplicationContext(), dashboardPage.class);
        dashboardPage.putExtra("userid", userId);
        startActivity(dashboardPage);
    }

    protected void allocateActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        } else {
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle saveInstanceState) {
        super.onPostCreate(saveInstanceState);
        new ActionBarDrawerToggle(this, (DrawerLayout) findViewById(R.id.drawer_layout),
                (Toolbar) findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close){}.syncState();
    }
}

