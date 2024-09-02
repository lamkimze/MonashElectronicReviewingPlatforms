package com.example.myapplication;

public class ReviewModel {
    String reviewTitle;
    int reviewRating;
    int reviewImage;

    public ReviewModel(int reviewRating, String reviewTitle, int reviewImage) {
        this.reviewRating = reviewRating;
        this.reviewTitle = reviewTitle;
        this.reviewImage = reviewImage;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    public int getReviewImage() {
        return reviewImage;
    }
}
