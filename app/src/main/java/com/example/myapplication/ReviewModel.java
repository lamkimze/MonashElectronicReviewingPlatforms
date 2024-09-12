package com.example.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ReviewModel {
    String reviewTitle;
    float reviewRating;
    ArrayList<Bitmap> reviewImages;
    String reviewText;
    int reviewerId;
    int businessId;

    public ReviewModel(float reviewRating, String reviewTitle, ArrayList<Bitmap> reviewImages, String reviewText, int reviewerId, int businessId) {
        this.reviewRating = reviewRating;
        this.reviewTitle = reviewTitle;
        this.reviewImages = reviewImages;
        this.reviewText = reviewText;
        this.reviewerId = reviewerId;
        this.businessId = businessId;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public float getReviewRating() {
        return reviewRating;
    }

    public ArrayList<Bitmap> getReviewImages() {
        return reviewImages;
    }

    public String getReviewText() {
        return reviewText;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public int getBusinessId() {
        return businessId;
    }
    
}
