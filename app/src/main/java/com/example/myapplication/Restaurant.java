package com.example.myapplication;

/**
 * Represents a Restaurant with a name, location, and hours of operation.
 */
public class Restaurant {
    private String name;
    private String location;
    private String hours;

    /**
     * @param name name of the restaurant
     * @param hours hours of operation
     * @param location location of the restaurant
     */
    public Restaurant(String name, String hours, String location) {
        this.name = name;
        this.hours = hours;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setRestaurantName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}