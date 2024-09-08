package com.example.myapplication.Entities;

public class Owner extends User{

    // Constructor for when the user tries to log in
    public Owner(String username, String password) {
        super(username);
    }

    // Constructor for when the user is created
    public Owner(String username, String email, String firstName, String lastName) {
        super(username, email, firstName, lastName);
    }

    @Override
    public String getTableName() {
        return "owner";
    }

    @Override
    public String getPkName() {
        return "owner_id";
    }
}
