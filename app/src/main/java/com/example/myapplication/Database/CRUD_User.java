package com.example.myapplication.Database;


import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Entities.Owner;
import com.example.myapplication.Entities.User;
import com.password4j.Password;


public class CRUD_User {

    DatabaseHelper dbHelper;

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
    @SuppressLint("Recycle")
    public boolean verifyLogin(User user, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = user.getUsername();
        String userTable = user.getTableName();
        String selectUser = format("SELECT * FROM %s WHERE username = '%s';", userTable, username);
        // Check if the user exists
        Cursor cursor = db.rawQuery(selectUser, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            @SuppressLint("Range") String hashedPass = cursor.getString(cursor.getColumnIndex("password"));
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
    @SuppressLint("Range")
    public User loginUser(User user, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // verify the login credentials
        if (verifyLogin(user, password)) {
            // use the users setter methods to set the user's information
            String username = user.getUsername();
            String userTable = user.getTableName();
            String selectUser = format("SELECT * FROM %s WHERE username = '%s';", userTable, username);
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectUser, null);
            cursor.moveToFirst();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
            user.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
            user.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
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
    public boolean createUser(User user, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String userTable = user.getTableName();
        String username = user.getUsername();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String hashedPass = Password.hash(password).withScrypt().getResult();

        // Check if the user already exists
        String selectUser = format("SELECT * FROM %s WHERE username = '%s';", userTable, username);
        if (db.rawQuery(selectUser, null).getCount() == 1) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("first_name", firstName);
        contentValues.put("last_name", lastName);
        contentValues.put("password", hashedPass);
        db.insert(userTable, null, contentValues);
        return true;
    }

    // Read methods

    /**
     * grabs the bus_id of the owner i.e. the business they own
     * @param owner the owner to get the bus_id of
     * @return the bus_id of the owner
     */
    @SuppressLint("Range")
    public int getOwnersBusID(Owner owner) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int ownerID = owner.getId();
        @SuppressLint("DefaultLocale") String selectBusID = format("SELECT owner_id FROM owner WHERE id = %d;", ownerID);
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectBusID, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("bus_id"));
    }

    /**
     * grabs the user_id of the user
     * @param user the user to get the user_id of
     * @return the user_id of the user
     */
    @SuppressLint("Range")
    public int getUserID(User user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = user.getUsername();
        String userTable = user.getTableName();
        @SuppressLint("DefaultLocale") String selectUserID = format("SELECT id FROM %s WHERE username = '%s';", userTable, username);
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectUserID, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("id"));
    }

    // Update methods

    /**
     * Assigns an owner to a bus
     * Note this method only updates the database, it does not update the owner object
     * @param owner the owner to assign to the bus
     * @param busID the bus to assign the owner to
     */
    public void assignOwnerToBus(Owner owner, int busID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int ownerID = owner.getId();
        String updateStatement = "UPDATE owner SET bus_id = %d WHERE id = %d;";
        @SuppressLint("DefaultLocale") String updateOwner = format(updateStatement, busID, ownerID);
        db.execSQL(updateOwner);
    }

}
