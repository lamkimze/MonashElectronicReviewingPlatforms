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
        String userTable = user.getTableName();
        String selectUser = format("SELECT * FROM %s WHERE username = '%s';", userTable, username);
        // Check if the user exists
        Cursor cursor = db.rawQuery(selectUser, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            @SuppressLint("Range") String hashedPass = cursor.getString(cursor.getColumnIndex("password"));
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
            String userTable = user.getTableName();
            String selectUser = format("SELECT * FROM %s WHERE username = '%s';", userTable, username);
            Cursor cursor = db.rawQuery(selectUser, null);
            cursor.moveToFirst();

            // get the indices of the columns
            int[] indices = {cursor.getColumnIndex("id"),
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
        String selectUser = format(locale,"SELECT * FROM %s WHERE username = '%s' OR email = '%s';", userTable, username, email);
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
        db.insert(userTable, null, contentValues);
        return true;
    }

    // Read methods

    /**
     * grabs the bus_id of the owner i.e. the business they own
     * @param owner the owner to get the bus_id of
     * @return the bus_id of the owner
     */
    public int getOwnersBusID(Owner owner) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int ownerID = owner.getId();
        String selectBusID = format(locale,"SELECT bus_id FROM owner WHERE owner_id = %d;", ownerID);
        Cursor cursor = db.rawQuery(selectBusID, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("bus_id");
        if (index == -1) {
            cursor.close();
            return -1;
        }
        int busID = cursor.getInt(index);
        cursor.close();
        return busID;
    }

    /**
     * grabs the bus_id of the owner i.e. the business they own
     * @param customer the customer to get the bus_id of
     * @return the bus_id of the customer
     */
    public int getCustomerID(Customer customer) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = customer.getUsername();
        String selectCustomerID = format("SELECT customer_id FROM customer WHERE username = '%s';", username);
        Cursor cursor = db.rawQuery(selectCustomerID, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("customer_id");
        if (index == -1) {
            cursor.close();
            return -1;
        }
        int customerID = cursor.getInt(index);
        cursor.close();
        return customerID;
    }

    /**
     * Method to get the owner ID of an owner
     * @param owner the owner to get the ID of
     * @return the ID of the owner
     */
    public int getOwnerID(Owner owner) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = owner.getUsername();
        String selectOwnerID = format("SELECT owner_id FROM owner WHERE username = '%s';", username);
        Cursor cursor = db.rawQuery(selectOwnerID, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("owner_id");
        if (index == -1) {
            cursor.close();
            return -1;
        }
        int ownerID = cursor.getInt(index);
        cursor.close();
        return ownerID;
    }

    /**
     * Method to get the customer object from the database
     * @param customerID the ID of the customer to get
     * @return the customer object
     */
    public Customer getCustomer (int customerID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectCustomer = "SELECT * FROM customer WHERE customer_id = " + customerID + ";";
        Cursor cursor = db.rawQuery(selectCustomer, null);
        cursor.moveToFirst();
        int[] indices = {cursor.getColumnIndex("username"),
                cursor.getColumnIndex("email"),
                cursor.getColumnIndex("first_name"),
                cursor.getColumnIndex("last_name")};

        if (Arrays.stream(indices).anyMatch(i -> i == -1)) {
            cursor.close();
            return null;
        } else {
            Customer customer = new Customer(cursor.getString(indices[0]),
                    cursor.getString(indices[1]),
                    cursor.getString(indices[2]),
                    cursor.getString(indices[3]));
            cursor.close();
            return customer;
        }
    }

    /**
     * Method to get the owner object from the database
     * @param ownerID the ID of the owner to get
     * @return the owner object
     */
    public Owner getOwner (int ownerID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectOwner = "SELECT * FROM owner WHERE owner_id = " + ownerID + ";";
        Cursor cursor = db.rawQuery(selectOwner, null);
        cursor.moveToFirst();
        int[] indices = {cursor.getColumnIndex("username"),
                cursor.getColumnIndex("email"),
                cursor.getColumnIndex("first_name"),
                cursor.getColumnIndex("last_name")};

        if (Arrays.stream(indices).anyMatch(i -> i == -1)) {
            cursor.close();
            return null;
        } else {
            Owner owner = new Owner(cursor.getString(indices[0]),
                    cursor.getString(indices[1]),
                    cursor.getString(indices[2]),
                    cursor.getString(indices[3]));
            cursor.close();
            return owner;
        }
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
        String updateStatement = "UPDATE owner SET bus_id = %d WHERE owner_id = %d;";
        String updateOwner = format(locale,updateStatement, busID, ownerID);
        db.execSQL(updateOwner);
    }

}


