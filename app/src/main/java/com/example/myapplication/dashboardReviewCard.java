package com.example.myapplication;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;

import java.util.ArrayList;

public class dashboardReviewCard {


    private String reviewTitle;
    private float reviewRating;
    private ArrayList<Bitmap> reviewImages;
    private String reviewText;
    private int reviewerId;
    private int businessId;
    private int reviewId;
    private Bitmap profilePic;
    private String reviewDate;
    private String reviewUsername;
    private String businessName;
    private ArrayList<Integer> likes;
    private ArrayList<Integer> dislikes;

    public dashboardReviewCard(String reviewTitle, float reviewRating, ArrayList<Bitmap> reviewImages, String reviewText, int reviewerId, int businessId, int reviewId, Bitmap profilePic, String reviewDate, String reviewUsername, String businessName, ArrayList<Integer> likes, ArrayList<Integer> dislikes) {
        this.reviewTitle = reviewTitle;
        this.reviewRating = reviewRating;
        this.reviewImages = reviewImages;
        this.reviewText = reviewText;
        this.reviewerId = reviewerId;
        this.businessId = businessId;
        this.reviewId = reviewId;
        this.profilePic = profilePic;
        this.reviewDate = reviewDate;
        this.reviewUsername = reviewUsername;
        this.businessName = businessName;
        this.likes = likes;
        this.dislikes = dislikes;
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

    public int getReviewId() {
        return reviewId;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public String getReviewUsername() {
        return reviewUsername;
    }

    public String getBusinessName() {
        return businessName;
    }

    public ArrayList<Integer> getLikes() {
        return likes;
    }

    public ArrayList<Integer> getDislikes() {
        return dislikes;
    }
}
