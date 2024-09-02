package com.example.myapplication.Entities;

public class Owner extends User{

    private int managingBusinessId;

    // Constructor for when the user tries to log in
    public Owner(String username, String password) {
        super(username);
    }

    // Constructor for when the user is created
    public Owner(String username, String email, String firstName, String lastName) {
        super(username, email, firstName, lastName);
    }


    public int getManagingBusinessId() {
        return managingBusinessId;
    }

    public void setManagingBusinessId(int managingBusiness) {
        this.managingBusinessId = managingBusiness;
    }

    @Override
    public String getTableName() {
        return "owner";
    }


}
