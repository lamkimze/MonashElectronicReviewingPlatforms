package com.example.myapplication.Database;


import static java.lang.String.format;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.example.myapplication.Entities.User;
import com.example.myapplication.Position;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.password4j.Password;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;


public class CRUD_User {

    DatabaseHelper dbHelper;
    Locale locale = Locale.getDefault();

    public CRUD_User(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    // login methods
    /**
     * Method to verify the login credentials of a user
     * @param user the user trying to login
     * @param password the UN-HASHED password attempt of the user
     * @return true if the login credentials are correct, false otherwise
     */
    public boolean verifyLogin(User user, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = user.getUsername();
        String selectUser = format("SELECT * FROM user WHERE username = '%s';", username);
        // Check if the user exists
        Cursor cursor = db.rawQuery(selectUser, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("password");
            if (index == -1) {
                cursor.close();
                return false;
            }
            String hashedPass = cursor.getString(index);
            cursor.close();
            return Password.check(password, hashedPass).withScrypt();
        }
        return false;
    }

    /**
     * Method to login a user, if the login credentials are correct
     * then we set the rest of the user's information
     * @param user the user trying to login
     * @param password the UN-HASHED password attempt of the user
     * @return the users id
     */
    public int loginUser(User user, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // verify the login credentials
        if (!verifyLogin(user, password)) {
            return -1;
        }
        // get the user's ID
        String username = user.getUsername();
        String selectUser = format(locale,"SELECT * FROM user WHERE username = '%s';", username);
        Cursor cursor = db.rawQuery(selectUser, null);
        cursor.moveToFirst();
        int userIDIndex = cursor.getColumnIndex("user_id");
        if (userIDIndex == -1) {
            cursor.close();
            return -1;
        }
        int userID = cursor.getInt(userIDIndex);
        cursor.close();
        return userID;
    }



    // Create methods

    /**
     * Method to create a user in the database without a profile picture
     * @param user the user to be created
     * @param password the UN-HASHED password of the user
     * @return true if the user was created, false otherwise
     */
    public Boolean createUser(User user, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String username = user.getUsername();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String hashedPass = Password.hash(password).withScrypt().getResult();
        // check if email or username already exists
        String selectUser = format(locale,"SELECT * FROM user WHERE username = '%s' OR email = '%s';", username, email);
        Cursor cursor = db.rawQuery(selectUser, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        // insert the user into the database
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("first_name", firstName);
        contentValues.put("last_name", lastName);
        contentValues.put("password", hashedPass);
        db.insert("user", null, contentValues);
        return true;
    }

    /**
     * Method to create a user in the database with a profile picture
     * @param user the user to be created
     * @param password the UN-HASHED password of the user
     * @param profileImage the profile picture of the user
     * @return true if the user was created, false otherwise
     * @throws IOException if the profile picture could not be inserted
     */
    public Boolean createUser(User user, String password, Bitmap profileImage) throws IOException {
        // insert the user into the database
        createUser(user, password);

        // insert the profile picture into the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int userID = getUserID(user);
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("profile_picture", DbBitmapUtility.getBytes(profileImage));
            db.update("user", contentValues,
                    "user_id = ?",
                    new String[]{String.valueOf(userID)}
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }



    /**
     * Method to create a user's position
     * @param userID the ID of the user to create the position for
     * @param busID the ID of the business the user is at
     * @param position the position of the user at the business
     * @return the ID of the user's position
     */
    public int createUserPosition(int userID, int busID, String position){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", userID);
        contentValues.put("bus_id", busID);
        contentValues.put("position_name", position);
        db.insert("user_position", null, contentValues);

        // return this user's position ID
        String selectPosition = format(locale,"SELECT * FROM user_position WHERE user_id = %d AND bus_id = %d;", userID, busID);
        Cursor cursor = db.rawQuery(selectPosition, null);
        cursor.moveToFirst();
        int positionIDIndex = cursor.getColumnIndex("position_id");
        if (positionIDIndex == -1) {
            cursor.close();
            return -1;
        }
        int positionID = cursor.getInt(positionIDIndex);
        cursor.close();

        // set the user's position ID
        assignPositionID(getUser(userID), positionID);

        return positionID;
    }


    // Read methods

    /**
     * Method to get the users ID
     * @param user the user to get the ID of
     * @return the user's ID
     */
    public int getUserID(User user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = user.getUsername();
        String selectUser = format(locale,"SELECT * FROM user WHERE username = '%s';", username);
        Cursor cursor = db.rawQuery(selectUser, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("user_id");
        if (index == -1) {
            cursor.close();
            return -1;
        }
        int userID = cursor.getInt(index);
        cursor.close();
        return userID;
    }

    /**
     * Method to get a user instance with all the user's information
     * @param userID the ID of the user to get the information of
     * @return the user object with all the user's information
     */
    public User getUser(int userID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectUser = format(locale,"SELECT * FROM user WHERE user_id = %d;", userID);
        Cursor cursor = db.rawQuery(selectUser, null);
        cursor.moveToFirst();
        int usernameIndex = cursor.getColumnIndex("username");
        int emailIndex = cursor.getColumnIndex("email");
        int firstNameIndex = cursor.getColumnIndex("first_name");
        int lastNameIndex = cursor.getColumnIndex("last_name");
        int positionIDIndex = cursor.getColumnIndex("position_id");
        int profilePicIndex = cursor.getColumnIndex("profile_image");
        if (usernameIndex == -1 || emailIndex == -1 || firstNameIndex == -1 || lastNameIndex == -1 || positionIDIndex == -1) {
            cursor.close();
            return null;
        }
        String username = cursor.getString(usernameIndex);
        String email = cursor.getString(emailIndex);
        String firstName = cursor.getString(firstNameIndex);
        String lastName = cursor.getString(lastNameIndex);
        User user = new User(username, email, firstName, lastName);
        int positionID = cursor.getInt(positionIDIndex);
        byte[] profilePicBlob = cursor.getBlob(profilePicIndex);
        if (profilePicBlob != null) {
            Bitmap profilePic = DbBitmapUtility.getBitmap(profilePicBlob);
            user.setProfilePicture(profilePic);
        }
        // create a new user object with the user's information
        // set the user's ID, position ID, and profile picture
        user.setId(userID);
        user.setPositionID(positionID);
        cursor.close();
        return user;
    }

    public Position getUserPoistion(int userID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // get user position id
        int positionID = getUser(userID).getPositionID();
        // check if position id is defined
        if (positionID <= 0) {
            return null;
        }
        String positionSelect = format(locale, "SELECT * FROM user_position WHERE position_id = %d", positionID);
        Cursor cursor = db.rawQuery(positionSelect, null);
        cursor.moveToFirst();
        int businessIndex = cursor.getColumnIndex("bus_id");
        int positionNameIndex = cursor.getColumnIndex("position_name");
        if (businessIndex == -1 || positionNameIndex == -1) {
            return null;
        }
        int busID = cursor.getInt(businessIndex);
        String positionName = cursor.getString(positionNameIndex);
        cursor.close();
        return new Position(positionID, userID, busID, positionName);
    }


    // Update methods
    /**
     * Assigns a position ID to a user
     * @param user the user to assign the position ID to
     * @param positionID the position ID to assign to the user
     */
    public void assignPositionID(User user, int positionID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int userID = getUserID(user);
        ContentValues contentValues = new ContentValues();
        contentValues.put("position_id", positionID);
        db.update("user", contentValues,
                "user_id = ?",
                new String[]{String.valueOf(userID)}
        );
    }



    /**
     * Method to get the full name of a user (first name and last name) by user ID
     * @param userID the ID of the user
     * @return the full name of the user as a String in the format "FirstName LastName", or null if the user is not found
     */
    public String getFullName(int userID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to get the first name and last name based on the user ID
        String selectUser = format(locale, "SELECT first_name, last_name FROM user WHERE user_id = %d;", userID);
        Cursor cursor = db.rawQuery(selectUser, null);

        // Check if the query returned any result
        if (cursor.moveToFirst()) {
            // Get the indices of the first and last name columns
            int firstNameIndex = cursor.getColumnIndex("first_name");
            int lastNameIndex = cursor.getColumnIndex("last_name");

            // If either index is invalid, close the cursor and return null
            if (firstNameIndex == -1 || lastNameIndex == -1) {
                cursor.close();
                return null;
            }

            // Retrieve the first and last names from the cursor
            String firstName = cursor.getString(firstNameIndex);
            String lastName = cursor.getString(lastNameIndex);

            // Close the cursor and return the full name as "FirstName LastName"
            cursor.close();
            return firstName + " " + lastName;
        }

        // If no user is found, close the cursor and return null
        cursor.close();
        return null;
    }

    public ArrayList<Integer> getFavoriteBusinesses(int userID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to get the favorite businesses JSON array for the given user ID
        String selectFavorites = format(locale, "SELECT favorites FROM user_favorites WHERE user_id = %d;", userID);
        Cursor cursor = db.rawQuery(selectFavorites, null);

        // Check if we got a result
        if (cursor.moveToFirst()) {
            // Get the index of the 'favorites' column
            int favListIndex = cursor.getColumnIndex("favorites");

            // If the index is valid, retrieve the JSON string
            if (favListIndex != -1) {
                String favoriteBusinessJson = cursor.getString(favListIndex);

                // Use Gson to deserialize the JSON string back into an ArrayList of Integer
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
                ArrayList<Integer> favoriteBusinesses = gson.fromJson(favoriteBusinessJson, type);

                // Close the cursor and return the list of favorite business IDs
                cursor.close();
                return favoriteBusinesses;
            }
        }

        // If no favorite businesses are found, return an empty list
        cursor.close();
        return new ArrayList<>();
    }

}


