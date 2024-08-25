package com.example.myapplication.Database;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Database.DatabaseHelper;
import com.example.myapplication.Restaurant;

import java.util.ArrayList;

public class CRUD_Business {

    DatabaseHelper dbHelper;

    public CRUD_Business(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }



    // Create operation
    /**
     * Creates a new business and inserts it into the database
     * @param bus_name The name of the business
     * @param bus_addr The address of the business
     * @param bus_ph_nb The phone number of the business
     * @param bus_email The email of the business
     * @param website_url The website of the business
     * @param bus_hours The hours of the business
     * @param bus_cuisine_type The cuisine type of the business
     */
    public void createBusiness(String bus_name, String bus_addr, String bus_ph_nb, String bus_email, String website_url, String bus_hours, String bus_cuisine_type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String insertBusinessData = "INSERT INTO business (bus_name, bus_addr, bus_ph_nb, bus_email, website_url, bus_hours, bus_cuisine_type) VALUES ('" + bus_name + "', '" + bus_addr + "', '" + bus_ph_nb + "', '" + bus_email + "', '" + website_url + "', '" + bus_hours + "', '" + bus_cuisine_type + "');";
        db.execSQL(insertBusinessData);
    }

    // Read operations
    /**
     * Get all restaurants from the database
     * @return An ArrayList of Restaurant objects
     */
    @SuppressLint("Range")
    public ArrayList<Restaurant> getAllRestaurants(){
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM business";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Restaurant restaurant = new Restaurant(null , null, null);
                restaurant.setId(cursor.getInt(cursor.getColumnIndex("bus_id")));
                restaurant.setName(cursor.getString(cursor.getColumnIndex("bus_name")));
                restaurant.setAddress(cursor.getString(cursor.getColumnIndex("bus_addr")));
                restaurant.setPhone(cursor.getString(cursor.getColumnIndex("bus_ph_nb")));
                restaurant.setEmail(cursor.getString(cursor.getColumnIndex("bus_email")));
                restaurant.setWebsite(cursor.getString(cursor.getColumnIndex("website_url")));
                restaurant.setHours(cursor.getString(cursor.getColumnIndex("bus_hours")));
                restaurant.setCuisine(cursor.getString(cursor.getColumnIndex("bus_cuisine_type")));
                restaurants.add(restaurant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return restaurants;
    }

    // Update operations


}
