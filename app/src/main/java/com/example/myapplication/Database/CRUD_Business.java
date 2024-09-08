package com.example.myapplication.Database;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.contentcapture.ContentCaptureCondition;

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
        String insertBusinessData = format("INSERT INTO business (bus_name, bus_addr, bus_ph_nb, bus_email, website_url, bus_hours, bus_cuisine_type) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                bus_name,
                bus_addr,
                bus_ph_nb,
                bus_email,
                website_url,
                bus_hours,
                bus_cuisine_type);
        db.execSQL(insertBusinessData);
    }

    // Read operations

    /**
     * Gets all the business ids from the database
     * @return An ArrayList of all the business ids
     */
    @SuppressLint("Range")
    public ArrayList<Integer> getBusinessIds() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectBusinessIds = "SELECT bus_id FROM business;";
        Cursor cursor = db.rawQuery(selectBusinessIds, null);
        ArrayList<Integer> businessIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            businessIds.add(cursor.getInt(cursor.getColumnIndex("bus_id")));
        }
        cursor.close();
        return businessIds;
    }

    /**
     * Gets a business from the database
     * @param bus_id The id of the business
     * @return A Restaurant object
     */
    @SuppressLint("Range")
    public Restaurant getRestaurant(int bus_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectRestaurant = "SELECT * FROM business WHERE bus_id = " + bus_id + ";";
        Cursor cursor = db.rawQuery(selectRestaurant, null);
        cursor.moveToFirst();
        Restaurant restaurant = new Restaurant(null, null, null);
        restaurant.setId(cursor.getInt(cursor.getColumnIndex("bus_id")));
        restaurant.setName(cursor.getString(cursor.getColumnIndex("bus_name")));
        restaurant.setAddress(cursor.getString(cursor.getColumnIndex("bus_addr")));
        restaurant.setPhone(cursor.getString(cursor.getColumnIndex("bus_ph_nb")));
        restaurant.setEmail(cursor.getString(cursor.getColumnIndex("bus_email")));
        restaurant.setWebsite(cursor.getString(cursor.getColumnIndex("website_url")));
        restaurant.setHours(cursor.getString(cursor.getColumnIndex("bus_hours")));
        restaurant.setCuisine(cursor.getString(cursor.getColumnIndex("bus_cuisine_type")));
        cursor.close();
        return restaurant;
    }

    /**
     * Gets all the restaurants from the database
     * @return An ArrayList of all the restaurants
     */
    public ArrayList<Restaurant> getAllRestaurants() {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        ArrayList<Integer> businessIds = getBusinessIds();
        for (int busID : businessIds) {
            restaurants.add(getRestaurant(busID));
        }
        return restaurants;
    }

    /**
     * Grabs the owner id of the business
     * @param restaurant the restaurant to get the owner id of
     * @return the owner id of the business
     */
    public int getOwnerID(Restaurant restaurant) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int bus_id = restaurant.getId();
        @SuppressLint("DefaultLocale") String selectOwnerID = format("SELECT owner_id FROM business WHERE bus_id = %d;", bus_id);
        Cursor cursor = db.rawQuery(selectOwnerID, null);
        cursor.moveToFirst();
        @SuppressLint("Range") int ownerID = cursor.getInt(cursor.getColumnIndex("owner_id"));
        cursor.close();
        return ownerID;
    }

    /**
     * Calculates the star rating of a business
     * @param restaurant The restaurant to calculate the star rating of
     * @return The star rating of the business in double format
     */
    public double getBusinessStarRating(Restaurant restaurant) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int bus_id = restaurant.getId();
        @SuppressLint("DefaultLocale") String selectStarRating = format("SELECT AVG(rating) FROM review WHERE bus_id = %d;", bus_id);
        Cursor cursor = db.rawQuery(selectStarRating, null);
        cursor.moveToFirst();
        @SuppressLint("Range") double starRating = cursor.getDouble(cursor.getColumnIndex("AVG(rating)"));
        cursor.close();
        return starRating;
    }

    /**
     * Gets the most reviewed restaurants of the day
     * @return An ArrayList of the most reviewed restaurants
     */
    @SuppressLint("Range")
    public ArrayList<Restaurant> getMostReviewedRestaurantsDaily() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectMostReviewedRestaurants = "SELECT bus_id, COUNT(*) FROM review WHERE date = CURRENT_DATE GROUP BY bus_id ORDER BY COUNT(*) DESC;";
        Cursor cursor = db.rawQuery(selectMostReviewedRestaurants, null);
        ArrayList<Restaurant> mostReviewedRestaurants = new ArrayList<>();
        while (cursor.moveToNext() && mostReviewedRestaurants.size() < 5) {
            int bus_id = cursor.getInt(cursor.getColumnIndex("bus_id"));
            mostReviewedRestaurants.add(getRestaurant(bus_id));
        }
        cursor.close();
        return mostReviewedRestaurants;
    }
    // Update operations
    /**
     * Updates the name of a business
     * @param bus_id The id of the business
     * @param bus_name The new name of the business
     */
    public void updateBusinessName(int bus_id, String bus_name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("DefaultLocale") String updateBusinessName = format("UPDATE business SET bus_name = '%s' WHERE bus_id = %d;",
                bus_name,
                bus_id);
        db.execSQL(updateBusinessName);
    }

    /**
     * Updates the address of a business
     * @param bus_id The id of the business
     * @param bus_addr The new address of the business
     */
    public void updateBusinessAddress(int bus_id, String bus_addr) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("DefaultLocale") String updateBusinessAddress = format("UPDATE business SET bus_addr = '%s' WHERE bus_id = %d;",
                bus_addr,
                bus_id);
        db.execSQL(updateBusinessAddress);
    }
    /**
     * Updates the phone number of a business
     * @param bus_id The id of the business
     * @param bus_ph_nb The new phone number of the business
     */
    public void updateBusinessPhone(int bus_id, String bus_ph_nb) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("DefaultLocale") String updateBusinessPhone = format("UPDATE business SET bus_ph_nb = '%s' WHERE bus_id = %d;",
                bus_ph_nb,
                bus_id);
        db.execSQL(updateBusinessPhone);
    }

    /**
     * Updates the email of a business
     * @param bus_id The id of the business
     * @param bus_email The new email of the business
     */
    public void updateBusinessEmail(int bus_id, String bus_email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("DefaultLocale") String updateBusinessEmail = format("UPDATE business SET bus_email = '%s' WHERE bus_id = %d;",
                bus_email,
                bus_id);
        db.execSQL(updateBusinessEmail);
    }

    /**
     * Updates the website of a business
     * @param bus_id The id of the business
     * @param website_url The new website of the business
     */
    public void updateBusinessWebsite(int bus_id, String website_url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("DefaultLocale") String updateBusinessWebsite = format("UPDATE business SET website_url = '%s' WHERE bus_id = %d;",
                website_url,
                bus_id);
        db.execSQL(updateBusinessWebsite);
    }
    /** Updates the hours of a business
     * @param bus_id The id of the business
     * @param bus_hours The new hours of the business
     */
    public void updateBusinessHours(int bus_id, String bus_hours) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("DefaultLocale") String updateBusinessHours = format("UPDATE business SET bus_hours = '%s' WHERE bus_id = %d;",
                bus_hours,
                bus_id);
        db.execSQL(updateBusinessHours);
    }
    /**
     * Updates the cuisine type of a business
     * @param bus_id The id of the business
     * @param bus_cuisine_type The new cuisine type of the business
     */
    public void updateBusinessCuisine(int bus_id, String bus_cuisine_type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("DefaultLocale") String updateBusinessCuisineType = format("UPDATE business SET bus_cuisine_type = '%s' WHERE bus_id = %d;",
                bus_cuisine_type,
                bus_id);
        db.execSQL(updateBusinessCuisineType);
    }

    /**
     * Assigns an owner to a business
     * @param bus_id The id of the business
     * @param owner_id The id of the owner
     */
    public void assignOwnerToBusiness(int bus_id, int owner_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        @SuppressLint("DefaultLocale") String updateBusinessOwner = format("UPDATE business SET owner_id = %d WHERE bus_id = %d;",
                owner_id,
                bus_id);
        db.execSQL(updateBusinessOwner);
    }

    public void updateRestaurantDetail(String bus_name, String bus_addr, String bus_ph_nb, String bus_email, String website_url, String bus_hours, String bus_cuisine_type) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("bus_name", bus_name);
        values.put("bus_addr", bus_addr);
        values.put("bus_ph_nb", bus_ph_nb);
        values.put("bus_email", bus_email);
        values.put("website_url", website_url);
        values.put("bus_hours", bus_hours);
        values.put("bus_cuisine_type", bus_cuisine_type);

        String whereClause = "bus_name = ?";
        String[] whereArgs = {bus_name};

        db.update("business", values, whereClause, whereArgs);
    }

}
