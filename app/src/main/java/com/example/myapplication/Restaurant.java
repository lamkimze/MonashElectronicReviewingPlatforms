package com.example.myapplication;

import java.util.Comparator;
import java.util.Date;

public class Restaurant extends RestaurantReviewCompetition {
    private int id;
    private int ownerID;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String hours;
    private float stars;
    private final Integer imageResource;
    private final String pictureUrl;
    private final String logoUrl;
    private String cuisine;


    public Restaurant(String name, String pictureUrl, String logoUrl){
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.logoUrl = logoUrl;
        this.imageResource = R.drawable.default_icon;
        this.stars = 0.0F;
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

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
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


    public Integer getImageResource() {
        return imageResource;
    }

    public void setCuisine(String busCuisineType) {
        this.cuisine = busCuisineType;
    }

    public String getCuisine() {
        return cuisine;
    }

    public static Comparator<Restaurant> medalAscending = new Comparator<Restaurant>() {
        @Override
        public int compare(Restaurant r1, Restaurant r2) {
            int medal1 = Integer.valueOf(r1.getGoldMedalNo());
            int medal2 = Integer.valueOf(r2.getGoldMedalNo());
            return Integer.compare(medal1, medal2);
        }
    };

    public static Comparator<Restaurant> nameAscending = new Comparator<Restaurant>() {
        @Override
        public int compare(Restaurant r1, Restaurant r2) {
            String name1 = r1.getName().toLowerCase();
            String name2 = r2.getName().toLowerCase();
            return name1.compareTo(name2);
        }
    };

    public static Comparator<Restaurant> ratingAscending = new Comparator<Restaurant>() {
        @Override
        public int compare(Restaurant r1, Restaurant r2) {
            double rating1 = r1.getStars();
            double rating2 = r2.getStars();
            return Double.compare(rating1, rating2);
        }
    };

}
