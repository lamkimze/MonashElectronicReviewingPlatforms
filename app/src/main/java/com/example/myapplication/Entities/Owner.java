package com.example.myapplication.Entities;

public class Owner extends User{

    // Constructor for when the user tries to log in
    public Owner(String username, String password) {
        super(username);
    }

    // Constructor for when the user is created
    public Owner(int id, String username, String email, String firstName, String lastName, String createdAt) {
        super(id, username, email, firstName, lastName, createdAt);
    }

    @Override
    public String getTableName() {
        return "owner";
    }
}
