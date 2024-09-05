package com.example.myapplication.Database;


import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Entities.Customer;
import com.example.myapplication.Entities.Owner;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Restaurant;
import com.password4j.Password;

import java.util.ArrayList;


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
    public Boolean createUser(User user, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String userTable = user.getTableName();
        String username = user.getUsername();
        String email = user.getEmail();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String hashedPass = Password.hash(password).withScrypt().getResult();

        // check if email or username already exists
        String selectUser = format("SELECT * FROM %s WHERE username = '%s' OR email = '%s';", userTable, username, email);
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectUser, null);
        if (cursor.getCount() > 0) {
            return false;
        }

        // insert the user into the database
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
     * grabs the bus_id of the owner i.e. the business they own
     * @param customer the customer to get the bus_id of
     * @return the bus_id of the customer
     */
    @SuppressLint("Range")
    public int getCustomerID(Customer customer) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = customer.getUsername();
        String selectCustomerID = format("SELECT id FROM customer WHERE username = '%s';", username);
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectCustomerID, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("id"));
    }

    /**
     * Method to get the owner ID of an owner
     * @param owner the owner to get the ID of
     * @return the ID of the owner
     */
    @SuppressLint("Range")
    public int getOwnerID(Owner owner) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = owner.getUsername();
        String selectOwnerID = format("SELECT id FROM owner WHERE username = '%s';", username);
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectOwnerID, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("id"));
    }

    /**
     * Method to get the customer object from the database
     * @param customerID the ID of the customer to get
     * @return the customer object
     */
    @SuppressLint("Range")
    public Customer getCustomer (int customerID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectCustomer = "SELECT * FROM customer WHERE id = " + customerID + ";";
        Cursor cursor = db.rawQuery(selectCustomer, null);
        cursor.moveToFirst();
        return new Customer(cursor.getString(cursor.getColumnIndex("username")),
                cursor.getString(cursor.getColumnIndex("email")),
                cursor.getString(cursor.getColumnIndex("first_name")),
                cursor.getString(cursor.getColumnIndex("last_name")));
    }

    /**
     * Method to get the owner object from the database
     * @param ownerID the ID of the owner to get
     * @return the owner object
     */
    @SuppressLint("Range")
    public Owner getOwner (int ownerID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectOwner = "SELECT * FROM owner WHERE id = " + ownerID + ";";
        Cursor cursor = db.rawQuery(selectOwner, null);
        cursor.moveToFirst();
        return new Owner(cursor.getString(cursor.getColumnIndex("username")),
                cursor.getString(cursor.getColumnIndex("email")),
                cursor.getString(cursor.getColumnIndex("first_name")),
                cursor.getString(cursor.getColumnIndex("last_name")));
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


