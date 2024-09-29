package com.example.myapplication.Database;

import static java.lang.String.*;
import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.contentcapture.ContentCaptureCondition;

import com.example.myapplication.Restaurant;

import java.util.ArrayList;
import java.util.Locale;

public class CRUD_Business {

    DatabaseHelper dbHelper;
    Locale locale = Locale.getDefault();

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
    public int createBusiness(String bus_name, String bus_addr, String bus_ph_nb, String bus_email, String website_url, String bus_hours, String bus_cuisine_type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bus_name", bus_name);
        values.put("bus_addr", bus_addr);
        values.put("bus_ph_nb", bus_ph_nb);
        values.put("bus_email", bus_email);
        values.put("website_url", website_url);
        values.put("bus_hours", bus_hours);
        values.put("bus_cuisine_type", bus_cuisine_type);
        db.insert("business", null, values);
        // return the id of the business as it is auto incremented so take the largest id in the table
        String idSelectQuery = "SELECT MAX(bus_id) FROM business;";
        Cursor cursor = db.rawQuery(idSelectQuery, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("MAX(bus_id)");
        if (index != -1) {
            int bus_id = cursor.getInt(index);
            cursor.close();
            return bus_id;
        } else {
            cursor.close();
            return -1;
        }
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
        String selectRestaurant = format(locale,"SELECT * FROM business WHERE bus_id = %d;", bus_id);
        Cursor cursor = db.rawQuery(selectRestaurant, null);
        cursor.moveToFirst();
        Restaurant restaurant = new Restaurant(null, null, null);
        restaurant.setId(cursor.getInt(cursor.getColumnIndex("bus_id"))) ;
        restaurant.setName(cursor.getString(cursor.getColumnIndex("bus_name")));
        restaurant.setAddress(cursor.getString(cursor.getColumnIndex("bus_addr")));
        restaurant.setPhone(cursor.getString(cursor.getColumnIndex("bus_ph_nb")));
        restaurant.setEmail(cursor.getString(cursor.getColumnIndex("bus_email")));
        restaurant.setWebsite(cursor.getString(cursor.getColumnIndex("website_url")));
        restaurant.setHours(cursor.getString(cursor.getColumnIndex("bus_hours")));
        restaurant.setCuisine(cursor.getString(cursor.getColumnIndex("bus_cuisine_type")));

        byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex("bus_image"));
        if (imageBlob != null) {
            restaurant.setBusinessImage(DbBitmapUtility.getBitmap(imageBlob));
        }

        cursor.close();
        // Set the star rating of the restaurant
        restaurant.setStars(getBusinessStarRating(restaurant));
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
        String selectOwnerID = format(locale, "SELECT owner_id FROM business WHERE bus_id = %d;", bus_id);
        Cursor cursor = db.rawQuery(selectOwnerID, null);
        cursor.moveToFirst();

        int ownerIdIndex = cursor.getColumnIndex("owner_id");
        if (ownerIdIndex != -1) {
            int ownerID = cursor.getInt(ownerIdIndex);
            cursor.close();
            return ownerID;
        } else {
            cursor.close();
            return -1;
        }
    }

