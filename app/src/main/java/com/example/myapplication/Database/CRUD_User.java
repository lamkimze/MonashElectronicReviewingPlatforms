package com.example.myapplication.Database;


import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Entities.User;
import com.password4j.Password;


public class CRUD_User {

    DatabaseHelper dbHelper;

    public CRUD_User(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }


    // login methods
    /**
     * Verify the login credentials of a user
     * @param username the username of the user
     * @param password the UN-HASHED password of the user
     * @return true if the login credentials are correct, false otherwise
     */
    @SuppressLint("Recycle")
    public boolean verifyLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectUser = format("SELECT * FROM user WHERE username = '%s';", username);
        if (db.rawQuery(selectUser, null).getCount() == 1) {
            // grab the hashed password from the database
            String selectHashedPass = format("SELECT passwrd FROM user WHERE username = '%s';", username);
            Cursor cursor = db.rawQuery(selectHashedPass, null);
            cursor.moveToFirst();
            @SuppressLint("Range") String hashedPass = cursor.getString(cursor.getColumnIndex("password"));
            return Password.check(password, hashedPass).withSCrypt(); // check the password
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
    @SuppressLint("Range")
    public User loginUser(User user, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String username = user.getUsername();
        // verify the login credentials
        if (verifyLogin(username, password)) {
            // use the users setter methods to set the user's information
            String userTable = user.getTableName();
            String selectUser = format("SELECT * FROM %s WHERE username = '%s';", userTable, username);
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectUser, null);
            cursor.moveToFirst();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
            user.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
            user.setCreatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
            return user;
        }
        return null;
    }

    // Create methods
    /**
     * Create a user in the database
     * @param user the user to create
     * @param password the UN-HASHED password of the user
     */
    public void createUser(User user, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String userTable = user.getTableName();
        String username = user.getUsername();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String hashedPass = Password.hash(password).withBCrypt().getResult();
        String insertStatement = "INSERT INTO %s (username, password, email, first_name, last_name)";
        String valueStatement = " VALUES ('%s', '%s', '%s', '%s', '%s');";
        String insertUser = format(insertStatement + valueStatement,
                userTable,
                username,
                hashedPass,
                email,
                firstName,
                lastName);
        db.execSQL(insertUser);
    }





}
