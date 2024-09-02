package com.example.myapplication.Entities;

public class Customer extends User{

    // Constructor for when the user tries to log in
    public Customer(String username) {
        super(username);
    }

    // Constructor for when the user is created
    public Customer(String username, String email, String firstName, String lastName) {
        super(username, email, firstName, lastName);
    }

    @Override
    public String getTableName() {
        return "customer";
    }
}
