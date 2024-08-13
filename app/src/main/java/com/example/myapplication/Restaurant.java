package com.example.myapplication;

/**
 * Represents a Restaurant with a name, location, and hours of operation.
 */
public class Restaurant {
    private String name;
    private String location;
    private String hours;
    private Double stars;
    private Integer imageResource;


    /**
     * Constructs a Restaurant with a name, location, and hours of operation.
     *
     * @param name     name of the restaurant
     * @param hours    hours of operation
     * @param location location of the restaurant
     * @param stars    rating of the restaurant
     */
    public Restaurant(String name, String hours, String location, Double stars) {
        this.name = name;
        this.hours = hours;
        this.location = location;
        this.stars = stars;
        this.imageResource = R.drawable.default_icon;
    }

    /**
     * Returns the name of the restaurant.
     *
     * @return name of the restaurant
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the restaurant.
     *
     * @param name name of the restaurant
     */
    public void setRestaurantName(String name) {
        this.name = name;
    }

    /**
     * Returns the location of the restaurant.
     *
     * @return location of the restaurant
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the restaurant.
     *
     * @param location location of the restaurant
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the hours of operation of the restaurant.
     *
     * @return hours of operation of the restaurant
     */
    public String getHours() {
        return hours;
    }

    /**
     * Sets the hours of operation of the restaurant.
     *
     * @param hours hours of operation of the restaurant
     */
    public void setHours(String hours) {
        this.hours = hours;
    }

    /**
     * Returns the rating of the restaurant.
     *
     * @return rating of the restaurant
     */
    public Double getStars() {
        return stars;
    }

    /**
     * Sets the rating of the restaurant.
     *
     * @param stars rating of the restaurant
     */
    public void setStars(Double stars) {
        this.stars = stars;
    }

    /**
     * Returns the image resource ID of the restaurant.
     *
     * @return image resource ID of the restaurant
     */
    public Integer getImageResource() {
        return imageResource;
    }

    /**
     * Sets the image resource ID of the restaurant.
     *
     * @param imageResource image resource ID of the restaurant
     */
    public void setImageResource(Integer imageResource) {
        this.imageResource = imageResource;
    }
}