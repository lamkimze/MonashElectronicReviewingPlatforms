package com.example.myapplication;

import java.util.Date;

public class Restaurant {
    private int id;
    private int ownerID;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String hours;
    private Double stars;
    private final Integer imageResource;
    private final String pictureUrl;
    private final String logoUrl;
    private String cuisine;
    private static int accumulatedReview = 0;
    private static int accumulateMonth = 0;
    private int monthlyReview = 0;
    private int seasonReview = 0;
    private int halfYearReview = 0;
    private int yearlyReview = 0;
    private int goldMedalNo = 0;
    private int silverMedalNo = 0;
    private int bronzeMedalNo = 0;
    protected int placeChange = -1;




    public Restaurant(String name, String pictureUrl, String logoUrl){
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.logoUrl = logoUrl;
        this.imageResource = R.drawable.default_icon;
        this.stars = 0.0;
    }


//    getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public Double getStars() {
        return stars;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }

    public static int getAccumulateMonth() {
        return accumulateMonth;
    }

    public int getPlaceChange() {
        return placeChange;
    }

    public void incrementReview(){
        accumulatedReview ++;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public int getAccumulatedReview() {
        return accumulatedReview;
    }

    public int getGoldMedalNo() {
        return goldMedalNo;
    }

    public int getSilverMedalNo() {
        return silverMedalNo;
    }

    public int getBronzeMedalNo() {
        return bronzeMedalNo;
    }

    public int getMonthlyReview() {
        return monthlyReview;
    }

    public int getSeasonReview() {
        return seasonReview;
    }

    public int getHalfYearReview() {
        return halfYearReview;
    }

    public int getYearlyReview() {
        return yearlyReview;
    }

    public Integer getImageResource() {
        return imageResource;
    }

    public void setCuisine(String busCuisineType) {
        this.cuisine = busCuisineType;
    }

    public String getCuisine() {
        return cuisine;
    }
}
