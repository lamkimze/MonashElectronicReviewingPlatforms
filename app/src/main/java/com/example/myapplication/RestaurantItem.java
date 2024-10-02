package com.example.myapplication;

import android.graphics.Bitmap;

public class RestaurantItem {
    String name;
    String rating;
    Bitmap image;

    public RestaurantItem(String name, String rating, Bitmap image) {
        this.name = name;
        this.rating = rating;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
