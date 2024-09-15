package com.example.myapplication.Database;


import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.example.myapplication.Entities.User;
import com.password4j.Password;

import java.util.Arrays;
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
     * @return the user object with all the user's information
     */
    public User loginUser(User user, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // verify the login credentials
        if (verifyLogin(user, password)) {
            // use the users setter methods to set the user's information
            String username = user.getUsername();
            String selectUser = format("SELECT * FROM user WHERE username = '%s';", username);
            Cursor cursor = db.rawQuery(selectUser, null);
            cursor.moveToFirst();

            // get the indices of the columns
            int[] indices = {cursor.getColumnIndex("user_id"),
                    cursor.getColumnIndex("email"),
                    cursor.getColumnIndex("first_name"),
                    cursor.getColumnIndex("last_name")};

            // if all the indices are found, set the user's information
            if (Arrays.stream(indices).noneMatch(i -> i == -1)) {
                user.setId(cursor.getInt(indices[0]));
                user.setEmail(cursor.getString(indices[1]));
                user.setFirstName(cursor.getString(indices[2]));
                user.setLastName(cursor.getString(indices[3]));
                cursor.close();
                return user;
            }
        }
        return null;
    }



    // Create methods

    public Boolean createUser(User user, String password, @Nullable Bitmap profilePic) {
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
        if (profilePic != null) {
            try {
                // convert the bitmap to a byte array
                byte[] profileImageBytes = DbBitmapUtility.getBytes(profilePic);
                contentValues.put("profile_picture", profileImageBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        db.insert("user", null, contentValues);
        return true;
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
        int index = cursor.getColumnIndex("id");
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
        String selectUser = format(locale,"SELECT * FROM user WHERE id = %d;", userID);
        Cursor cursor = db.rawQuery(selectUser, null);
        cursor.moveToFirst();
        int usernameIndex = cursor.getColumnIndex("username");
        int emailIndex = cursor.getColumnIndex("email");
        int firstNameIndex = cursor.getColumnIndex("first_name");
        int lastNameIndex = cursor.getColumnIndex("last_name");
        int positionIDIndex = cursor.getColumnIndex("position_id");
        int profilePicIndex = cursor.getColumnIndex("profile_picture");
        if (usernameIndex == -1 || emailIndex == -1 || firstNameIndex == -1 || lastNameIndex == -1 || positionIDIndex == -1) {
            cursor.close();
            return null;
        }
        String username = cursor.getString(usernameIndex);
        String email = cursor.getString(emailIndex);
        String firstName = cursor.getString(firstNameIndex);
        String lastName = cursor.getString(lastNameIndex);
        int positionID = cursor.getInt(positionIDIndex);
        Bitmap profilePic = DbBitmapUtility.getBlob(cursor.getBlob(profilePicIndex));
        // create a new user object with the user's information
        User user = new User(username, email, firstName, lastName);
        // set the user's ID, position ID, and profile picture
        user.setId(userID);
        user.setPositionID(positionID);
        user.setProfilePicture(profilePic);
        cursor.close();
        return user;
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
        db.update("user", contentValues, "id = ?", new String[]{String.valueOf(userID)});
    }

}