    /**
     * Calculates the star rating of a business
     * @param restaurant The restaurant to calculate the star rating of
     * @return The star rating of the business in double format
     */
    public float getBusinessStarRating(Restaurant restaurant) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int bus_id = restaurant.getId();
        String selectStarRating = format(locale,"SELECT AVG(star_rating) FROM review WHERE bus_id = %d;", bus_id);
        Cursor cursor = db.rawQuery(selectStarRating, null);
        cursor.moveToFirst();
        int starRatingIndex = cursor.getColumnIndex("AVG(star_rating)");
        float starRating = cursor.getFloat(starRatingIndex);
        cursor.close();
        return starRating;
    }

    /**
     * Gets the most reviewed restaurants of the day
     * @return An ArrayList of the most reviewed restaurants
     */
    public ArrayList<Restaurant> getMostReviewedRestaurantsDaily() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectMostReviewedRestaurants = "SELECT bus_id, COUNT(*) FROM review WHERE date = CURRENT_DATE GROUP BY bus_id ORDER BY COUNT(*) DESC;";
        Cursor cursor = db.rawQuery(selectMostReviewedRestaurants, null);
        ArrayList<Restaurant> mostReviewedRestaurants = new ArrayList<>();
        // After
        while (cursor.moveToNext() && mostReviewedRestaurants.size() < 5) {
            int busIdIndex = cursor.getColumnIndex("bus_id");
            if (busIdIndex != -1) {
                int bus_id = cursor.getInt(busIdIndex);
                mostReviewedRestaurants.add(getRestaurant(bus_id));
            }
        }
        cursor.close();
        return mostReviewedRestaurants;
    }
    // Update operations

    /**
     * Updates the name of a business
     * @param bus_id The id of the business
     * @param bus_name The new name of the business
     * @return bus_id The id of the business
     */
    public int updateBusinessName(int bus_id, String bus_name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bus_name", bus_name);
        String whereClause = "bus_id = ?";
        String[] whereArgs = {String.valueOf(bus_id)};
        db.update("business", contentValues, whereClause, whereArgs);
        return bus_id;
    }


    /**
     * Updates the address of a business
     * @param bus_id The id of the business
     * @param bus_addr The new address of the business
     * @return bus_id The id of the business
     */
    public int updateBusinessAddress(int bus_id, String bus_addr) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bus_addr", bus_addr);
        String whereClause = "bus_id = ?";
        String[] whereArgs = {String.valueOf(bus_id)};
        db.update("business", contentValues, whereClause, whereArgs);
        return bus_id;
    }

    /**
     * Updates the phone number of a business
     * @param bus_id The id of the business
     * @param bus_ph_nb The new phone number of the business
     * @return bus_id The id of the business
     */
    public int updateBusinessPhone(int bus_id, String bus_ph_nb) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bus_ph_nb", bus_ph_nb);
        String whereClause = "bus_id = ?";
        String[] whereArgs = {String.valueOf(bus_id)};
        db.update("business", contentValues, whereClause, whereArgs);
        return bus_id;
    }

    /**
     * Updates the email of a business
     * @param bus_id The id of the business
     * @param bus_email The new email of the business
     * @return bus_id The id of the business
     */
    public int  updateBusinessEmail(int bus_id, String bus_email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bus_email", bus_email);
        String whereClause = "bus_id = ?";
        String[] whereArgs = {String.valueOf(bus_id)};
        db.update("business", contentValues, whereClause, whereArgs);
        return bus_id;
    }

    /**
     * Updates the website of a business
     * @param bus_id The id of the business
     * @param website_url The new website of the business
     * @return bus_id The id of the business
     */
    public int updateBusinessWebsite(int bus_id, String website_url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("website_url", website_url);
        String whereClause = "bus_id = ?";
        String[] whereArgs = {String.valueOf(bus_id)};
        db.update("business", contentValues, whereClause, whereArgs);
        return bus_id;
    }

    /**
     * Updates the hours of a business
     * @param bus_id The id of the business
     * @param bus_hours The new hours of the business
     * @return bus_id The id of the business
     */
    public int  updateBusinessHours(int bus_id, String bus_hours) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bus_hours", bus_hours);
        String whereClause = "bus_id = ?";
        String[] whereArgs = {String.valueOf(bus_id)};
        db.update("business", contentValues, whereClause, whereArgs);
        return bus_id;
    }

    /**
     * Updates the cuisine type of a business
     * @param bus_id The id of the business
     * @param bus_cuisine_type The new cuisine type of the business
     * @return bus_id The id of the business
     */
    public int updateBusinessCuisine(int bus_id, String bus_cuisine_type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bus_cuisine_type", bus_cuisine_type);
        String whereClause = "bus_id = ?";
        String[] whereArgs = {String.valueOf(bus_id)};
        db.update("business", contentValues, whereClause, whereArgs);
        return bus_id;
    }


    /**
     * Updates the details of a restaurant using the business's name
     * @param bus_name The name of the business
     * @param bus_addr The address of the business
     * @param bus_ph_nb The phone number of the business
     * @param bus_email The email of the business
     * @param website_url The website of the business
     * @param bus_hours The hours of the business
     * @param bus_cuisine_type The cuisine type of the business
     */
    public int updateRestaurantDetail(String bus_name, String bus_addr, String bus_ph_nb, String bus_email, String website_url, String bus_hours, String bus_cuisine_type) {
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

        // get the id of the business
        String selectBusinessId = format(locale,
                "SELECT bus_id FROM business WHERE bus_name = %s;",
                bus_name
        );
        Cursor cursor = db.rawQuery(selectBusinessId, null);
        cursor.moveToFirst();
        @SuppressLint("Range") int bus_id = cursor.getInt(cursor.getColumnIndex("bus_id"));
        cursor.close();
        return bus_id;
    }


    /**
     * Method to get the average rating of a business
     * @return array list of top rated businesses
     */
    public ArrayList<Restaurant> getTop5RatedBusinesses() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to get businesses and their average ratings, sorted in descending order by rating
        String query = "SELECT b.bus_id, b.bus_name, b.bus_addr, b.bus_ph_nb, b.bus_email, b.website_url, b.bus_hours, b.bus_cuisine_type, AVG(r.star_rating) AS avg_rating " +
                "FROM business b " +
                "JOIN review r ON b.bus_id = r.bus_id " +
                "GROUP BY b.bus_id " +
                "ORDER BY avg_rating DESC " +
                "LIMIT 5;";

        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Restaurant> topRatedRestaurants = new ArrayList<>();

        // Iterate through the results and populate the restaurant objects
        while (cursor.moveToNext()) {
            int busIdIndex = cursor.getColumnIndex("bus_id");
            int busNameIndex = cursor.getColumnIndex("bus_name");
            int busAddrIndex = cursor.getColumnIndex("bus_addr");
            int busPhoneIndex = cursor.getColumnIndex("bus_ph_nb");
            int busEmailIndex = cursor.getColumnIndex("bus_email");
            int busWebsiteIndex = cursor.getColumnIndex("website_url");
            int busHoursIndex = cursor.getColumnIndex("bus_hours");
            int busCuisineIndex = cursor.getColumnIndex("bus_cuisine_type");
            int avgRatingIndex = cursor.getColumnIndex("avg_rating");

            if (busIdIndex != -1 && avgRatingIndex != -1) {
                // Create a Restaurant object for each result
                Restaurant restaurant = new Restaurant(
                        cursor.getString(busNameIndex),
                        cursor.getString(busAddrIndex),
                        cursor.getString(busPhoneIndex)
                );
                restaurant.setId(cursor.getInt(busIdIndex));
                restaurant.setEmail(cursor.getString(busEmailIndex));
                restaurant.setWebsite(cursor.getString(busWebsiteIndex));
                restaurant.setHours(cursor.getString(busHoursIndex));
                restaurant.setCuisine(cursor.getString(busCuisineIndex));
                restaurant.setStars(cursor.getFloat(avgRatingIndex));

                // Add the restaurant to the list
                topRatedRestaurants.add(restaurant);
            }
        }

        cursor.close();
        return topRatedRestaurants;
    }
}
