package com.example.myapplication.Entities;

public abstract class User {
    private int id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String createdAt;


    // Constructor for when the user tries to log in
    public User(String username) {
        this.username = username;
    }

    // Constructor for when the user is created
    public User(int id, String username,  String email, String firstName, String lastName, String createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    abstract public String getTableName();
}
