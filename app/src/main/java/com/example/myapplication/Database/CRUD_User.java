package com.example.myapplication.Database;


import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.password4j.Password;


public class CRUD_User {

    DatabaseHelper dbHelper;

    public CRUD_User(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    /**
     * Verify the login credentials of a user
     * @param username the username of the user
     * @param passwrd the UNHASHED password of the user
     * @return true if the login credentials are correct, false otherwise
     */
    @SuppressLint("Recycle")
    public boolean verifyLogin(String username, String passwrd) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectUser = format("SELECT * FROM user WHERE username = '%s';", username);
        if (db.rawQuery(selectUser, null).getCount() == 1) {
            // grab the hashed password from the database
            String selectHashedPass = format("SELECT passwrd FROM user WHERE username = '%s';", username);
            Cursor cursor = db.rawQuery(selectHashedPass, null);
            cursor.moveToFirst();
            @SuppressLint("Range") String hashedPass = cursor.getString(cursor.getColumnIndex("passwrd"));
            return Password.check(passwrd, hashedPass).withSCrypt(); // check the password
        }
        return false;
    }


}
