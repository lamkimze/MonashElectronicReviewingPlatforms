package com.example.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ReviewModel {
    String reviewTitle;
    int reviewRating;
    ArrayList<Bitmap> reviewImages;
    String reviewText;
    int reviewerId;
    int businessId;

    public ReviewModel(int reviewRating, String reviewTitle, ArrayList<Bitmap> reviewImages, String reviewText, int reviewerId, int businessId) {
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

    public int getReviewRating() {
        return reviewRating;
    }

    public ArrayList<Bitmap> getReviewImages() {
        return reviewImages;
    }
}
