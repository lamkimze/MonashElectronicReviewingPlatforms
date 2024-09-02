package com.example.myapplication.Entities;

public class Owner extends User{

    private int busID;

    // Constructor for when the user tries to log in
    public Owner(String username) {
        super(username);
    }

    // Constructor for when the user is created
    public Owner(String username, String email, String firstName, String lastName) {
        super(username, email, firstName, lastName);
    }


    public int getBusID() {
        return busID;
    }

    public void setBusID(int busID) {
        this.busID = busID;
    }

    @Override
    public String getTableName() {
        return "owner";
    }


}
