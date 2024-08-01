package com.example.myapplication;

/**
 * Represents a Restaurant with a name, location, and hours of operation.
 */
public class Restaurant {
    private String name;
    private String location;
    private String hours;

    /**
     * Constructs a new Restaurant with the specified name, location, and hours.
     *
     * @param name the name of the restaurant
     * @param location the location of the restaurant
     * @param hours the hours of operation of the restaurant
     */
    public Restaurant(String name, String location, String hours) {
        this.name = name;
        this.location = location;
        this.hours = hours;
    }

    /**
     * Returns the name of the restaurant.
     *
     * @return the name of the restaurant
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the restaurant.
     *
     * @param name the new name of the restaurant
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the location of the restaurant.
     *
     * @return the location of the restaurant
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the restaurant.
     *
     * @param location the new location of the restaurant
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the hours of operation of the restaurant.
     *
     * @return the hours of operation of the restaurant
     */
    public String getHours() {
        return hours;
    }

    /**
     * Sets the hours of operation of the restaurant.
     *
     * @param hours the new hours of operation of the restaurant
     */
    public void setHours(String hours) {
        this.hours = hours;
    }

}