package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.Database.CRUD_User;
import com.google.android.material.navigation.NavigationView;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    boolean isBackDisabled = false;
    int userid;
    Position userPosition;
    CRUD_User crudUser;


    public void setContentView(View view){
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        userid = getIntent().getExtras().getInt("userId");
        //userPosition = crudUser.getUserPoistion(userid);




        Log.d("TAG", "User ID: " + userid);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


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
        startActivity(favIntent);
    }

    private void goToProfile(){
        userid = getIntent().getExtras().getInt("userId");
        userPosition = null;
        if (userPosition == null){
            Intent favIntent = new Intent(this, profilePage.class);
            startActivity(favIntent);
        }

        else if (userPosition.getPositionName() == "Owner"){
            Intent favIntent = new Intent(this, restaurantOwnerProfilePage.class);
            startActivity(favIntent);
        }else{
            Intent favIntent = new Intent(this, profilePage.class);
            startActivity(favIntent);
        }
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if (id == R.id.item_home) {

        }else if(id == R.id.item_setting){

        } else if (id == R.id.item_share) {

        } else if (id == R.id.item_profile) {
            goToProfile();

        } else if(id == R.id.item_favouriteList){
            goToFavourite();
        }
        else if(id == R.id.item_log_out){
            goToLogin();
        }

        drawerLayout.closeDrawers();
        return true;
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

    public void goToProfile(View view){
        Intent profileIntent = new Intent(this, profilePage.class);
        profileIntent.putExtra("userid", userid);
        startActivity(profileIntent);
    }

    protected void onPostCreate(Bundle saveInstanceState){
        super.onPostCreate(saveInstanceState);
        new ActionBarDrawerToggle(this, (DrawerLayout) findViewById(R.id.drawer_layout),
                (Toolbar) findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close){}.syncState();
    }
}
