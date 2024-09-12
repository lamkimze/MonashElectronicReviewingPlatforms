package com.example.myapplication.Entities;

import java.util.ArrayList;

public abstract class User {
    private int id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private ArrayList<Integer> favourites = new ArrayList<>();
    private String position;


    // Constructor for when the user tries to log in
    public User(String username) {
        this.username = username;
    }

    // Constructor for when the user is created
    public User(String username,  String email, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    abstract public String getTableName();

    abstract public String getPkName();

    public ArrayList<Integer> getFavourites() {
        return favourites;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setFavourites(ArrayList<Integer> favourites) {
        this.favourites = favourites;
    }
}
